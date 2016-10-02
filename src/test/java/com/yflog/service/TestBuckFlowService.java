package com.yflog.service;

import com.yflog.entity.BuckFlow;
import com.yflog.entity.Bucket;
import com.yflog.entity.Role;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by vincent on 9/30/16.
 */
public class TestBuckFlowService {

    ClassPathXmlApplicationContext context = null;

    BuckFlowService bfService = null;

    @Before
    public void before() {
        context = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
        context.start();

        bfService = (BuckFlowService) context.getBean("buckFlowService");
    }

    @After
    public void after() {
        if (context != null) {
            context.stop();
        }
    }

    @Test
    public void testCRUD() {

        Bucket myBucket = saveBucket(100, 1);
        Bucket expenseBucket = saveExpenseBucket();

        Role role = saveRole();


        BuckFlow buckFlow = saveBuckFlow(myBucket, expenseBucket, role);

        // assert after save, the id is set by db
        Assert.assertTrue(buckFlow.getId() != 0);

        // query
        BuckFlow queried = bfService.getById(buckFlow.getId());

        // assert equals
        Assert.assertEquals(queried, buckFlow);

        // get by Role
        List<BuckFlow> flows = bfService.getFlowsByRole(role);

        Assert.assertTrue(flows.contains(buckFlow));


        // save another buckflow

        BuckFlow anotherBuckFlow = saveBuckFlow(myBucket, expenseBucket, role);

        List<BuckFlow> all = bfService.loadAllFlows();
        Assert.assertTrue(all.contains(buckFlow));
        Assert.assertTrue(all.contains(anotherBuckFlow));

        List<BuckFlow> ranged = bfService.loadAllFlows(buckFlow.getCreateEpoch(), anotherBuckFlow.getCreateEpoch());
        Assert.assertTrue(ranged.contains(buckFlow));
        Assert.assertTrue(ranged.contains(anotherBuckFlow));


    }

    private BuckFlow saveBuckFlow(Bucket from, Bucket to, Role role) {



        BuckFlow buckFlow = new BuckFlow();
        buckFlow.setDesc("A buck flow for test");
        buckFlow.setAmount(50);
        buckFlow.setFlowType(1);
        buckFlow.setFromBucket(from);
        buckFlow.setToBucket(to);
        buckFlow.setLatestUpdateEpoch(System.currentTimeMillis());
        buckFlow.setCreateEpoch(System.currentTimeMillis());

        buckFlow.setRole(role);

        bfService.save(buckFlow);

        return buckFlow;
    }


    private Role saveRole() {
        RoleService roleService = (RoleService) context.getBean("roleService");
        Role role = new Role();

        role.setDescription("for test BuckFlowService" );
        role.setRoleName("vin" + System.nanoTime()) ;
        roleService.save(role);

        return role;
    }

    private Bucket saveExpenseBucket() {
        BucketService bucketService = (BucketService) context.getBean("bucketService");

        Bucket bucket = new Bucket();
        bucket.setName("Expense bucket");
        bucket.setDesc("Expense bucket for test");
        bucket.setType(0);
        bucket.setBalance(0);

        bucketService.save(bucket);

        return bucket;
    }

    private Bucket saveBucket(int balance, int type) {
        BucketService bucketService = (BucketService) context.getBean("bucketService");

        Bucket bucket = new Bucket();
        bucket.setDesc("For test");
        bucket.setBalance(balance);
        bucket.setType(type);
        bucket.setName("credit card");

        bucketService.save(bucket);

        return bucket;
    }


}
