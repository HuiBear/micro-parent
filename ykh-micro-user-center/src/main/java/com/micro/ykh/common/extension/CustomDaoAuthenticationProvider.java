package com.micro.ykh.common.extension;

import com.micro.ykh.common.controller.model.LoginVO;
import com.micro.ykh.exception.CustomException;
import com.micro.ykh.type.UserLoginMode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @ClassName CustomDaoAuthenticationProvider
 * @Description TODO
 * @Author xiongh
 * @Date 2020/12/17 11:02
 * @Version 1.0
 **/
public class CustomDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    /**
     * The plaintext password used to perform
     * PasswordEncoder#matches(CharSequence, String)}  on when the user is
     * not found to avoid SEC-2056.
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    // ~ Instance fields
    // ================================================================================================

    private PasswordEncoder passwordEncoder;

    /**
     * The password used to perform
     * {@link PasswordEncoder#matches(CharSequence, String)} on when the user is
     * not found to avoid SEC-2056. This is necessary, because some
     * {@link PasswordEncoder} implementations will short circuit if the password is not
     * in a valid format.
     */
    private volatile String userNotFoundEncodedPassword;

    private CustomOauthUserDetailService userDetailsService;

    private UserDetailsPasswordService userDetailsPasswordService;

    public CustomDaoAuthenticationProvider() {
        setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    // ~ Methods
    // ========================================================================================================

    @SuppressWarnings("deprecation")
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
        // 数据库中的密码设置了加密模式，所以web提交的password要加密转换，才能与数据库的密码匹配
        String presentedPassword = authentication.getCredentials().toString();
        /*
         * 这边要获取用户登录模式
         *    1.loginMode == 密码模式(passwd),需要验证密码是否匹配
         *    2.loginMode == 手机模式(phone),不需要验证密码
         */
        Map<String, String> map = (Map<String, String>) authentication.getDetails();
        String loginMode = map.get("loginMode");
        if (UserLoginMode.PASSWORD.getLoginMode().equals(loginMode)) {
            if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
                logger.debug("Authentication failed: password does not match stored value");

//                throw new BadCredentialsException(messages.getMessage(
//                        "AbstractUserDetailsAuthenticationProvider.badCredentials",
//                        "Bad credentials"));
                throw new CustomException("密码不正确，请重新输入！");
            }
        }

    }

    @Override
    protected void doAfterPropertiesSet() {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }

    @Override
    protected final UserDetails retrieveUser(String username,
                                             UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        prepareTimingAttackProtection();
        // 自定义添加
        Map<String, String> map = (Map<String, String>) authentication.getDetails();
        // 数据转换
        LoginVO vo = new LoginVO();
        vo.setLoginMode(map.get("loginMode"));
        vo.setPlatformType(map.get("platformType"));
        vo.setMobilePhone(map.get("mobilePhone"));
        vo.setUsername(map.get("username"));
        vo.setMobilePhone(map.get("mobilePhone"));
        try {
            UserDetails loadedUser = this.getUserDetailsService().customLoadUserByLoginVO(vo);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        } catch (UsernameNotFoundException ex) {
            mitigateAgainstTimingAttack(authentication);
            throw ex;
        } catch (InternalAuthenticationServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal,
                                                         Authentication authentication, UserDetails user) {
        boolean upgradeEncoding = this.userDetailsPasswordService != null
                && this.passwordEncoder.upgradeEncoding(user.getPassword());
        if (upgradeEncoding) {
            String presentedPassword = authentication.getCredentials().toString();
            String newPassword = this.passwordEncoder.encode(presentedPassword);
            user = this.userDetailsPasswordService.updatePassword(user, newPassword);
        }
        return super.createSuccessAuthentication(principal, authentication, user);
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
        }
    }

    private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }
    }

    /**
     * Sets the PasswordEncoder instance to be used to encode and validate passwords. If
     * not set, the password will be compared using {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}
     *
     * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder}
     *                        types.
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setUserDetailsService(CustomOauthUserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    protected CustomOauthUserDetailService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsPasswordService(
            UserDetailsPasswordService userDetailsPasswordService) {
        this.userDetailsPasswordService = userDetailsPasswordService;
    }
}
