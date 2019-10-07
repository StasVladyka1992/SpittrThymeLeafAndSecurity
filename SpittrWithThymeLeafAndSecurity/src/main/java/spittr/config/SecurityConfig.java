package spittr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

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
      //basic config, default config
        // THERE IS ONE TRICKY MOMENT: By default Spring Security enables CSRF,
        // so in order to send post requests i need to disable csrf protection.
        // https://stackoverflow.com/questions/52449496/403-forbidden-when-i-try-to-post-to-my-spring-api

//        You can chain as many calls to antMatchers(), regexMatchers(),
//        and anyRequest()as  you  need  to  fully  establish
//        the  security rules around your web  application.
//        You should know, however, that they’ll be applied in the order given.
//        For that reason, it’simportant to configure the most specific request path patterns first
//        and the least spe-cific ones (such as anyRequest()) last. If not, then the least specific
//        paths will trump the more specific ones.

//        Most of the methods in table 9.4 are one-dimensional.
//        That is, you can use hasRole()to require a certain role,
//        but you can’t also use hasIpAddress() to require a specificIP address on the same path. In that case use SpEL

//        Spring  Security  implements  CSRF  protection  with  a  synchronizer  token.  State-changing requests
//        (for example, any request that is not GET, HEAD, OPTIONS, or TRACE)will be intercepted and checked
//        for a CSRF token. If the request doesn’t carry a CSRFtoken,
//        or if the token doesn’t match the token on the server, the request will fail witha CsrfException.
        http.csrf().disable().authorizeRequests()
                .anyRequest().permitAll().and()
                .requiresChannel().antMatchers("/spitter/**").requiresInsecure()
//        https://www.logicbig.com/tutorials/spring-framework/spring-security/https-security.html - how to enable https
                .antMatchers(HttpMethod.GET, "/spittles").requiresSecure().and()
                .formLogin();
//    }

//        The path given to antMatchers() supports Ant-style wildcarding. Although we’re
//        not using it here, you could specify a path with a wildcard like on the page 261 (Spring in action 4)
//        .antMatchers("/spitters/**").authenticated();
//        .regexMatchers("/spitters/.*").authenticated();
//        to see full list of available methods of security config check p.262 of spring in action

    //it is useful, because without it htttp
    //maps the port 8080(http) to 8443(https)
        http
                .portMapper()
                .http(8080).mapsTo(8443);

//        http.authorizeRequests()
    //means that only users with role 'ROLE_SPITTER' are able to register
//                .antMatchers("/spitter/register").authenticated()
//                .antMatchers("/spittles").authenticated()
//                .anyRequest().permitAll().and().formLogin();
//                .requiresChannel().antMatchers("/spitter/register").requiresSecure()
//                .antMatchers("/").requiresInsecure();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //role is shortcut for authorites - Any values
        //given to roles() are prefixed with ROLE_ and assigned as authorities to the user.
        //In effect, the following user configuration is equivalent to that in listing 9.3:
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("password")
//                .authorities("ROLE_USER").and()
//                .withUser("admin").password("password")
//                .authorities("ROLE_USER", "ROLE_ADMIN");
        //
        // If there is an error: There is no PasswordEncoder mapped for the id "null", then read this
        // https://www.mkyong.com/spring-boot/spring-security-there-is-no-passwordencoder-mapped-for-the-id-null/

        //This is implementation of in-memory authentication. No database driver and configuration isn't required
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("{noop}password").roles("USER").and()
//                .withUser("admin").password("{noop}password").roles("USER", "ADMIN");

        //Relation db authentication:
        // in order to do this i need to configure a datasource and create a certain schema in db. Then Spring will invoke\
        // several methods to implement security

//        IMPORTANT!!!!!
//        When replacing the default SQL queries with those of your own design, it’s important to adhere to the basic contract of the queries. All of them take the username as
//        their only parameter. The authentication query selects the username, password, and
//        enabled status. The authorities query selects zero or more rows containing the username and a granted authority. And the group authorities query selects zero or more
//        rows each with a group ID, group name, and an authority

        auth.jdbcAuthentication().dataSource(dataSource)
        //configuration of my own queries that is used to authenticate users.
        //1) First query is used to authenticate user
                .usersByUsernameQuery(
                "select username, password, true from Spitter where username=?")
        //2) Second query looks up the user's granted authorities granted to a user
                .authoritiesByUsernameQuery(
                "select username, 'ROLE_USER' from Spitter where username=?")
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
        //3) Third query looks may be used to override group authorities
//                .groupAuthoritiesByUsername()
    }
}
