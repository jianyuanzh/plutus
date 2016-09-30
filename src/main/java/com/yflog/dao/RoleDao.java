package com.yflog.dao;

import com.yflog.entity.Role;

/**
 * Created by vincent on 9/30/16.
 */
public abstract class RoleDao extends AbstractHibernateDao<Role> {
    public abstract Role getByName(String name);
}
