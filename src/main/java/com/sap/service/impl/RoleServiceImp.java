package com.sap.service.impl;
import com.sap.dao.RoleDao;
import com.sap.service.RoleService;
import com.sap.models.Role;
import javax.annotation.Resource;

public class RoleServiceImp implements RoleService {

    @Resource
    private RoleDao roleDao;

    @Override
    public void addRole(Role role) {
        this.roleDao.addRole(role);
    }

    @Override
    public Role getRoleById(int id) { return this.roleDao.getRoleById(id); }

}
