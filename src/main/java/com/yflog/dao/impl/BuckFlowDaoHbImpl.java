package com.yflog.dao.impl;

import com.yflog.dao.BuckFlowDao;
import com.yflog.entity.BuckFlow;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by vincent on 9/30/16.
 */
@Repository("buckFlowDao")
public class BuckFlowDaoHbImpl extends BuckFlowDao {

    @Transactional
    @Override
    public void save(BuckFlow o) {
        getHibernateTemplate().save(o);
    }

    @Override
    public void update(BuckFlow o) {
        getHibernateTemplate().update(o);
    }

    @Override
    public void delete(BuckFlow o) {
        getHibernateTemplate().delete(o);
    }

    @Override
    public BuckFlow getById(int id) {
        return getHibernateTemplate().get(BuckFlow.class, id);
    }
}
