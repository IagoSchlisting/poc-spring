package com.sap.Dao;

import com.sap.models.Role;

public interface RoleDao {
    public void addRole(Role r);
    public Role getRoleById(int id);

}
