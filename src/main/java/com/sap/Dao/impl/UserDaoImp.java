package com.sap.Dao.impl;

import com.sap.Dao.UserDao;
import com.sap.models.User;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public class UserDaoImp extends HibernateDaoSupport implements UserDao {

    @Override
    @Transactional
    public void addUser(User user) {
        getHibernateTemplate().save(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        getHibernateTemplate().update(user);
    }

    @Override
    public List<User> listUsers() {
        return (List<User>) getHibernateTemplate().find("from com.sap.models.User");
    }

    @Override
    public User getUserById(int id) {
        User user = getHibernateTemplate().get(User.class, id);
        return user;
    }

    @Override
    @Transactional
    public void removeUser(int id) {
        getHibernateTemplate().delete(this.getUserById(id));
    }

}
