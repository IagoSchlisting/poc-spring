package com.sap.dao.impl;

import com.sap.dao.RoleDao;
import com.sap.models.Role;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class RoleDaoImp extends HibernateDaoSupport implements RoleDao {

    @Override
    @Transactional
    public void addRole(Role role) {
        getHibernateTemplate().save(role);
    }

    @Override
    public Role getRoleById(int id) {
        Role role = getHibernateTemplate().get(Role.class, id);
        return role;
    }


}
