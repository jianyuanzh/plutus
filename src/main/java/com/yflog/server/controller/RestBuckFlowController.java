package com.yflog.server.controller;

import com.yflog.common.ErrorCodes;
import com.yflog.entity.BuckFlow;
import com.yflog.entity.util.Type;
import com.yflog.server.pojo.Response;
import com.yflog.service.BuckFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        response.setData(buckFlowService.getById(buckFlow.getId()));

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
            response.setData(buckFlowService.getById(bfId));
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/all/{type}", method = RequestMethod.GET)
    public Response viewAll(@PathVariable String type,
                            @RequestParam(name = "from", defaultValue = "-1") long from,
                            @RequestParam(value = "to", defaultValue = "-1") long to) {

        Response response = new Response();
        try {
            Type tp = Type.valueOf(type.toUpperCase());
            List<BuckFlow> flows =null;
            if (from == -1 || to == -1) {
                flows = buckFlowService.listAllFlows(tp);
            }
            else {
                flows = buckFlowService.listAllFlows(tp, from, to);
            }

            response.setData(flows);

            return response;
        }
        catch (Exception e) {
            response.set(ErrorCodes.STATUS_REST_ARGUMENT_ERROR, String.format("%s: flowType invalid - %s",
                    ErrorCodes.ERR_MSG_REST_ARGUMENT_ERROR, type));
        }

        return response;

    }

    public static void main(String[] args) {
        for (Type type : Type.values()) {
            System.out.println(type.toString());
        }

    }
}
