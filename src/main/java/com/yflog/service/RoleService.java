package com.yflog.service;

import com.yflog.dao.RoleDao;
import com.yflog.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by vincent on 9/30/16.
 */
@Service("roleService")
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    public void save(Role role) {
        roleDao.save(role);
    }

    public void update(Role role) {
        roleDao.update(role);
    }

    public void delete(Role role) {
        roleDao.delete(role);
    }

    public Role getRole(int id) {
        return roleDao.getById(id);
    }

    public Role getRoleByName(String name) {
        return roleDao.getByName(name);
    }
}
