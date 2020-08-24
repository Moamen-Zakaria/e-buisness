package com.vodafone.ebuisness.configuration;

import com.vodafone.ebuisness.security.util.impl.JwtTokenProviderImpl;
import com.vodafone.ebuisness.security.filter.JwtFilterConfigurer;
import com.vodafone.ebuisness.security.filter.MyUsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String rolePrefix = "ROLE_";

    @Autowired
    private JwtTokenProviderImpl jwtTokenProvider;

    @Bean
    public MyUsernamePasswordAuthenticationFilter getMyUsernamePasswordAuthenticationFilter() throws Exception {
        return new MyUsernamePasswordAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()

                .antMatchers("/admin/retail/**").hasRole(AccountRole.ADMIN)
                .antMatchers("/admin/auth/**").hasRole(AccountRole.ADMIN)

                .antMatchers("/customer/carts/**").hasRole(AccountRole.USER)

                .antMatchers("/shared/accounts/**").hasAnyRole(AccountRole.ADMIN , AccountRole.USER)
                .antMatchers("/shared/carts/**").hasAnyRole(AccountRole.ADMIN , AccountRole.USER)

                .anyRequest().permitAll()

                .and()
                .apply(new JwtFilterConfigurer(getMyUsernamePasswordAuthenticationFilter()));
    }
}
