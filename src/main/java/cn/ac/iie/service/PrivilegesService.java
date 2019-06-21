package cn.ac.iie.service;

import cn.ac.iie.bean.Privileges;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;

public interface PrivilegesService {

    IndexResponse addPrivileges(Privileges privileges);

    UpdateResponse updatePrivileges(Privileges privileges);

    DeleteResponse deleteByUserName(String userName);

    Privileges getByUserName(String userName);

    boolean checkUser(String userName, String uri);
}
