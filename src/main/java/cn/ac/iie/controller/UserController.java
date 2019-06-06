package cn.ac.iie.controller;

import cn.ac.iie.bean.User;
import cn.ac.iie.service.PrivilegesService;
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

    @Autowired
    private PrivilegesService privilegesService;

    private static final String URI = "/usr";

    @PostMapping("/users")
    public Object addUser(User user) {
        System.out.println(user);
        IndexResponse indexResponse = userService.addUser(user);
        return "CREATED".equals(indexResponse.getResult().toString()) ? new ResponseResult("200", "success") : new ResponseResult("500", "failed");
    }

    @PutMapping("/users")
    public Object updateUser(User user) {
        UpdateResponse updateResponse = userService.updateUser(user);
        System.out.println(updateResponse.getResult().toString());
        return ("UPDATED".equals(updateResponse.getResult().toString()) || "NOOP".equals(updateResponse.getResult().toString())) ? new ResponseResult("200", "success") : new ResponseResult("500", "failed");
    }

    @DeleteMapping("/user/{userName}")
    public Object deleteUser(@PathVariable("userName") String userName) {
        DeleteResponse user_deleteResponse = userService.deleteByUserName(userName);
        // 删除权限
        DeleteResponse pri_deleteResponse1 = privilegesService.deleteByUserName(userName);
        String result = (user_deleteResponse.getResult().toString()).equals(pri_deleteResponse1.getResult().toString()) && "DELETED" .equals(user_deleteResponse.getResult().toString()) ? "success" : "failed";
        return result;
    }

    @GetMapping("/user/{userName}")
    public Object getUser(@PathVariable("userName") String userName) {
        User user = userService.getByUserName(userName);
        return user == null ? new ResponseResult("200", "no such user") : new ResponseResult("200", "success", user);
    }

    @GetMapping("/exist/{userName}")
    public Object existUser(@PathVariable("userName") String userName) {
        boolean result = userService.existUser(userName);
        return new ResponseResult("200", "success", result);
    }

    @GetMapping("/users")
    public Object list() {
        List<String> userList = userService.getUserList();
        return new ResponseResult("200", "success", userList);
    }

}
