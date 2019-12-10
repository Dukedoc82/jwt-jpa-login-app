package com.dyukov.taxi.model;

import com.dyukov.taxi.dao.UserDao;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class TpUserDetails extends User {

    private UserDao user;

    public TpUserDetails(UserDao user, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.user = user;
    }

    public UserDao getUser() {
        return user;
    }

    public void setUser(UserDao user) {
        this.user = user;
    }

}
