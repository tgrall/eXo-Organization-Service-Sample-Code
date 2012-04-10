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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.MembershipTypeHandler;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserHandler;
import org.exoplatform.services.organization.UserProfileHandler;

/**
 *
 * @author tgrall
 */
public class UserManagementService {

    OrganizationService organizationService = null;
    GroupHandler groupHandler;
    MembershipHandler membershipHandler;
    UserHandler userHandler;
    MembershipTypeHandler membershipTypeHandler;
    UserProfileHandler userProfileHandler;

    /**
     * Initialize the class and setup all utilities
     */
    public UserManagementService() {
        ExoContainer container = ExoContainerContext.getCurrentContainer();
        organizationService = (OrganizationService) container.getComponentInstanceOfType(OrganizationService.class);

        groupHandler = organizationService.getGroupHandler();
        userHandler = organizationService.getUserHandler();
        membershipHandler = organizationService.getMembershipHandler();
        membershipTypeHandler = organizationService.getMembershipTypeHandler();
        userProfileHandler = organizationService.getUserProfileHandler();

    }

    /**
     * Create and save user
     * @param userName
     * @param email
     * @param firstName
     * @param lastName
     * @param password
     * @throws Exception 
     */
    public void createUser(String userName, String email, String firstName, String lastName, String password) throws Exception {
        User user = findUserByUserName(userName);
        if (user == null) {
            user = userHandler.createUserInstance(userName);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(password);
            userHandler.createUser(user, true);
        } else {
            throw new Exception("User " + userName + " already exist");
        }
    }

    public void createGroup(String parentId, String name, String label, String desc) throws Exception {
        Group parent = parentId == null ? null : groupHandler.findGroupById(parentId);
        Group child = groupHandler.createGroupInstance();
        child.setGroupName(name);
        child.setLabel(label);
        child.setDescription(desc);
        groupHandler.addChild(parent, child, true);

    }

    public void createMembership(String userName, String groupName, String membershipType) throws Exception {
        membershipHandler.linkMembership(userHandler.findUserByName(userName), groupHandler.findGroupById("/" + groupName), membershipTypeHandler.findMembershipType(membershipType), true);
    }

    /**
     * 
     * @param type
     * @param desc
     * @throws Exception 
     */
    public void createMembershipType(String type, String desc) throws Exception {
        MembershipType membershipType = membershipTypeHandler.createMembershipTypeInstance();
        membershipType.setName(type);
        membershipType.setDescription(desc);
        membershipTypeHandler.createMembershipType(membershipType, true);
    }

    /**
     * 
     * @param username
     * @return User if exists, null if not 
     */
    public User findUserByUserName(String username) {

        User user = null;
        try {
            user = userHandler.findUserByName(username);
        } catch (Exception ex) {
        }
        return user;

    }
}
