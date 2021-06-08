package com.example.stortorget.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;


@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                //     .usersByUsernameQuery("select user_name,CONCAT('{noop}',password),true from users where user_name=?")
                .usersByUsernameQuery("select user_name, password, true from users where user_name=?")
                .authoritiesByUsernameQuery("select user_name, role from users where user_name=?");
        ;
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/style.css").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/adboard").hasAnyAuthority("Admin", "User")
                .antMatchers("/userinfo").hasAuthority("Admin")
                .antMatchers("/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/")
                .and()
                .formLogin()
                .permitAll()
                .defaultSuccessUrl("/")
                .loginPage("/login")
                .and()
                .logout()
                .permitAll()
                .deleteCookies("JSESSIONID");

    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }




}
