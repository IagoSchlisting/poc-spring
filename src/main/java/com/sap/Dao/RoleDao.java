package com.sap.Dao;

import com.sap.models.Role;

import java.util.List;

public interface RoleDao {
    public void addRole(Role r);
    public List<Role> listRoles();
    public Role getRoleById(int id);

}
