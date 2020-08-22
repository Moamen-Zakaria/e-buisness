package com.vodafone.ebuisness.security.filter;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter;

    public JwtFilterConfigurer(MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter) {
        this.myUsernamePasswordAuthenticationFilter = myUsernamePasswordAuthenticationFilter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(myUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }


}
