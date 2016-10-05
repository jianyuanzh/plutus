package com.yflog.service;

import com.yflog.dao.BuckFlowDao;
import com.yflog.entity.BuckFlow;
import com.yflog.entity.Role;
import com.yflog.entity.util.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vincent on 9/30/16.
 */
@Service("buckFlowService")
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

    public List<BuckFlow> getFlowsByRole(Role role) {
        return buckFlowDao.getFlowsByRole(role);
    }

    public List<BuckFlow> loadAllFlows() {
        return buckFlowDao.loadAll();
    }

    public List<BuckFlow> listAllFlows(Type type) {
        return buckFlowDao.listAll(type);
    }

    public List<BuckFlow> loadAllFlows(long from, long to) {
        return buckFlowDao.loadAll(from, to);
    }

    public List<BuckFlow> listAllFlows(Type type, long from, long to) {
        return buckFlowDao.listAll(type, from, to);
    }
}
