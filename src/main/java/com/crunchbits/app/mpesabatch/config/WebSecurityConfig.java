package com.crunchbits.app.mpesabatch.config;

 

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
 

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Value("${ldap.urls}")
	private String ldapUrls;
	@Value("${ldap.base.dn}")
	private String ldapBaseDn;
	@Value("${ldap.username}")
	private String ldapSecurityPrincipal;
	@Value("${ldap.password}")
	private String ldapPrincipalPassword;
	@Value("${ldap.user.dn.pattern}")
	private String ldapUserDnPattern;
	@Value("${ldap.enabled}")
	private String ldapEnabled;
	
	
 
    
    @Override
	protected void configure(HttpSecurity http) throws Exception {
    	http
    	   .authorizeRequests()
    	   .antMatchers("/login**").permitAll()
    	    .antMatchers("/profile/**").fullyAuthenticated()
    	   .antMatchers("/").permitAll()
    	    .and()
    	   .formLogin()
    	   .loginPage("/login")
    	   .failureUrl("/login?error")
    	   .permitAll()
    	   .and()
    	  .logout()
    	  .invalidateHttpSession(true)
    	.deleteCookies("JSESSIONID")
    	.permitAll();
    	
 
	}

    /*
     * (non-Javadoc)
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
     
     *$ldap_svr = "172.28.229.103"; #prod
$ldap_port = "389";
$dn = "ou=safaricom departments,dc=safaricom,dc=net";
     */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception { 
		
		if(Boolean.parseBoolean(ldapEnabled)){
			auth
			.ldapAuthentication()
			.contextSource()
			.url(ldapUrls + ldapBaseDn)
			//.managerDn(ldapSecurityPrincipal)
			//.managerPassword(ldapPrincipalPassword)
			.and()
			.userDnPatterns(ldapUserDnPattern);
			} else {
			auth
			.inMemoryAuthentication()
			.withUser("user").password("password").roles("USER")
			.and()
			.withUser("admin").password("admin").roles("ADMIN");
			}
		}
	

}
