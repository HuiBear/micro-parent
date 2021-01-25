package com.micro.ykh.common.config.auth;

import com.micro.ykh.common.extension.ClientDaoAuthenticationProvider;
import com.micro.ykh.common.extension.CustomDaoAuthenticationProvider;
import com.micro.ykh.common.extension.CustomOauthUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.sql.DataSource;

/**
 * @ClassName WebSecurityConfiguration
 * @Description 授权中心web安全配置
 * @Author xiongh
 * @Date 2020/12/21 11:38
 * @Version 1.0
 **/
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Qualifier("customOauthUserDetailService")
    @Autowired
    private CustomOauthUserDetailService userDetailsService;

    @Autowired
    private DataSource dataSource;


//    @Autowired
//    private LogoutSuccessHandler logoutSuccessHandler;

    /**
     * 方法实现说明:用于构建用户认证组件,需要传递userDetailsService和密码加密器
     * 为了满足多平台用户统一登录，这块自己定义了一个CustomOauthUserDetailsService,以及重写了CustomDaoAuthenticationProvider的retrieveUser方法
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(customAuthenticationProvider());
    }

    /**
     * 设置前台静态资源不拦截
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**", "/css/**", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/user/login", "/user/oauth/token", "/user/refresh_token", "/user/sms/verification_code", "/user/captcha_image", "/user/remote/**").permitAll()
                //关掉跨域保护
                .and().csrf().disable().cors();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 自定义认证提供者：重写了CustomDaoAuthenticationProvider和CustomeOauthUserDetailService，
     *
     * @return
     */
    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        CustomDaoAuthenticationProvider customDaoAuthenticationProvider = new CustomDaoAuthenticationProvider();
        customDaoAuthenticationProvider.setUserDetailsService(userDetailsService);
        customDaoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        customDaoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return customDaoAuthenticationProvider;
    }

    @Bean
    public AuthenticationProvider clientAuthenticationProvider() {
        ClientDaoAuthenticationProvider clientDaoAuthenticationProvider = new ClientDaoAuthenticationProvider();
        clientDaoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        clientDaoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        clientDaoAuthenticationProvider.setUserDetailsService(new ClientDetailsUserDetailsService(clientDetails()));
        return clientDaoAuthenticationProvider;
    }

    /**
     * 方法实现说明:用于查找我们第三方客户端的组件 主要用于查找 数据库表 oauth_client_details
     *
     * @return
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

}
