package de.paulstueber.ui.config;

import de.paulstueber.ui.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.data.mongo.JdkMongoSessionConverter;
import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession;

/**
 * Spring security configuration. The heart of all settings to handle requests of authenticated and guest users
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableMongoHttpSession(maxInactiveIntervalInSeconds = 86400)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${config.init.user.name}")
    private String initUserName;
    @Value("${config.init.user.password}")
    private String initUserPassword;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/api/scaler/**")
                        .hasIpAddress("127.0.0.1")
                    .antMatchers("/", "/css/**", "/js/**", "/img/**", "/v/**", "/monitor/health/**")
                        .permitAll()
                    .antMatchers("/admin/**").hasAnyRole(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .failureUrl("/login?error")
                    .usernameParameter("email")
                    .and()
                .logout()
                    .permitAll()
                    .logoutUrl("/logout")
                    .deleteCookies("remember-me")
                    .logoutSuccessUrl("/index")
                    .and()
                .rememberMe();
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser(initUserName).password(initUserPassword).roles(Role.SUPER_ADMIN.name());

        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public JdkMongoSessionConverter jdkMongoSessionConverter() {
        return new JdkMongoSessionConverter();
    }
}
