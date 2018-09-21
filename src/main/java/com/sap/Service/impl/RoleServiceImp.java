package com.sap.Service.impl;
import com.sap.Dao.RoleDao;
import com.sap.Service.RoleService;
import com.sap.models.Role;
import javax.annotation.Resource;
import java.util.List;

public class RoleServiceImp implements RoleService {

    @Resource
    private RoleDao roleDao;

    @Override
    public void addRole(Role role) {
        this.roleDao.addRole(role);
    }

    @Override
    public List<Role> listRoles() {
        return this.roleDao.listRoles();
    }

    @Override
    public Role getRoleById(int id) { return this.roleDao.getRoleById(id); }

}
