package com.micro.ykh.common.config.auth;

import com.micro.ykh.constant.FwtConstant;
import com.micro.ykh.common.extension.AuthTokenEnhancer;
import com.micro.ykh.common.extension.CustomOauthUserDetailService;
import com.micro.ykh.common.extension.CustomTokenService;
import com.micro.ykh.common.extension.CustomUserDetailsByNameServiceWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Collections;

/**
 * @ClassName AuthorizationConfig
 * @Description 授权服务器配置
 * @Author xiongh
 * @Date 2020/12/21 11:34
 * @Version 1.0
 **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClientDetailsService clientDetails;

    @Qualifier("customOauthUserDetailService")
    @Autowired
    private CustomOauthUserDetailService userDetailsService;

    /**
     * 方法实现说明: 使用jwt存储token,我们创建jwtTOkenStore的时候 需要一个组件
     * jwtAccessTokenConverter  所以我们 可以通过@Bean的形式 创建一个该组件.
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 这个组件 用于jwt basecode 字符串和 安全认证对象的信息转化
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //jwt的密钥(用来保证jwt 字符串的安全性  jwt可以防止篡改  但是不能防窃听  所以jwt不要 放敏感信息)
        converter.setKeyPair(keyPair());
        //converter.setSigningKey("123456");
        return converter;
    }

    /**
     * KeyPair是 非对称加密的公钥和私钥的保存者
     *
     * @return
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(FwtConstant.RSA_KEY_PATH), FwtConstant.RSA_PLAIN_TEXT.toCharArray());
        return keyStoreKeyFactory.getKeyPair(FwtConstant.RSA_KEY_ALIA, FwtConstant.RSA_PLAIN_TEXT.toCharArray());
    }

    @Bean
    public AuthTokenEnhancer authTokenEnhancer() {
        return new AuthTokenEnhancer();
    }

    /**
     * 方法实现说明:认证服务器能够给哪些 客户端颁发token  我们需要把客户端的配置 存储到
     * 数据库中 可以基于内存存储和db存储
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails);
    }



    /**
     * 方法实现说明:授权服务器的配置的配置
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {


        //增加我们的令牌信息
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(authTokenEnhancer(), jwtAccessTokenConverter()));
//        tokenEnhancerChain.setTokenEnhancers(Collections.singletonList(jwtAccessTokenConverter()));
        //授权服务器颁发的token 怎么存储的
        endpoints.tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                //用户来获取token的时候需要 进行账号密码
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager)
                .tokenServices(customTokenService(endpoints));
    }


    private CustomTokenService customTokenService(AuthorizationServerEndpointsConfigurer endpoints) {
        CustomTokenService customTokenService = new CustomTokenService();
        customTokenService.setTokenStore(endpoints.getTokenStore());
        customTokenService.setSupportRefreshToken(true);
        customTokenService.setReuseRefreshToken(true);
        customTokenService.setClientDetailsService(clientDetails);
        customTokenService.setTokenEnhancer(endpoints.getTokenEnhancer());
        // 设置自定义的CustomUserDetailsByNameServiceWrapper
        if (userDetailsService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new CustomUserDetailsByNameServiceWrapper(userDetailsService));
            customTokenService.setAuthenticationManager(new ProviderManager(Collections.singletonList(provider)));
        }
        return customTokenService;
    }

    /**
     * 方法实现说明:授权服务器安全配置
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //第三方客户端校验token需要带入 clientId 和clientSecret来校验
        security.checkTokenAccess("isAuthenticated()")
                //来获取我们的tokenKey需要带入clientId,clientSecret
                .tokenKeyAccess("isAuthenticated()");
        security.allowFormAuthenticationForClients();

    }

}
