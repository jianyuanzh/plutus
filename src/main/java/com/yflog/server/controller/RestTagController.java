package com.yflog.server.controller;

import com.yflog.common.ErrorCodes;
import com.yflog.entity.Tag;
import com.yflog.server.pojo.Response;
import com.yflog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by vincent on 10/3/16.
 */
@Controller
@RequestMapping(value = "/rest/tag")
public class RestTagController {

    @Autowired
    private TagService tagService;

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response addTag(@RequestBody Tag tag) {
        tagService.save(tag);
        Response response =  new Response();
        response.setData(tag);
        return response;
    }


    @ResponseBody
    @RequestMapping(value = "/{tagId}", method = RequestMethod.GET)
    public Response getById(@PathVariable int tagId) {
        Response response = new Response();
        Tag tag = tagService.getById(tagId);
        if (tag == null) {
            response.set(ErrorCodes.STATUS_NO_REOCORD, ErrorCodes.ERR_MSG_NO_SUCH_RECORD);
        }
        else {
            response.setData(tag);
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/{tagId}", method = RequestMethod.DELETE)
    public Response delete(@PathVariable int tagId) {
        Tag tag = tagService.getById(tagId);
        Response response = new Response();
        if (tag == null) {
            response.set(ErrorCodes.STATUS_NO_REOCORD, ErrorCodes.ERR_MSG_NO_SUCH_RECORD);
        }
        else {
            tagService.delete(tag);
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/{tagId}", method = RequestMethod.PUT)
    public Response update(@RequestBody Tag tag,
                           @PathVariable int tagId) {
        Tag checkExist = tagService.getById(tagId);
        Response response = new Response();

        if (checkExist == null) {
            response.set(ErrorCodes.STATUS_NO_REOCORD, ErrorCodes.ERR_MSG_NO_SUCH_RECORD);
        }
        else {
            tag.setId(tagId);
            tagService.update(tag);
            response.setData(tag);
        }

        return response;
    }

}
