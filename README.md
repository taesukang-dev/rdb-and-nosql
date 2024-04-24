# RDB (MySQL), NoSQL (MongoDB) performance test (mainly select)
- 실행 환경 spec
  - 11th Gen Intel(R) Core(TM) i7-1165G7 @ 2.80GHz   2.80 GHz
  - RAM : 16GB
- 데이터 천만개 기준 (local 에서 돌리므로 아주 큰 데이터는 셋팅하지 않았음)
  - mongo
    - 단순 Select
      - 10,000,000 건 전체 fetch, 140s, http response 154s
    - PK select
      - id {121540 번째 데이터 : 0.19s, 6712222 번째 데이터 : 0.008s, 9992222 번째 데이터 : 0.0047s}
    - Index 가 없는 Column Select
      - content {670000 번째 데이터 : 4.6s, 5970000 번째 데이터 : 4.3s, 9715400 번째 데이터 : 4.6s}
    - Index 가 있는 Column Select
      - title {670000 번째 데이터 : 0.005s, 5970000 번째 데이터 : 0.005s, 9715400 번째 데이터 : 0.006s}
    - paging
      - 974774 번째부터 10개, 0.25s
      - 7967062 번째부터 10개, 2.05s
      - `{ "createdAt" : { "$gte" : { "$date" : "2024-04-23T04:15:34.23Z"}, "$lte" : { "$date" : "2024-04-23T04:15:35Z"}}}`
        - 0 번째부터 10개 = 0.005s
        - 40000 번째부터 10개 = 0.07s
    - 시간을 기준으로 Range 탐색
      - createdAt 은 index 설정
        ```json
            // data 54082건, data fetch 1.8s, http repsonse 2.01s
            {
              createdAt: {
                $gte : new ISODate('2024-04-23T04:15:00.000+00:00'), 
                $lte : new ISODate('2024-04-23T04:15:35.000+00:00')
              }
            }
        ```
  - mysql
    - 단순 Select
      - 10,000,000 건 전체 측정 불가, AsyncRequestTimeoutException
    - PK Select
      - id {121540 번째 데이터 : 0.01s, 6712222 번째 데이터 : 0.01s , 9992222 번째 데이터 : 0.01s}
    - Index 가 없는 Column Select
      - content {670000 번째 데이터 : 26.4s, 5970000 번째 데이터 : 23.41s, 9715400 번째 데이터 : 20.7s}
    - Index 가 있는 Column Select
      - title {670000 번째 데이터 : 0.005s, 5970000 번째 데이터 : 0.01s, 9715400 번째 데이터 : 0.6s}
    - Paging
      - 974774 번째부터 10개, 0.44s
      - 7967062 번째부터 10개, 3.5s
      - `created_at between :start and :end limit :page, :size`
        - 0 번째부터 10개 = 0.03s
        - 40000 번째부터 10개 = 0.8s
    - 시간을 기준으로 Range 탐색
      - createdAt 은 index 설정
      ```sql
        -- 52028 건, data fetch 2.03, http response 2.52
        select * from test_log where created_at between :start and :end
      ```

## 참고
- 위 테스트는 caching 상태, buffer pool 상태 등에 대한 운영 환경이 상이할 경우 결과는 달라질 수 있음
- 비교
  - 수식 : 1 - (빠른 / 느린) * 100
  - 단순 select
    - 테스트에서 mysql 은 측정 자체가 불가함, r2dbc 가 내부적으로도 정말 reactive 한지 확인 필요
    - 예상하건데, mysql 서버가 reactive 하게 response 하지 못하므로 위와 같은 결과가 발생하지 않았나 판단
      - 좀 더 자세한 search 필요
  - pk select
    - mongo 평균 : 0.067, mysql 평균 : 0.01 -> mysql 85% 우세
  - index 가 없는 column select
    - mongo 평균 : 4.5, mysql 평균 : 7.8 -> mongo 42% 우세
  - index 가 있는 column select
    - mongo 평균 : 0.0053, mysql 평균 : 0.205 -> mongo 97% 우세
  - paging
    - mongo 평균 : 0.59, mysql 평균 : 1.1925 -> mongo 50.52 % 우세
  - 시간을 기준으로 range 탐색
    - mongo : 1.8, mysql : 2.03 -> mongo 11% 우세
- 대체로 mongo 우세
  - index 를 b tree 로 사용하는 것은 동일하나, bson 으로 저장하면서 io 시 cost 가 낮은 게 이점이지 않나 생각
    - page 로 저장할 때 record byte 계산, tablespace 구조 맞추는 등의 작업이 필요하지만 bson 은 걍 때려 넣을듯...
  - ~memetable, lsm tree 를 쓰면서 loose index scan 처럼 기본적으로 동작하고 추가로 bloom filter 가 데이터 존재 여부를 거르는 것도 성능에 한 몫 하는 듯~
    - ~또, 대부분의 동작을 비동기적으로 처리하기도 하고(memory to sstable, sstable to disk), hot data 를 memory 에 올려놓고 찾기 쉬운 구조니까 memory 관리에 유의 해야 할 듯~
      - MongoDB 는 caching 될 때 메모리를 적게 사용하기 위한 구조로 변환해서 적재, WiredTiger storage engine 및 block manager
    - 근데 MySQL 도 비슷하지 않나 싶긴 한데... (change buffer, redo log...)
  - pk select 할 때에는 mysql 이 더 우세했는데, clustering key 저장 / 동작 되는게 이점을 가지는 듯
  - mongoDB 를 sharding 한다면 성능 향상 여지가 있음
  - 데이터가 많아지면 많아질 수록 차이는 훨씬 벌어짐
    - <a href="https://www.youtube.com/watch?v=3axR2Onz1nU">관련 유튜브 링크</a>
