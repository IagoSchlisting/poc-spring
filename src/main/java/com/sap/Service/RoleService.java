package com.sap.Service;

import com.sap.models.Role;

import java.util.List;

public interface RoleService {
    public void addRole(Role role);
    public Role getRoleById(int id);
}
