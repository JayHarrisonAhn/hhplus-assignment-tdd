# 장애 대응 문서

## 원인 파악
### Intellij Profiling
- Server Application의 어떤 코드에서 지연이 발생하는지 파악 가능
- 아래 결과는 Stress Test 수행시의 Profiling 결과
![](./load_flamegraph_before.png)
- `findConcertSeats` 함수에 많은 부하가 많이 걸린 것을 확인

## 대응
### 장애 포인트
- 부하테스트에서 실질적인 장애는 발생하지 않았지만, 가상 시나리오로 장애상황을 가정
- `findConcertSeats` 함수의 부하로 인해 장애가 발생했다고 가정

### 장애 분석
위의 Intellij Profiling을 통해 문제 코드를 파악

### 장애 조치
- 장애 지점인 `findConcertSeats`에 캐싱 적용
```java
@Cacheable(value = "concert:timeslot:seat", key = "#timeslotId", cacheManager = "redisCacheManager")
public List<ConcertSeat> findConcertSeats(Long timeslotId) { ... }
```

### 조치 결과
![](./load_flamegraph_after.png)
- 장애 지점에 걸렸던 부하가 5000ms대 -> 319ms로 90% 이상 감소

### 조치 후 성능 테스트
![](./load_flamegraph_after_k6.png)
- vuser 800부터 connection timeout 에러가 발생
- Step 19에서 테스트했던 vuser 600보다 **_30%_** 성능 향상 

# 기타 알아본 자료들
### 참고자료
- [배민 장애대응](https://techblog.woowahan.com/4886/)

### 장애 전파 기능
- APM을 이용 가능
- Scouter, Pinpoint, Prometheus&Grafana 등
- Prometheus
  - Alert 기능 제공
  - ```yaml
    groups:
    - name: example
      labels:
      team: myteam
      rules:
        - alert: HighRequestLatency
          expr: job:request_latency_seconds:mean5m{job="myjob"} > 0.5
          for: 10m
          keep_firing_for: 5m
          labels:
          severity: page
          annotations:
          summary: High request latency
    ```
  - ```yaml
    receivers:
    - name: 'email-notifications'
      email_configs:
      - to: 'your-email@example.com'
        from: 'alertmanager@example.com'
        smarthost: 'smtp.gmail.com:587'
        auth_username: 'your-gmail-username@gmail.com'
        auth_identity: 'your-gmail-username@gmail.com'
        auth_password: 'your-gmail-password'
        ```
