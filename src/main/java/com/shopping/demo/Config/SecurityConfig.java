package com.shopping.demo.Config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http    .csrf(csrf -> csrf.disable())  //  關掉 CSRF
                .authorizeHttpRequests(configurer  -> configurer

                        // 公開
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/error").permitAll()

                        //登入
                        .requestMatchers("/productlist").authenticated()
                        .requestMatchers("/orders/**").authenticated()
                        .requestMatchers("/cart/**").authenticated()
                        .requestMatchers("/cart/items").authenticated()
                        .requestMatchers("/orders").authenticated() // POST /orders 結帳

                        //  管理員(hasRole("ADMIN"))
                        .requestMatchers("/create").hasRole("ADMIN")  // 新增
                        .requestMatchers("/edit/**").hasRole("ADMIN") // 修改
                        .requestMatchers("/delete/**").hasRole("ADMIN") // 刪除
                        .anyRequest().authenticated()       // 登入
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/productlist",false)
                        .permitAll()
                )
                .logout(logout -> logout



                        .logoutSuccessUrl("/login?logout") // 跳回商品列表，並加上 ?logout 參數 (方便顯示提示)

                        .invalidateHttpSession(true)

                        // 【重要】刪除特定的 Cookie (例如 JSESSIONID)
                        .deleteCookies("JSESSIONID")

                        .permitAll() // 允許所有人訪問登出 URL
                );
        return http.build();
    }



/*    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select username, password, true as enabled from users where username=?");

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select username, authority from users where username=?");


        return jdbcUserDetailsManager;
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
