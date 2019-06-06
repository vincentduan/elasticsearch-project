package cn.ac.iie.service;


import cn.ac.iie.bean.User;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;

import java.util.List;

public interface UserService {

    /**
     * 往ES中添加数据
     * @param user
     * @return
     */
    IndexResponse addUser(User user);

    /**
     * 往ES中修改数据
     * @param user
     * @return
     */
    UpdateResponse updateUser(User user);

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
    List<String> getUserList();

    /**
     * 判断用户是否存在
     * @param userName
     * @return
     */
    boolean existUser(String userName);
}
