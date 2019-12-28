package com.github.dhiraj072.LibMgtSystem.auth;

import com.github.dhiraj072.LibMgtSystem.member.Member;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class LibraryUserDetails implements UserDetails {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(LibraryUserDetails.class);

  private Member member;

  public LibraryUserDetails(Member member) {

    this.member = member;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    return null;
  }

  @Override
  public String getPassword() {

    LOGGER.trace("Getting password for {}", member);
    return member.getPassword();
  }

  @Override
  public String getUsername() {

    return member.getUserName();
  }

  @Override
  public boolean isAccountNonExpired() {

    return true;
  }

  @Override
  public boolean isAccountNonLocked() {

    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {

    return true;
  }

  @Override
  public boolean isEnabled() {

    return true;
  }
}
