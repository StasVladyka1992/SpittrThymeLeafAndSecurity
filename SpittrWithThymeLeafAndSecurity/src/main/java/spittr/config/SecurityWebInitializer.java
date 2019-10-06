package spittr.config;

import org.springframework.security.web.context.
        AbstractSecurityWebApplicationInitializer;
// to extend AbstractSecurityWebApplicationInitializer.class i need spring-security-web dependency

// This class is needed in order to discover DelegatingFilterProxy(servlet context) - a special servlet
// that delegates to an implementation of javax.servlet.Filter that is registered as a <bean>
// in SPRING APPLICATION CONTEXT. This servlet can be also added with the help of web.xml:
//<filter>
//<filter-name>springSecurityFilterChain</filter-name>
//<filter-class>
//        org.springframework.web.filter.DelegatingFilterProxy
//</filter-class>
//</filter>

// The most important thing here is that the <filter-name> be set to springSecurity-
// FilterChain. That’s because you’ll soon be configuring Spring Security for web security,
// and there will be a filter bean named springSecurityFilterChain that
// DelegatingFilterProxy will need to delegate to.

// AbstractSecurityWebApplicationInitializer implements WebApplication-
// Initializer, so it will be discovered by Spring and be used to register Delegating-
// FilterProxy with the web container. Although you can override its appendFilters()
// or insertFilters() methods to register filters of your own choosing, you need not
// override anything to register DelegatingFilterProxy.

// As for the springSecurityFilterChain bean itself, it’s another special filter
// known as FilterChainProxy. It’s a single filter that chains together one or more additional
// filters. Spring Security relies on several servlet filters to provide different security
// features, but you should almost never need to know these details

public class SecurityWebInitializer
        extends AbstractSecurityWebApplicationInitializer {
}
