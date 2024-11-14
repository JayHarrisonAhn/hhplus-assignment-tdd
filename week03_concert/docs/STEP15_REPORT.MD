# 인덱스에 의한 쿼리 성능 개선
## 인덱싱 필요한 쿼리
### 콘서트 시간 조회 쿼리
ConcertTimeslot(콘서트 시간 Table) + ConcertTimeslotOccupancy(콘서트 시간별 잔여 좌석 수 Table)

콘서트별 시간 조회 + 정렬 + Join으로 인해 DB가 아파할 수 있다
```jpaql
SELECT new com.example.concert.concert.dto.ConcertTimeslotWithOccupancy(
    ts.id,
    ts.concertId,
    ts.concertStartTime,
    ts.reservationStartTime,
    occ.maxSeatAmount,
    occ.occupiedSeatAmount
)
FROM
    ConcertTimeslot ts
    INNER JOIN
    ConcertTimeslotOccupancy occ
ON
    ts.concertId=:concertId
    AND ts.id=occ.concertTimeslotId
ORDER BY
    ts.concertStartTime
```

## 인덱스 적용
`ConcertTimeslot`
```java
@Table(
        indexes = @Index(
                name = "idx_concert_id",
                columnList = "concertId, reservationStartTime"
        )
)
```

## 쿼리 성능 측정
### 더미데이터 축적
10000건의 콘서트 시간 정보 : 콘서트 100개 & 콘서트당 timeslot 100개를 삽입
```java
@Test
void insertDummyData() {
    for (int i = 0; i < 100; i++) {
        // Create Concert
        Concert concert = concertFacade.createConcert("콘서트 " + i);

        for (int j=0; j<100; j++) {
            LocalDateTime concertDateTime = LocalDateTime.now().plusDays(10 + j);
            concertFacade.createConcertTimeslot(
                    concert.getId(),
                    concertDateTime,
                    concertDateTime.minusDays(1)
            );
        }
    }
}
```

### 쿼리 적용 여부 확인
Query Execution Plan - type
쿼리가 실행될 방법을 표현
type 값을 통해 table이 join되는 방법과 index 활용 여부 판단 가능
- ALL 등 : Full scan으로 쿼리를 수행함
- ref, ref_or_null, eq_ref, range 등 : 쿼리 수행 시 index를 활용함

## 쿼리 성능 측정 결과
```sql
SELECT *
FROM
  concert.concert_timeslot ts
    INNER JOIN
  concert.concert_timeslot_occupancy occ
  ON
    ts.concert_id=7
    AND ts.id=occ.concert_timeslot_id
ORDER BY reservation_start_time;
```
- Query Execution Plan
  - 인덱스 적용 전 `0.0200s (10회 평균)`
  - ![index_before](index_before.png)
  - 인덱스 적용 후 `0.0035s (10회 평균)`
  - ![index_after](index_after.png)

## 기타 알아본 점
### EXPLAIN 과 ANALYZE의 차이
- EXPLAIN은 쿼리를 실행하지 않고, 쿼리 수행 계획을 출력
- ANALYZE는 쿼리를 실제로 실행한 후, 성능과 통계를 측정하여 출력
  - MySQL 10.1.0부터 EXPLAIN ANALYZE -> ANALYZE로 실행방법이 변경됨
- ANALYZE FORMAT=JSON 은 더 많은 데이터 보여줌

### EXPLAIN / ANALYZE column별 의미
- type : row가 table에서 검색되는 방법
- possible_keys : row를 탐색하는데 이용되는 Key
- rows : 각 lookup당 얼마나 많은 row가 탐색될 것인지 추정치
- extra
  - using filesore : 정렬을 위해 디스크 기반 머지 소트를 수행한 결과를 바탕으로 row 반환

### JPA Open in View
- api 요청부터 응답까지 영속성 콘텍스트를 유지하는 설정
- jpa.open-in-view : true
  - transaction이 끝나도 connection을 미반납
  - api 응답이 끝나야 connection을 반납
- jpa.open-in-view : false
  - transaction이 끝나면 DB Cnnection 즉시 반납
  - 트랜잭션 외부에서 lazy 로딩시 문제 발생 가능
