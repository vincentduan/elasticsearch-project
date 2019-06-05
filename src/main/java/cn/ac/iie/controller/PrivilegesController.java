package cn.ac.iie.controller;

import cn.ac.iie.bean.Privileges;
import cn.ac.iie.bean.User;
import cn.ac.iie.service.PrivilegesService;
import cn.ac.iie.web.ResponseResult;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PrivilegesController {

    @Autowired
    private PrivilegesService privilegesService;

    @PostMapping("/privileges/add")
    public Object addPrivileges(@RequestBody Privileges privileges) {
        IndexResponse indexResponse = privilegesService.addPrivileges(privileges);
        return "CREATED".equals(indexResponse.getResult().toString()) ? new ResponseResult("200", "success") : new ResponseResult("500", "failed");
    }

    @PostMapping("/privileges/update")
    public Object updatePrivileges(@RequestBody Privileges privileges) {
        UpdateResponse updateResponse = privilegesService.updatePrivileges(privileges);
        return ("UPDATED".equals(updateResponse.getResult().toString()) || "NOOP".equals(updateResponse.getResult().toString())) ? new ResponseResult("200", "success") : new ResponseResult("500", "failed");
    }

    @DeleteMapping("/privileges/{userName}")
    public Object deletePrivileges(@PathVariable("userName") String userName) {
        DeleteResponse deleteResponse = privilegesService.deleteByUserName(userName);
        System.out.println(deleteResponse.getResult().toString());
        return "DELETED".equals(deleteResponse.getResult().toString()) ? new ResponseResult("200", "success") : new ResponseResult("500", "failed");
    }

    @GetMapping("/privileges/{userName}")
    public Object getUser(@PathVariable("userName") String userName) {
        Privileges privileges = privilegesService.getByUserName(userName);
        return privileges == null ? new ResponseResult("500", "no such user") : new ResponseResult("200", "success", privileges);
    }

}
