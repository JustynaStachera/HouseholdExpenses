package com.justyna.stachera.householdexpenses.security.configuration;

import com.justyna.stachera.householdexpenses.security.service.SBUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * {@link Configuration} class. It extends {@link WebSecurityConfigurerAdapter} and checks the user access rights.
 */
@Configuration
@EnableWebSecurity
public class SBWebSecurityConfiguration extends WebSecurityConfigurerAdapter
{
    private SBUserDetailsService userDetailsService;

    /**
     * Argument constructor.
     *
     * @param userDetailsService User details service.
     */
    @Autowired
    public SBWebSecurityConfiguration(SBUserDetailsService userDetailsService)
    {
        this.userDetailsService = userDetailsService;
    }

    /**
     * It provides global configurations.
     *
     * @param authenticationManagerBuilder Authentication manager builder.
     * @throws Exception It throws exception if method fails.
     */
    @Autowired
    public void configurationGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception
    {
        authenticationManagerBuilder.userDetailsService(userDetailsService);
    }

    /**
     * It sets the access rights for users depending on the role.
     *
     * @param httpSecurity It configures access rights for users.
     * @throws Exception It throws exception if method fails.
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity
                .authorizeRequests()
                // Help
                .antMatchers("/help").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER_ARM", "USER_AR", "USER_AM", "USER_A", "USER_RM", "USER_R")
                // Expenses
                .antMatchers("/expenses/selectAll").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER_ARM", "USER_AR", "USER_AM", "USER_A", "USER_RM", "USER_R")
                .antMatchers("/expenses/add").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER_ARM", "USER_AR", "USER_AM", "USER_A")
                .antMatchers("/expenses/modify/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER_ARM", "USER_AM", "USER_RM")
                .antMatchers("/expenses/remove/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER_ARM", "USER_AM", "USER_RM")
                // Loans
                .antMatchers("/loans/selectAll").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER_ARM", "USER_AR", "USER_AM", "USER_A", "USER_RM", "USER_R")
                .antMatchers("/loans/add").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER_ARM", "USER_AR", "USER_AM", "USER_A")
                .antMatchers("/loans/modify/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER_ARM", "USER_AM", "USER_RM")
                .antMatchers("/loans/remove/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER_ARM", "USER_AM", "USER_RM")
                // Taxes
                .antMatchers("/taxes/selectAll").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER_ARM", "USER_AR", "USER_AM", "USER_A", "USER_RM", "USER_R")
                .antMatchers("/taxes/add").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER_ARM", "USER_AR", "USER_AM", "USER_A")
                .antMatchers("/taxes/modify/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER_ARM", "USER_AM", "USER_RM")
                .antMatchers("/taxes/remove/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER_ARM", "USER_AM", "USER_RM")
                // Statistics
                .antMatchers("/statistics/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER_ARM", "USER_AR", "USER_RM", "USER_R")
                // Edit
                .antMatchers("/edit/**").hasAnyRole("SUPER_ADMIN", "ADMIN")
                // Registration
                .antMatchers("/registration").permitAll()
                // Others
                .antMatchers("/css/**", "/js/**", "/img/**").permitAll().anyRequest().permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/login").successForwardUrl("/help")
                .and()
                .logout().permitAll().logoutUrl("/logout").clearAuthentication(true).logoutSuccessUrl("/logout")
                .and()
                .httpBasic();
    }
}
