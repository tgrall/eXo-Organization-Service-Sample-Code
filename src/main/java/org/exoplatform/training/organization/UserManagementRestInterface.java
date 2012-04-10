/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *
 */
package org.exoplatform.training.organization;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.exoplatform.services.organization.User;

import org.exoplatform.services.rest.resource.ResourceContainer;

@Path("/extensions/users/")
public class UserManagementRestInterface implements ResourceContainer {

    @GET
    @Path("/add")
    public String addUser(
            @QueryParam("userName") String userName,
            @QueryParam("email") String email,
            @QueryParam("firstName") String firstName,
            @QueryParam("lastName") String lastName,
            @QueryParam("password") String password) {

        StringBuilder sb = new StringBuilder();

        try {
            UserManagementService userMgmtService = new UserManagementService();
            userMgmtService.createUser(userName, email, firstName, lastName, password);
            sb.append("User ").append(userName).append(" created.");
        } catch (Exception ex) {
            Logger.getLogger(UserManagementRestInterface.class.getName()).log(Level.SEVERE, null, ex);
            sb.append(ex.getMessage());
        }
        return sb.toString();
    }

    
    @GET
    @Path("/addMembership")
    public String addMembership(
            @QueryParam("userName") String userName,
            @QueryParam("groupName") String groupName, 
            @QueryParam("membershipType") String membershipType) {

        StringBuilder sb = new StringBuilder();

        try {
            UserManagementService userMgmtService = new UserManagementService();
            userMgmtService.createMembership(userName, groupName, membershipType);
            sb.append("User ").append(userName).append(" added to ").append(membershipType).append(":").append(groupName);
        } catch (Exception ex) {
            Logger.getLogger(UserManagementRestInterface.class.getName()).log(Level.SEVERE, null, ex);
            sb.append(ex.getMessage());
        }
        return sb.toString();
    }    
    
    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUser(@QueryParam("userName") String userName) {

        List<Object> listData = new ArrayList<Object>();
        try {
            UserManagementService userMgmtService = new UserManagementService();
            User user = userMgmtService.findUserByUserName(userName);
            listData.add(user);

        } catch (Exception ex) {
            Logger.getLogger(UserManagementRestInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return renderJSON(listData);
    }


    
    private Response renderJSON(List<Object> list) {
        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoStore(true);
        MessageBean data = new MessageBean();
        data.setData(list);
        return Response.ok(data, MediaType.APPLICATION_JSON).cacheControl(cacheControl).build();
    }
}
