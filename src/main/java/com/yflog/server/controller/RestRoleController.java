package com.yflog.server.controller;

import com.yflog.common.ErrorCodes;
import com.yflog.server.pojo.Response;
import com.yflog.entity.Role;
import com.yflog.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.yflog.common.ErrorCodes.*;

/**
 * Created by vincent on 10/2/16.
 */
@Controller
@RequestMapping("/rest/role")
public class RestRoleController {

    @Autowired
    private RoleService roleService;


    @RequestMapping(value = "/", method = RequestMethod.POST, headers = {"Content-type=application/json"})
    @ResponseBody
    public Response addRole(@RequestBody Role role) {
        System.out.println("post role: " + role);
        Response response = new Response();
        roleService.save(role);
        response.setData(role);
        return response;
    }

    @RequestMapping(value = "/{roleId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Response delete(@PathVariable int roleId) {
        Role role = roleService.getRole(roleId);

        Response response = new Response();
        if (role == null) {
            response.set(ErrorCodes.STATUS_NO_REOCORD, ErrorCodes.ERR_MSG_NO_SUCH_RECORD);
        }
        else {
            roleService.delete(role);
        }
        return response;
    }

    @RequestMapping(value = "/{roleId}", method = RequestMethod.PUT)
    @ResponseBody
    public Response updateRole(@RequestBody Role role, @PathVariable int roleId) {
        role.setId(roleId);
        roleService.update(role);

        Response response = new Response();
        response.setData(role);

        return response;
    }

    @RequestMapping(
            value = "/{roleId}",
            method = RequestMethod.GET)
    @ResponseBody
    public Response getRoleById(@PathVariable int roleId) {
        Role role = roleService.getRole(roleId);

        System.out.println("Role got: " + role);

        Response response = new Response();
        if (role == null) {
            response.set(STATUS_NO_REOCORD, ERR_MSG_NO_SUCH_RECORD, null);
        }
        else {
            response.setData(role   );
        }

        return response;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public Response test() {
        return new Response();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public Response viewAll() {
        Response response = new Response();
        response.setData(roleService.loadAll());

        return response;
    }

}
