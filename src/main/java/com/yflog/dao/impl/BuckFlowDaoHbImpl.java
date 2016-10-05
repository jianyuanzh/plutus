package com.yflog.dao.impl;

import com.yflog.dao.BuckFlowDao;
import com.yflog.entity.BuckFlow;
import com.yflog.entity.Bucket;
import com.yflog.entity.Role;
import com.yflog.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by vincent on 9/30/16.
 */
@Repository("buckFlowDao")
public class BuckFlowDaoHbImpl extends BuckFlowDao {

    @Autowired
    private BucketService bucketService;

    @Override
    public List<BuckFlow> loadAll() {
        return getHibernateTemplate().loadAll(BuckFlow.class);
    }

    public List<BuckFlow> loadAll(long from, long to) {
        return (List<BuckFlow>) getHibernateTemplate().findByNamedParam(
                "from BuckFlow as bf where bf.createEpoch in (:fromEpoch, :toEpoch)",
                new String[]{"fromEpoch", "toEpoch"},
                new Long[]{from, to});
    }

    @Transactional
    @Override
    public void save(BuckFlow o) {
        Bucket fromBucket = bucketService.getById(o.getFromBucket().getId());
        Bucket toBucket = bucketService.getById(o.getToBucket().getId());
        fromBucket.setBalance(fromBucket.getBalance() - o.getAmount());
        toBucket.setBalance(toBucket.getBalance() + o.getAmount());

        bucketService.updateBalance(fromBucket);
        bucketService.updateBalance(toBucket);

        getHibernateTemplate().save(o);
    }

    @Transactional
    @Override
    public void update(BuckFlow o) {
        BuckFlow curr = getById(o.getId());
        getHibernateTemplate().update(o);
    }

    @Transactional
    @Override
    public int bulkUpdate(BuckFlow buckFlow) {
        return getHibernateTemplate().bulkUpdate(
                "update BuckFlow set flowType=?, amount=?, latestUpdateEpoch=?, fromBucket=?, toBucket=?, desc=?, role=?, tag=? where id=?",
                buckFlow.getFlowType(), buckFlow.getAmount(), System.currentTimeMillis(), buckFlow.getFromBucket(), buckFlow.getToBucket(), buckFlow.getDesc(), buckFlow.getRole(), buckFlow.getTag(), buckFlow.getId()
                );
    }

    @Transactional
    @Override
    public void delete(BuckFlow o) {
        getHibernateTemplate().delete(o);
    }

    @Override
    public BuckFlow getById(int id) {
        return getHibernateTemplate().get(BuckFlow.class, id);
    }

    @Override
    public List<BuckFlow> getFlowsByRole(Role role) {
        return (List<BuckFlow>) getHibernateTemplate().findByNamedParam("from BuckFlow as bf where bf.role=:role", "role", role);
    }

    @Override
    public List<BuckFlow> getFlowsByToBucket(Bucket bucket) {
        return (List<BuckFlow>) getHibernateTemplate().findByNamedParam("from BuckFlow as bf where bf.toBucket=:to", "to", bucket);
    }
}
