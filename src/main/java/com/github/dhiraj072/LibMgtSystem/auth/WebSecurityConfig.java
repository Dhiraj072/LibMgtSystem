package com.github.dhiraj072.LibMgtSystem.auth;

import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Resource
  private AuthenticationEntryPoint restAuthenticationEntryPoint;

  @Resource
  private MySavedRequestAwareAuthenticationSuccessHandler mySavedRequestAwareAuthenticationSuccessHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
      .csrf().disable()
      .exceptionHandling()
      .authenticationEntryPoint(restAuthenticationEntryPoint)
    .and()
      .authorizeRequests()
      .antMatchers("/do_login").permitAll()
      .antMatchers("/login").permitAll()
      .anyRequest().authenticated()
    .and()
      .formLogin()
      .successHandler(mySavedRequestAwareAuthenticationSuccessHandler)
      .failureHandler(new SimpleUrlAuthenticationFailureHandler())
      .permitAll()
    .and()
      .logout()
      .permitAll();
  }

  @Bean
  @Override
  public UserDetailsService userDetailsService() {

    UserDetails user =
        User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();

    return new InMemoryUserDetailsManager(user);
  }
}
