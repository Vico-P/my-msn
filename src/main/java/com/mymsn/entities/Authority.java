package com.mymsn.entities;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {
    String authority;

    public Authority() {
    }

    public Authority(String authority) {
        this.authority = authority;
    }

    public Authority authority(String authority) {
        setAuthority(authority);
        return this;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        // TODO Auto-generated method stub
        return null;
    }
}
