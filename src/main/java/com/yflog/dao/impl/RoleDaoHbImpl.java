package com.yflog.dao.impl;

import com.yflog.dao.RoleDao;
import com.yflog.entity.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by vincent on 9/30/16.
 */
@Repository("roleDao")
public class RoleDaoHbImpl extends RoleDao {
    @Override
    public Role getByName(String name) {
        List<Role> roles = (List<Role>) getHibernateTemplate().findByNamedParam("from Role as r where r.roleName=:name", "name", name);
        if (roles == null || roles.isEmpty()) {
            return null;
        }

        return roles.get(0);
    }

    @Override
    public List<Role> loadAll() {
        return getHibernateTemplate().loadAll(Role.class);
    }

    @Transactional
    @Override
    public void save(Role o) {
        getHibernateTemplate().save(o);
    }

    @Transactional
    @Override
    public void update(Role o) {
        getHibernateTemplate().update(o);
    }

    @Transactional
    @Override
    public void delete(Role o) {
        getHibernateTemplate().delete(o);
    }

    @Override
    public Role getById(int id) {
        return getHibernateTemplate().get(Role.class, id);
    }
}
