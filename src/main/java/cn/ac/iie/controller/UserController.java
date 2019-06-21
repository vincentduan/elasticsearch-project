package cn.ac.iie.controller;

import cn.ac.iie.authorization.annotation.Authorization;
import cn.ac.iie.bean.User;
import cn.ac.iie.service.PrivilegesService;
import cn.ac.iie.service.UserService;
import cn.ac.iie.utils.ResponseVOUtil;
import cn.ac.iie.vo.ResponseResult;
import org.apache.tomcat.util.http.ResponseUtil;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("userManagement")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PrivilegesService privilegesService;

    private static final String URI = "/user";

    @PostMapping("/users")
    @Authorization
    public Object addUser(User user) {
        System.out.println(user);
        IndexResponse indexResponse = userService.addUser(user);
        return "CREATED".equals(indexResponse.getResult().toString()) ? new ResponseResult(200, "success") : new ResponseResult(500, "failed");
    }

    @PutMapping("/users")
    @Authorization
    public Object updateUser(User user) {
        UpdateResponse updateResponse = userService.updateUser(user);
        System.out.println(updateResponse.getResult().toString());
        return ("UPDATED".equals(updateResponse.getResult().toString()) || "NOOP".equals(updateResponse.getResult().toString())) ? new ResponseResult(200, "success") : new ResponseResult(500, "failed");
    }

    @DeleteMapping("/user/{userName}")
    @Authorization
    public Object deleteUser(@PathVariable("userName") String userName) {
        // 删除用户
        DeleteResponse user_deleteResponse = userService.deleteByUserName(userName);
        // 删除权限
        DeleteResponse pri_deleteResponse1 = privilegesService.deleteByUserName(userName);
        String result = (user_deleteResponse.getResult().toString()).equals(pri_deleteResponse1.getResult().toString()) && "DELETED".equals(user_deleteResponse.getResult().toString()) ? "success" : "failed";
        return result;
    }

    @GetMapping("/user/{userName}")
    @Authorization
    public Object getUser(@PathVariable("userName") String userName) {
        User user = userService.getByUserName(userName);
        return user == null ? ResponseVOUtil.success("no such user") : ResponseVOUtil.success(user);
    }

    @GetMapping("/exist/{userName}")
    @Authorization
    public Object existUser(@PathVariable("userName") String userName) {
        boolean result = userService.existUser(userName);
        return ResponseVOUtil.success(result);
    }

    @GetMapping("/users")
    @Authorization
    public Object list() {
        List<String> userList = userService.getUserList();
        return ResponseVOUtil.success(userList);
    }

}
