package com.yflog.server.controller;

import com.yflog.common.ErrorCodes;
import com.yflog.entity.User;
import com.yflog.server.pojo.Response;
import com.yflog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by vincent on 10/3/16.
 */
@Controller
@RequestMapping("/rest/user")
public class RestUserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response addUser(@RequestBody User user) {
        userService.save(user);
        Response response = new Response();
        response.setData(user);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public Response getById(@PathVariable int userId) {
        User user = userService.getById(userId);
        Response response = new Response();
        if (user == null) {
            response.set(ErrorCodes.STATUS_NO_REOCORD, ErrorCodes.ERR_MSG_NO_SUCH_RECORD);
        }
        else {
            response.setData(user);
        }

        return response;
    }


    @ResponseBody
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public Response deletedById(@PathVariable int userId) {
        User user = userService.getById(userId);
        Response response = new Response();
        if (user == null) {
            response.set(ErrorCodes.STATUS_NO_REOCORD, ErrorCodes.ERR_MSG_NO_SUCH_RECORD);
        }
        else {
            userService.delete(user);
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public Response updateUser(@PathVariable int userId, @RequestBody User user) {
        User check = userService.getById(userId);
        Response response = new Response();
        if (check == null) {
            response.set(ErrorCodes.STATUS_NO_REOCORD, ErrorCodes.ERR_MSG_NO_SUCH_RECORD);
        }
        else {
            user.setId(userId);
            userService.update(user);
            response.setData(user);
        }

        return response;
    }

}
