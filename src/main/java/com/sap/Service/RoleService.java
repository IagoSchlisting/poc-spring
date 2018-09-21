package com.sap.Service;

import com.sap.models.Role;

import java.util.List;

public interface RoleService {
    public void addRole(Role role);
    public List<Role> listRoles();
    public Role getRoleById(int id);
}
