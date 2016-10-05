package com.yflog.dao;

import com.yflog.entity.BuckFlow;
import com.yflog.entity.Bucket;
import com.yflog.entity.Role;

import java.util.List;

/**
 * Created by vincent on 9/30/16.
 */
public abstract class BuckFlowDao extends AbstractHibernateDao<BuckFlow> {

    public abstract List<BuckFlow> getFlowsByRole(Role role);

    public abstract List<BuckFlow> getFlowsByToBucket(Bucket toBucket);

    public abstract List<BuckFlow> loadAll(long from, long to);

    public abstract int bulkUpdate(BuckFlow buckFlow);
}
