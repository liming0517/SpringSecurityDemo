package com.wiserun.myvhr.mapper;

import com.wiserun.myvhr.bean.Hr;
import com.wiserun.myvhr.bean.Role;

import java.util.List;

public interface HrMapper {
    /*查询某个hr信息*/
    Hr findByName(String username);

    List<Role> getRolesByHrId(Long id);
}
