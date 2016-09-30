package com.yflog.service;

import com.yflog.dao.BuckFlowDao;
import com.yflog.entity.BuckFlow;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by vincent on 9/30/16.
 */
public class BuckFlowService {
    @Autowired
    private BuckFlowDao buckFlowDao;

    public void save(BuckFlow buckFlow) {
        buckFlowDao.save(buckFlow);
    }

    public void delete(BuckFlow buckFlow) {
        buckFlowDao.delete(buckFlow);
    }

    public void update(BuckFlow buckFlow) {
        buckFlowDao.update(buckFlow);
    }

    public BuckFlow getById(int id) {
        return buckFlowDao.getById(id);
    }
}
