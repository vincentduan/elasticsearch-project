package cn.ac.iie.service.impl;

import cn.ac.iie.bean.Privileges;
import cn.ac.iie.service.PrivilegesService;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class PrivilegesServiceImpl implements PrivilegesService {

    @Autowired
    private Client client;

    private static String INDEX_NAME = "privileges";

    @Override
    public IndexResponse addPrivileges(Privileges privileges) {
        IndexResponse indexResponse = null;
        try {
            indexResponse = client.prepareIndex(INDEX_NAME, "type").setSource(jsonBuilder().startObject().field("userName", privileges.getUserName()).field("authorityType", privileges.getAuthorityType()).field("authorityApps", privileges.getAuthorityApps()).endObject()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexResponse;
    }

    @Override
    public UpdateResponse updatePrivileges(Privileges privileges) {
        SearchResponse searchResponse = client.prepareSearch(INDEX_NAME).setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userName", privileges.getUserName()))).setSize(1).get();
        String docId = searchResponse.getHits().getHits()[0].getId();
        UpdateResponse updateResponse = null;
        try {
            updateResponse = client.prepareUpdate(INDEX_NAME, "type", docId + "").setDoc(jsonBuilder().startObject().field("authorityType", privileges.getAuthorityType()).field("authorityApps", privileges.getAuthorityApps()).endObject()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updateResponse;
    }

    @Override
    public DeleteResponse deleteByUserName(String userName) {
        SearchResponse searchResponse = client.prepareSearch(INDEX_NAME).setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userName", userName))).setSize(1).get();
        String docId = searchResponse.getHits().getHits()[0].getId();
        DeleteResponse response = client.prepareDelete(INDEX_NAME, "type", docId).get();
        return response;
    }

    @Override
    public Privileges getByUserName(String userName) {
        SearchResponse searchResponse = client.prepareSearch(INDEX_NAME).setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userName", userName))).setSize(1).get();
        SearchHit[] hits = searchResponse.getHits().getHits();
        if (hits.length == 0) {
            return null;
        } else {
            String authorityType = hits[0].getSourceAsMap().get("authorityType").toString();
            List<String> authorityApps = (List<String>) hits[0].getSourceAsMap().get("authorityApps");
            return new Privileges(userName, authorityType, authorityApps);
        }
    }
}
