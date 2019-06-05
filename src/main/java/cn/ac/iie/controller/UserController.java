package cn.ac.iie.controller;

import cn.ac.iie.bean.User;
import cn.ac.iie.service.UserService;
import cn.ac.iie.web.ResponseResult;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/add")
    public Object addUser(User user) {
        IndexResponse indexResponse = userService.addUser("user", user);
        return "CREATED".equals(indexResponse.getResult().toString()) ? new ResponseResult("200", "success") : new ResponseResult("500", "failed");
    }

    @PostMapping("/user/update")
    public Object updateUser(User user) {
        UpdateResponse updateResponse = userService.updateUser("user", user);
        System.out.println(updateResponse.getResult().toString());
        return ("UPDATED".equals(updateResponse.getResult().toString()) || "NOOP".equals(updateResponse.getResult().toString())) ? new ResponseResult("200", "success") : new ResponseResult("500", "failed");
    }

    @DeleteMapping("/user/{userName}")
    public Object deleteUser(@PathVariable("userName") String userName) {
        System.out.println(userName);
        DeleteResponse deleteResponse = userService.deleteByUserName(userName);
        System.out.println(deleteResponse.getResult().toString());
        return "DELETED".equals(deleteResponse.getResult().toString()) ? new ResponseResult("200", "success") : new ResponseResult("500", "failed");
    }

    @GetMapping("/user/{userName}")
    public Object addUser(@PathVariable("userName") String userName) {
        User user = userService.getByUserName(userName);
        return user == null ? new ResponseResult("500", "no such user") : new ResponseResult("200", "success", user);
    }

    @GetMapping("/users")
    public Object list() {
        List<User> userList = userService.getUserList();
        return new ResponseResult("200", "success", userList);
    }

}
