package com.vaadin.samples.authentication;

import java.io.Serializable;

/**
 * Simple interface for authentication and authorization checks.
 */
public interface AccessControl extends Serializable {

    String ADMIN_ROLE_NAME = "admin";
    String ADMIN_USERNAME = "admin";

    public boolean signIn(String username, String password);

    public boolean isUserSignedIn();

    public boolean isUserInRole(String role);

    public String getPrincipalName();
}
