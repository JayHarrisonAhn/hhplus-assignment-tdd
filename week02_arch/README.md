# ERD
![erd](erd.png)

_그림에는 foreign key가 포함되지 않음_

## 테이블
### 코스 정보 테이블 (course_infojpa)
- 코스와 관련된 기본 정보를 저장하기 위한 테이블
- 과제 구현을 위해 강사 정보를 string으로 포함했으나, 추후 별도 테이블로 분리하는 것이 좋을 듯

### 코스 등록 인원 테이블 (course_registration_countjpa)
- 각 코스에 등록된 학생 수를 관리하는 테이블
- 해당 코스에 등록된 현재 인원(count)과 최대 수용 가능 인원(max)을 저장
- 현재 인원이 수용 가능 인원을 초과하지 않게끔 비즈니스 로직 설계

### 코스 등록 테이블 (course_registrationjpa)
- 학생들의 코스 등록 정보를 관리하는 테이블
- 등록된 코스 ID (course_id), 등록 시간(created_at), 학생 ID (student_id)를 통해 누가 어떤 것을 신청했는지 저장

### DDL
```sql
CREATE TABLE course_infojpa (
    id BIGINT NOT NULL PRIMARY KEY,
    date DATE,
    coach_description VARCHAR(255),
    name VARCHAR(255)
);

CREATE TABLE course_registration_countjpa (
    course_id BIGINT NOT NULL PRIMARY KEY,
    count INTEGER,
    max INTEGER,
);

CREATE TABLE course_registrationjpa (
    id BIGINT NOT NULL PRIMARY KEY,
    course_id BIGINT,
    student_id BIGINT,
    created_at TIMESTAMP(6),
    CONSTRAINT OneStudentPerCourse UNIQUE (course_id, student_id)
);

alter table course_registration_countjpa
    add foreign key (course_id)
    references course_infojpa

alter table course_registrationjpa
    add foreign key (course_id)
        references course_infojpa
```

## JPA 관련
아직은 JPA에 익숙하지 않아, Foreign Key와 관련된 설정을 하지 못했다.
향후 이 부분을 보완하면 좋을 것 같음
