package com.sap.dao;

import com.sap.models.Role;

public interface RoleDao {
    public void addRole(Role r);
    public Role getRoleById(int id);

}
