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
    - PK Select
    - Index 가 없는 Column Select
    - Index 가 있는 Column Select
    - Paging
    - 시간을 기준으로 Range 탐색