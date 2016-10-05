package com.yflog.server.controller;

import com.yflog.common.ErrorCodes;
import com.yflog.entity.Bucket;
import com.yflog.server.pojo.Response;
import com.yflog.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.yflog.common.ErrorCodes.ERR_MSG_NO_SUCH_RECORD;
import static com.yflog.common.ErrorCodes.STATUS_NO_REOCORD;

/**
 * Created by vincent on 10/3/16.
 */
@Controller
@RequestMapping("/rest/bucket")
public class RestBucketController {
    @Autowired
    private BucketService bucketService;

    @ResponseBody
    @RequestMapping(value = "/",
            method = RequestMethod.POST,
            headers = {"Content-type=application/json"}
    )
    public Response addBucket(@RequestBody Bucket bucket) {
        Response response = new Response();
        bucketService.save(bucket);
        response.setData(bucket);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/{bucketId}", method = RequestMethod.DELETE)
    public Response delete(@PathVariable int bucketId) {
        Bucket bucket = bucketService.getById(bucketId);
        Response response = new Response();
        if (bucket == null) {
            response.set(STATUS_NO_REOCORD, ErrorCodes.ERR_MSG_NO_SUCH_RECORD);
        }
        else {
            bucketService.delete(bucket);
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/{bucketId}", method = RequestMethod.GET)
    public Response getBucketById(@PathVariable int bucketId) {
        Bucket bucket = bucketService.getById(bucketId);

        Response response = new Response();
        if (bucket == null) {
            response.set(STATUS_NO_REOCORD, ERR_MSG_NO_SUCH_RECORD);
        }

        else {
            response.setData(bucket);
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/{bucketId}", method = RequestMethod.PUT)
    public Response updateBucket(@PathVariable int bucketId, @RequestBody Bucket bucket) {
        Response response = new Response();
        Bucket check = bucketService.getById(bucketId);
        if (check != null) {
            bucket.setId(bucketId);
            bucketService.update(bucket);
            response.setData(bucket);
        }
        else {
            response.set(STATUS_NO_REOCORD, ERR_MSG_NO_SUCH_RECORD);
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Response viewAll() {
        List<Bucket> buckets = bucketService.loadAll();
        Response response = new Response();
        response.setData(buckets);

        return response;
    }

}
