package com.ecsite.springbootoracle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Set;

@Configuration
@EnableWebSecurity
public class AdminConfiguration extends WebSecurityConfigurerAdapter {
    @Bean
    //Find User.
    public UserDetailsService userDetailsService(){
        return new AdminServiceConfig();  //return to AdminServiceConfig() in package: config/AdminServiceConfig  //This method will use the findByUsername function, find the user, if there is a user, return the user object, if not, it will return an error message.
    }

    @Bean //hash password
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean //Continue of line17.
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());  //if possible find user from database from line17, set that user info to provider
        provider.setPasswordEncoder(passwordEncoder()); //will return the encrypted password, so as not to show real the password.
        return provider;
    }

    //This method is called by Spring Security during application startup to configure the authentication mechanism.(it is used to configure the DaoAuthenticationProvider that will be used for authenticating users.)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    //This method is used to configure the security for HTTP requests in your application(used to specify the authorization rules for different URLs in your application, as well as the login and logout URLs and behavior.)
    @Override
    protected void configure (HttpSecurity http) throws Exception{
        http.authorizeRequests().antMatchers("/*").permitAll()
                .antMatchers("/admin/*")
                .hasAuthority("ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")  //login with this URL
                .loginProcessingUrl("/do-login") //when pass ID and password, th:action in thymeleaf will send request to "do-login"
                //.defaultSuccessUrl("/index") //login success -> redirect to URL: index.html
                .successHandler(successHandler())   //access permission setting: if ADMIN -> go to admin page, if user -> go to user page.
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))  //access to UTL : /logout, it will logout.
                .logoutSuccessUrl("/login?logout")
                .permitAll();
    }

    private AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());  //base on file UserDetails.java in line 21, system know what "Role" user have.
            System.out.println("value of role: "+ roles);
            if (roles.contains("ADMIN")) {
                response.sendRedirect("admin-page");
            } else if (roles.contains("USER")) {
                response.sendRedirect("shop");
            } else {
                throw new IllegalStateException("User has no valid role");
            }
        };
    }
}
