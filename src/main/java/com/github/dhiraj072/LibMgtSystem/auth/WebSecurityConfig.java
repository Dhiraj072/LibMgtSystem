package com.github.dhiraj072.LibMgtSystem.auth;

import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@Profile("!IntegrationTest")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Resource
  private AuthenticationEntryPoint restAuthenticationEntryPoint;

  @Resource
  private MySavedRequestAwareAuthenticationSuccessHandler mySavedRequestAwareAuthenticationSuccessHandler;

  @Resource
  private LibraryUserDetailsService userDetailsService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
      .csrf().disable()
      .exceptionHandling()
      .authenticationEntryPoint(restAuthenticationEntryPoint)
    .and()
      .authorizeRequests()
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
  public DaoAuthenticationProvider provider() {

    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(bCryptEncoder());
    return provider;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {

    auth.authenticationProvider(provider());
  }

  @Bean
  public PasswordEncoder bCryptEncoder() {

    return new BCryptPasswordEncoder();
  }
}
