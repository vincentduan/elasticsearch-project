package cn.ac.iie.service.impl;

import cn.ac.iie.bean.User;
import cn.ac.iie.exception.MyException;
import cn.ac.iie.service.UserService;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private Client client;

    private static String INDEX_NAME = "user";

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public IndexResponse addUser(User user) {
//        IndexResponse indexResponse = client.prepareIndex("user", "type").setSource(jsonObject).get();
        IndexResponse indexResponse = null;
        try {
            indexResponse = client.prepareIndex(INDEX_NAME, "type").setSource(jsonBuilder().startObject().field("userName", user.getUserName()).field("passwordEn", user.getPasswordEn()).endObject()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexResponse;
    }

    @Override
    public UpdateResponse updateUser(User user) {
        SearchResponse searchResponse = client.prepareSearch(INDEX_NAME).setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userName", user.getUserName()))).setSize(1).get();
        String docId = searchResponse.getHits().getHits()[0].getId();
        UpdateResponse updateResponse = null;
        try {
            updateResponse = client.prepareUpdate(INDEX_NAME, "type", docId + "").setDoc(jsonBuilder().startObject().field("userName", user.getUserName()).field("passwordEn", user.getPasswordEn()).endObject()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updateResponse;
    }

    @Override
    public DeleteResponse deleteByUserName(String userName) {
        SearchResponse searchResponse = client.prepareSearch(INDEX_NAME).setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userName", userName))).setSize(1).get();
        if (searchResponse.getHits().getHits().length > 0) {
            String docId = searchResponse.getHits().getHits()[0].getId();
            DeleteResponse response = client.prepareDelete(INDEX_NAME, "type", docId).get();
            return response;
        } else {
            throw new MyException(" The user " + userName + " don't exists");
        }
    }

    @Override
    public User getByUserName(String userName) {
        SearchResponse searchResponse = client.prepareSearch(INDEX_NAME).setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userName", userName))).setSize(1).get();
        SearchHit[] hits = searchResponse.getHits().getHits();
        if (hits.length == 0) {
            throw new MyException(" The user " + userName + " don't exists");
        } else {
            String passwordEn = hits[0].getSourceAsMap().get("passwordEn").toString();
            return new User(userName, passwordEn);
        }
    }

    @Override
    public List<String> getUserList() {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEX_NAME).setQuery(QueryBuilders.boolQuery()).setSize(0);
        int totalHits = Integer.parseInt(searchRequestBuilder.get().getHits().totalHits + "");
        SearchResponse searchResponse = searchRequestBuilder.setSize(totalHits).get();
        SearchHit[] hits = searchResponse.getHits().getHits();
        List<String> userList = new LinkedList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String userName = sourceAsMap.get("userName").toString();
            userList.add(userName);
        }
        return userList;
    }

    @Override
    public boolean existUser(String userName) {
        SearchResponse searchResponse = client.prepareSearch(INDEX_NAME).setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userName", userName))).get();
        return searchResponse.getHits().totalHits > 0 ? true : false;
    }


}
