package com.example.benchmark.controller;

import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.query_dsl.MatchAllQuery;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class LogElasticController {
    private final OpenSearchClient openSearchClient;

    @GetMapping("/testing")
    public void test() {
        try {
            // 검색 쿼리 생성: "test" 인덱스에서 모든 문서 검색
            SearchRequest request = new SearchRequest.Builder()
                    .index("test")
                    .query(Query.of(q -> q
                            .matchAll(ma -> ma)))
                    .build();

            // 검색 실행
            SearchResponse<Object> response = openSearchClient.search(request, Object.class);

            // 검색 결과 출력
            System.out.println("검색 결과:");
            for (Hit<Object> hit : response.hits().hits()) {
                System.out.println(hit.source());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
