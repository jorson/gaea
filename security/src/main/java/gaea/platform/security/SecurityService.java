package gaea.platform.security;


import gaea.platform.security.access.User;

public interface SecurityService {

    public User getUser(String username);
}
