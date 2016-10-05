package com.yflog.dao.impl;

import com.yflog.dao.BuckFlowDao;
import com.yflog.entity.BuckFlow;
import com.yflog.entity.Bucket;
import com.yflog.entity.Role;
import com.yflog.entity.util.Type;
import com.yflog.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public void update(BuckFlow updated) {

        BuckFlow old = getById(updated.getId());

        if (old == null) {
            throw new IllegalArgumentException("BuckFlow not exist - " + updated.getId());
        }

        // amount changed
        if (old.getAmount() != updated.getAmount()) {
            Set<Integer> involvedBucketIds = new HashSet<>();
            involvedBucketIds.add(updated.getFromBucket().getId());
            involvedBucketIds.add(updated.getToBucket().getId());
            involvedBucketIds.add(old.getFromBucket().getId());
            involvedBucketIds.add(old.getToBucket().getId());

            Map<Integer, Bucket> involvedBuckets = new HashMap<>();
            for (int id : involvedBucketIds) {
                Bucket bucket = bucketService.getById(id);
                if (bucket == null) {
                    throw new IllegalArgumentException("Bucket not exist - " + id);
                }

                involvedBuckets.put(id, bucket);
            }

            // update old buckets
            Bucket oldFrom = involvedBuckets.get(old.getFromBucket().getId());
            Bucket oldTo = involvedBuckets.get(old.getToBucket().getId());
            oldFrom.setBalance(oldFrom.getBalance() + old.getAmount());
            oldTo.setBalance(oldTo.getBalance() -  old.getAmount());

            // update new buckets
            Bucket newFrom = involvedBuckets.get(updated.getFromBucket().getId());
            Bucket newTo = involvedBuckets.get(updated.getToBucket().getId());
            newFrom.setBalance(newFrom.getBalance() - updated.getAmount());
            newTo.setBalance(newTo.getBalance() + updated.getAmount());

            for (Integer idKey : involvedBuckets.keySet()) {
                updateBucketBalance(involvedBuckets.get(idKey));
            }
        }

        getHibernateTemplate().evict(old);
        getHibernateTemplate().update(updated);

    }

    @Transactional
    @Override
    public int bulkUpdate(BuckFlow buckFlow) {
        return getHibernateTemplate().bulkUpdate(
                "update BuckFlow set flowType=?, amount=?, latestUpdateEpoch=?, fromBucket=?, toBucket=?, desc=?, role=?, tag=? where id=?",
                buckFlow.getFlowType(), buckFlow.getAmount(), System.currentTimeMillis(), buckFlow.getFromBucket(), buckFlow.getToBucket(), buckFlow.getDesc(), buckFlow.getRole(), buckFlow.getTag(), buckFlow.getId()
                );
    }

    @Override
    public List<BuckFlow> listAll(Type type) {
        return (List<BuckFlow>) getHibernateTemplate().findByNamedParam("from BuckFlow as bf where bf.flowType=:fType", "fType", type.getType());
    }

    @Override
    public List<BuckFlow> listAll(Type type, long from, long to) {
        return (List<BuckFlow>) getHibernateTemplate().findByNamedParam("from BuckFlow as bf where bf.flowType=:fType and bf.createEpoch in (:fromEpoch, :toEpoch)",
                new String[]{"fType", "fromEpoch", "toEpoch"},
                new Object[]{type.getType(), from, to});
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

    private void updateBucketBalance(Bucket bucket) {
        getHibernateTemplate().bulkUpdate("update Bucket set balance=? where id=?", bucket.getBalance(), bucket.getId());
    }
}
