package com.ecsite.springbootoracle.config;

import com.ecsite.springbootoracle.model.Admin;
import com.ecsite.springbootoracle.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class AdminConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    AdminService adminService;

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
        http.authorizeRequests()
                .antMatchers("/*").permitAll()
                .antMatchers("/activateUser/**").permitAll() // allow access without authentication
                .antMatchers("/admin/*").hasAuthority("ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")  //login with this URL
                .loginProcessingUrl("/do-login") //when pass ID and password, th:action in thymeleaf will send request to "do-login"
                //.defaultSuccessUrl("/index") //login success -> redirect to URL: index.html
                .successHandler(successHandler())   //access permission setting: if ADMIN -> go to admin page, if user -> go to user page.
                .failureHandler(authenticationFailureHandler())
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))  //access to UTL : /logout, it will logout.
                .logoutSuccessUrl("/login?logout")
                .permitAll();
    }



    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            String errorMessage = "";
            if (exception.getClass().isAssignableFrom(BadCredentialsException.class)){
                errorMessage = "Invalid username or password";
            } else if (exception.getClass().isAssignableFrom(DisabledException.class)) {
                errorMessage = "Account is not active, please contact an administrator";
            }
            request.getSession().setAttribute("errorMessage", errorMessage);
            response.sendRedirect("/login?error");
        };
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                for (GrantedAuthority grantedAuthority : authorities) {
                    if (grantedAuthority.getAuthority().equals("ADMIN")) {
                        response.sendRedirect("admin-page");
                        return;
                    } else if (grantedAuthority.getAuthority().equals("USER")) {
                        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                        String username = userDetails.getUsername();
                        Admin admin = adminService.findByUsername(username);
                        if (admin != null && !admin.isActive()) {
                            request.getSession().setAttribute("errorMessage", "Account is not active, please contact to administrator");
                            response.sendRedirect("/login?error");
                        } else {
                            response.sendRedirect("user-homepage");
                            return;
                        }
                    }
                    else {
                        throw new IllegalStateException("User has no valid role, please contact to administrator");
                    }
                }
            }
        };
    }

}
