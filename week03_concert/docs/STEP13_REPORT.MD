# 캐싱 분석 리포트

## 캐싱 공부 내용
### 알게된 점
- 캐싱이 적용된 데이터는 데이터의 정확도를 보장할 수 없다
  - DB에 저장된 영속 데이터와 시간 차이로 인해 데이터가 정확히 일치되지는 않는다
  - 따라서 실시간성이 극도로 중요한 곳에는 이용하지 않는 것이 바람직할 수 있다
- 캐싱을 적용하는 주된 이유는 '순간적인 부하 감소'이다
  - 캐싱은 데이터를 일정 시간동안만 저장했다가 그 자료를 응답한다
  - 따라서 평상시보다는, 데이터가 몰리는 피크타임 때 빛을 발한다
- 피크타임때 캐싱의 유용성
  - 서버 트래픽이 과도해지는 이유는 대부분 순간 트래픽이 몰려서이다
  - 따라서 캐싱은 부하 피크 상태에 중점을 둔다면 더 용이하게 쓸 수 있다
- 물론 평상시에도 캐싱이 유용할 때가 있다
  - 추천 알고리즘과 같은 몇몇 로직은 기본적으로 부하가 클 수 있다
  - 따라서 이런 일시적이면서도 부하가 높은 로직을 대상으로 유용할 수 있다

### 캐싱 전략 분석
#### 읽기
- Look Aside
  - 캐시 확인
  - miss시 db에 접근
- Read Through
  - 캐시 확인
  - miss시 redis가 db로부터 업데이트
#### 쓰기
- Write Back
  - 업데이트 내역을 일단 캐시에 보관
  - 한 번씩 db에 저장
- Write Through
  - 캐시에 저장
  - redis가 알아서 db에 저장
- Write Around
  - db에 저장
  - 캐시도 같이 삭제 또는 변경

## 캐싱 적용 가능 로직 분석
### 대기열
#### 적용 이유
- 대기열 토큰은 서버에 접근하기 위한 임시 토큰
  - 토큰이 사라져도 재발급에 큰 무리가 없음
- 이용량이 매우 높음
  - 모든 콘서트 api 접근시에 필요
  - 매우 높은 사용 빈도 발생
- 따라서 Redis에서만 관리해도 무리가 없는 데이터이기에 DB와 분리시키면 효율성 증가 가능

#### 적용 방법
- 성능 측정 테스트 우선 작성 `performance/CacheTest.java`
  - 아래 함수의 실행 시간을 측정
```java
List<CompletableFuture<String>> tasks = IntStream.range(0, numOfUsers)
      .<CompletableFuture<String>>mapToObj( i -> CompletableFuture.supplyAsync(() -> {
          Long userId = users.get(i).getId();

          UUID token = tokenFacade.issue(userId).getToken();
          while (true) {
              try {
                  tokenFacade.refreshTokenQueue(1);
                  tokenFacade.check(userId, token.toString());
                  break;
              } catch (Exception ignored) {
                  log.info(ignored.getMessage());
              }

              try {
                  Thread.sleep(1000);
              } catch (InterruptedException e) {
                  throw new RuntimeException(e);
              }
          }

          return null;
      })).toList();
```
- Infra 레이어를 Redis로 교체
  - 기존 환경 : `TokenRepository` 는 JPA interface를 상속
  - 변경 :
    - `TokenRepository`를 pure한 interface로 변경
    - `TokenRepository`를 상속받는 `TokenRepositoryRedisImpl` 제작 및 Primary Bean 등록

#### 레디스 적용 후기
- 성능 측정
  - 3000명의 유저가 토큰을 발급하고, token이 active될 때 까지 대기(activation은 token 발급과 동시에 수행)
  - 기존 대비 **50%** 이상 성능 향상
  - AS-IS(RDB) : ```Total time: 19927 ms```
  - TO-BE(Redis) : ```Total time: 8213 ms```
