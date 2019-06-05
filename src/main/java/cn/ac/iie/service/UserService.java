package cn.ac.iie.service;


import cn.ac.iie.bean.User;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;

import java.util.List;

public interface UserService {

    /**
     * 往ES中添加数据
     * @param indexName
     * @param data
     * @return
     */
    IndexResponse addUser(String indexName, User data);

    /**
     * 往ES中修改数据
     * @param indexName
     * @param user
     * @return
     */
    UpdateResponse updateUser(String indexName, User user);

    /**
     * 删除数据
     * @param userName
     * @return
     */
    DeleteResponse deleteByUserName(String userName);

    /**
     * 根据用户名得到该用户
     * @param userName
     * @return
     */
    User getByUserName(String userName);

    /**
     * 得到用户列表
     * @return
     */
    List<User> getUserList();

}
