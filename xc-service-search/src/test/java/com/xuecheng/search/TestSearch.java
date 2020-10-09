package com.xuecheng.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSearch {

    @Autowired
    RestHighLevelClient client;

    @Autowired
    RestClient restClient;

    //搜索全部
    @Test
    public void testSearchAll() throws IOException {

        SearchRequest searchResult = new SearchRequest("xc_course");
        //指定类型
        searchResult.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //搜索方式
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //设置源字段过滤  第一个参数结果集包括那些字段，第二个结果集不包括那些字段
        searchSourceBuilder.fetchSource(new String[]{},new String[]{});
        //向搜索请求对象中配置搜索源
        searchResult.source(searchSourceBuilder);
        //执行搜索
         SearchResponse searchResponse = client.search(searchResult);
        SearchHits searchHits  = searchResponse.getHits();
        //匹配到的总记录数
        long totalHits = searchHits.getTotalHits();
        SearchHit[] searchHits1 = searchHits.getHits();
        for (SearchHit hit:searchHits1){
            //文档主键
            String id = hit.getId();
            //源文档内容
            Map<String,Object> map = hit.getSourceAsMap();
            String name =(String) map.get("name");
            String studymodel = (String) map.get("studymodel");
            String description = (String) map.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);

        }
    }

}