- 클린아키텍처
  - Service의 변경 없이 Infra 구성 변경만으로 Redis 적용 가능했음
  - 그러나 성능 향상을 더 끌어올리기 위해서는 redis에 맞게 infra 코드를 튜닝해야 할 것 같음

#### 향후 확장 가능성
- K6, JMeter, NGrinder 등을 이용한 부하 분석 실행 가능
- Redis 적용에 따른 성능 튜닝 가능
  - 현재 코드는 JPA 기반 기존 로직을 그대로 활용
  - 인프라만 JPA에서 Redis 구현체로 교체됨
  - 따라서 Redis의 기능을 더 잘 활용할 수 있는 가능성 열려있음(status 전환을 한 함수에서 진행하는 등)

### 시간대별 잔여좌석 갯수
#### 적용 이유
- 시간대별 잔여좌석 수는 실시간성 보장 필요 없음
  - 이용자 흐름을 보면, 잔여좌석 수 확인 후에 좌석 선택으로 넘어감
  - 그 사이에 시간간격이 있다는 것을 이용자도 알기에 정확한 데이터를 넘겨줄 필요는 없음
- 이용량이 비교적 높음
  - 콘서트 예매 이용자가 모두 거쳐가는 유즈케이스
  - 예매가 몰리는 시간대에 트래픽이 몰릴 수 있음
- 따라서 캐싱을 통해 사용자 경험을 해치지 않으며 DB 부하를 줄일 수 있음

#### 적용 방법
- 성능 측정 테스트 우선 작성 `performance/CacheTest.java`
  - 아래 함수의 실행 시간을 측정
```java
for (int i = 0; i < numOfViews; i++) {
    concertFacade.findConcertTimeslots(concertId);
}
```
- Spring `@Cachable`을 이용하여 캐시 적용
  - `RedisCacheConfig` 적용하여 Cacheable 로드
  - `@Cacheable(value = "concert:timeslot", key = "#concertId", cacheManager = "redisCacheManager")`
  - TTL은 3초로 세팅했으나, 글로벌 값으로 지정되어 있기 때문에 별도 ttl 적용방법 찾아야 할 듯

#### 캐시 적용 후기(성능)
- 10,000번의 concert timeslot 조회를 수행
- 기존 대비 **90%** 이상 성능 향상
- AS-IS : ```Total time: 21782 ms```
- TO-BE : ```Total time: 2773 ms```

#### 향후 확장 가능성
- 캐시별 별도의 ttl을 구성할 수 있는 방법 찾아봐야 할 듯

## 이외에도 캐싱 적용 가능한 API에 대한 고찰
- 캐싱은 주로 부하를 많이 차지하면서 실시간성에서 자유로운 로직에 대해 수행하면 효율적일 것 같음
- 아래의 기능이 있다면, 캐싱 적용이 유리하다고 생각
  - 주간, 일간, 월간 콘서트 랭킹
    - 리프레시되는 시간이 매우 길다
    - 집계하는 연산 비용이 매우 높다
  - 개인 추천 콘텐츠
    - 추천 알고리즘의 연산 비용이 매우 높다
    - 그러나 유저가 다양한 추천 콘텐츠를 원할 수 있으므로, 미리 여러 목록을 뽑아놓고 랜덤으로 보여주는 것도 괜찮을 것 같다
  - 공지사항
    - 유저가 콘서트 취소 같은 알림을 받으면 유저가 몰릴 가능성이 존재한다
    - 공지사항이 수정될 수도 있긴 하지만, 이는 알림을 통해 재전달되기에 유저가 변경 여부를 파악할 수 있다
    - TTL을 짧게 10초정도로 잡는다면, 부하에 대비하면서도 어느정도의 실시간성을 가져갈 수 있다
    - 혹은 공지사항이 자주 변경되지는 않으므로 동시성 문제에서 자유롭다. 따라서 공지사항 변경시 캐시 업데이트도 즉시 수행해도 될 것 같다.
