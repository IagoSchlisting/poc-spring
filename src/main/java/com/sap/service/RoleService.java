package com.sap.service;

import com.sap.models.Role;

public interface RoleService {
    public void addRole(Role role);
    public Role getRoleById(int id);
}
