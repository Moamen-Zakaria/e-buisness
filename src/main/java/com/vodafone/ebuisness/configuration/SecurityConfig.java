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
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                .antMatchers("/admin/retail/**").hasRole(AccountRole.ADMIN)

                .antMatchers("/admin/retail/**").authenticated()
                .antMatchers("/admin/auth/**").authenticated()

                .antMatchers("/customer/carts/**").authenticated()

                .antMatchers("/shared/accounts/**").authenticated()
                .antMatchers("/shared/carts/**").authenticated()

                .antMatchers("/auth/**").permitAll()
//                .antMatchers(HttpMethod.GET, "/admin/retail/**").permitAll()
//                .antMatchers(HttpMethod.DELETE, "/admin/retail/**").hasRole("ADMIN")
//                .antMatchers(HttpMethod.GET, "/v1/vehicles/**").permitAll()
//                .anyRequest().authenticated()
                .and()
                .apply(new JwtFilterConfigurer(getMyUsernamePasswordAuthenticationFilter()));
    }
}
