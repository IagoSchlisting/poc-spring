package com.sap.Dao.impl;

import com.sap.Dao.RoleDao;
import com.sap.models.Role;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public class RoleDaoImp extends HibernateDaoSupport implements RoleDao {

    @Override
    @Transactional
    public void addRole(Role role) {
        getHibernateTemplate().save(role);
    }
    @Override
    public List<Role> listRoles() {
        return (List<Role>) getHibernateTemplate().find("from com.sap.models.Role");
    }

    @Override
    public Role getRoleById(int id) {
        Role role = getHibernateTemplate().get(Role.class, id);
        return role;
    }


}
