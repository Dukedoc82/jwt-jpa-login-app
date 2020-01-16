package com.dyukov.taxi.model;

import com.dyukov.taxi.entity.TpUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class TpUserDetails extends User {

    private TpUser user;

    public TpUserDetails(TpUser user, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, user.isEnabled(), true, true, true, authorities);
        this.user = user;
    }

    public TpUser getUser() {
        return user;
    }

    public void setUser(TpUser user) {
        this.user = user;
    }

}
