package spittr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
//To add @EnableWebSecurity i need spring-security-config dependency
//Among other things, the @EnableWebMvcSecurity annotation configures a Spring
//MVC argument resolver so that handler methods can receive the authenticated user’s
//principal (or username) via @AuthenticationPrincipal-annotated parameters. It
//also configures a bean that automatically adds a hidden cross-site request forgery
//(CSRF) token field on forms using Spring’s form-binding tag library.
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //role is shortcut for authorites - Any values
        //given to roles() are prefixed with ROLE_ and assigned as authorities to the user.
        //In effect, the following user configuration is equivalent to that in listing 9.3:
        //auth
        //.inMemoryAuthentication()
        //.withUser("user").password("password")
        //.authorities("ROLE_USER").and()
        //.withUser("admin").password("password")
        //.authorities("ROLE_USER", "ROLE_ADMIN");
        //
        // If there is an error: There is no PasswordEncoder mapped for the id "null", then read this
        // https://www.mkyong.com/spring-boot/spring-security-there-is-no-passwordencoder-mapped-for-the-id-null/

        //This is implementation of in-memory authentication. No database driver and configuration isn't required
        auth
                .inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER")
                    .and()
                .withUser("admin").password("{noop}password").roles("USER", "ADMIN");

        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                "select username, password, true from Spitter where username=?")
                .authoritiesByUsernameQuery(
                "select username, 'ROLE_USER' from Spitter where username=?");
    }
}
