package com.jeong.oauth2authserver.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class MemberDto implements UserDetails {

    /**
     * 아이디
     */
    private String userName;

    /**
     * 비밀번호
     */
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        return roles;
    }


    @Override
    public String getUsername() {
        return this.userName;
    }


    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }


    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
}
