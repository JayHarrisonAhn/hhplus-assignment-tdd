# TDD 프로젝트

과제 : 동시성 제어 방식에 대한 분석 및 보고서 작성

## 요구사항
일부 포인트 증감 액션에 동시성이 보장되어야 한다.

- **다른 유저간 액션** -> 동시에 발생해도 상관없다.(동시성 제어 필요없음)
- **한 유저에서 발생하는 여러 액션** -> 원자성이 보장돼야 한다.
- **`PointHistoryTable`, `UserPointTable`** -> 쓰기 액션에 원자성이 필요하다.(내부 cursor가 atomic하지 않기 때문)

## 알아본 내용

- monitor lock : Java의 고유 락
- Compare and Swap(lock free 알고리즘)
  - 기존 값과 변경할 값을 전달
  - 기존 값과 메모리가 같다면 값을 변경하고 true 리턴
  - 기존 값과 메모리가 다르다면 false 반환


## 해결책 후보 탐색

### 1. `synchronized`, `ReentranceLock`, `AtomicHashMap` 등
한 번에 하나의 스레드만 접속을 허용(배타성 보장)하는 장치들이다.

확실한 제어 방법이긴 하지만, 동시접근이 아예 불가능하기 때문에 성능 향상에 한계점이 있다. 

### 2. `ReadWriteLock`
읽기 작업에는 여러 스레드의 동시접근을 허용, 쓰기 작업에는 한 스레드만 접근을 허용(배타성 보장)한다.

읽기 작업을 동시에 할 수 있기 때문에 1번보다 성능이 다소 향상된다.


### 3. `ConcurrentHashMap`
`AtomicHashMap` : 모든 내부 요소를 통틀어서 한 스레드만 접근 허용

`ConcurrentHashMap` : 여러 스레드가 동시 접속 가능하지만, 한 key에는 한 스레드만 접속 허용 

Key별로 원자성을 제공하기 때문에 동시에 여러 lock을 다루는 데 성능 향상을 기대할 수 있다.(hash bucket별 원자성 제공)


## 해결책 선정 및 적용
### 한 유저에서 발생하는 여러 액션 : `ConcurrentHashMap`
PointService에서 한 유저에 대해 수행하는 여러 메소드는 원자성이 보장되어야 한다.

다른 유저에 대해서는 알 바가 아니기 때문에 key에 기반한 locking 정책이 필요하다.

유저별 원자성을 보장하는 `PointTableAccessor`에 관련 로직을 몰아놓고, access 함수를 통해 접근 가능하다.

```java
@Component
@RequiredArgsConstructor
public class PointTableAccessor {
    private final ConcurrentHashMap<Long, Object> userLocks = new ConcurrentHashMap<>();

    public <T> T access (long userId, PointTableAccessOperator<T> operator) {
        AtomicReference<T> result = new AtomicReference<>();
        userLocks.compute(userId, (k, v) -> {
            result.set(operator.run());
            return v;
        });
        return result.get();
    }
}
```

### PointHistoryTable, UserPointTable : `ReadWriteLock`
내부 변수인 cursor는 insert 메소드 실행 시 값이 변경된다.

따라서 쓰기에만 원자성 보장이 필요하다.

Repository에 관련 책임을 할당하고, table에 대한 모든 접근을 제어한다.

```java
@Repository
@RequiredArgsConstructor
public class PointHistoryRepository {

    final private PointHistoryTable pointHistoryTable;

    final private ReadWriteLock tableLock = new ReentrantReadWriteLock();

    public PointHistory insert(PointHistoryDTO pointHistoryDTO) {
        Lock lock = tableLock.writeLock();
        PointHistory pointHistory = this.pointHistoryTable.insert(
                pointHistoryDTO.userId(),
                pointHistoryDTO.amount(),
                pointHistoryDTO.type(),
                pointHistoryDTO.updateMillis()
        );
        lock.unlock();
        return pointHistory;
    }

    public List<PointHistory> selectAllByUserId(long userId) {
        Lock lock = tableLock.readLock();
        List<PointHistory> pointHistories = this.pointHistoryTable.selectAllByUserId(userId);
        lock.unlock();
        return pointHistories;
    }
}
```

## 테스트 작성
### 멀티스레드 생성 방법
멀티스레드 생성 방법에는 Executor, Future, Flow 등이 있다.

Flow는 아쉽지만 1.9부터 지원하므로 패스

동적인 테스트 구성에 Executor가 더 적합할 것 같아서 이것으로 결정했다.

### 테스트 작성 방법
n명의 유저에 대해 각각 n번의 연산을 동시에 수행하는 함수인 `executeRepeatPerUser`을 만들어 재사용

동시적으로 모든 연산을 수행한 후, 남아있는 결과중 모순된 것이 없는지 검사

### 하드웨어 특성에 대한 고민
이론적으로는 아무리 멀티스레드로 돌려도 동시성을 전혀 적용하지 않은 것 처럼 작동하는 환경이 있을 수 있다
(예를 들어 cpu와 운영체제가 모두 싱글코어에 non-blocking 정책일 경우)
혹은, 기적의 확률로 동시에 돌지 않을 수도 있다.

이런 상황일 경우, 문제가 있는 동시성 로직이 테스트를 통과할 가능성이 있다.

확률적 문제를 최대한 낮추는 방법으로 접근하면 좋다. @Repeats를 통해 여러번 실행하는 등

## 결론
### 작동
- PointService를 통한 모든 요청은 두 개의 락을 얻어야 Table에 접근 가능하다(User Lock & Table Lock)
- User Lock은 key based ConcurrentHashMap
- Table Lock은 ReadWriteLock
- 일방향으로 얻어가기 때문에 데드락 발생하지 않음
- 읽기 요청에서 성능 향상 기대 가능

### 배운 점
- 동시성 제어 정책(Lock, synchronized, ConcurrentHashMap 등) 학습
- 테스트에 있어 멱등성의 중요성
- AOP 관점에서 동시성 코드 분리의 필요셩

### 추가 개선점
- 현재는 동시성 코드 실행을 lambda를 통하지만, annotation을 이용해서 더 깔끔하게 개선 가능