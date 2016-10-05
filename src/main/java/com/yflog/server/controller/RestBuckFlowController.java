package com.yflog.server.controller;

import com.yflog.common.ErrorCodes;
import com.yflog.entity.BuckFlow;
import com.yflog.server.pojo.Response;
import com.yflog.service.BuckFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by vincent on 10/3/16.
 */
@Controller
@RequestMapping("/rest/buckflow")
public class RestBuckFlowController {

    @Autowired
    private BuckFlowService buckFlowService;

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response addBuckFlow(@RequestBody BuckFlow buckFlow) {
        buckFlowService.save(buckFlow);
        Response response = new Response();
        response.setData(buckFlow);

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/{bfId}", method = RequestMethod.DELETE)
    public Response deleteById(@PathVariable int bfId) {
        BuckFlow bf = buckFlowService.getById(bfId);
        Response response = new Response();
        if (bf == null) {
            response.set(ErrorCodes.STATUS_NO_REOCORD, ErrorCodes.ERR_MSG_NO_SUCH_RECORD);
        }
        else {
            buckFlowService.delete(bf);
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/{bfId}", method = RequestMethod.GET)
    public Response getById(@PathVariable int bfId) {
        BuckFlow bf = buckFlowService.getById(bfId);
        Response response = new Response();
        if (bf == null) {
            response.set(ErrorCodes.STATUS_NO_REOCORD, ErrorCodes.ERR_MSG_NO_SUCH_RECORD);
        }
        else {
            response.setData(bf);
        }
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/{bfId}", method = RequestMethod.PUT)
    public Response update(@PathVariable int bfId, @RequestBody BuckFlow bf) {
        BuckFlow check = buckFlowService.getById(bfId);

        Response response = new Response();
        if (check == null) {
            response.set(ErrorCodes.STATUS_NO_REOCORD, ErrorCodes.ERR_MSG_NO_SUCH_RECORD);
        }
        else {
            bf.setId(bfId);
            buckFlowService.update(bf);
            response.setData(bf);
        }

        return response;
    }
}
