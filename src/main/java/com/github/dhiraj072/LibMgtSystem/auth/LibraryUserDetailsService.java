package com.github.dhiraj072.LibMgtSystem.auth;

import com.github.dhiraj072.LibMgtSystem.member.Member;
import com.github.dhiraj072.LibMgtSystem.member.MemberDAO;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class LibraryUserDetailsService implements UserDetailsService {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(LibraryUserDetailsService.class);

  @Resource
  private MemberDAO memberDAO;

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {

    LOGGER.trace("Getting user {}", username);
    Member member = memberDAO.getMember(username);
    if (member == null) {

      throw new UsernameNotFoundException(username);
    }
    LOGGER.info("Got user details {}", member);
    return new LibraryUserDetails(member);
  }
}
