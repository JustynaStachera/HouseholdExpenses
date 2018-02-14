package com.justyna.stachera.householdexpenses.security.service;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBUserDao;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Service class. It implements UserDetailsService and logs in user.
 */
@Service
public class SBUserDetailsService implements UserDetailsService
{
    private SBUserDao userDao;

    /**
     * Argument constructors.
     *
     * @param userDao It provides methods related with 'sbuser' table from database.
     */
    @Autowired
    public SBUserDetailsService(SBUserDao userDao)
    {
        this.userDao = userDao;
    }

    /**
     * It returns User object if user exists.
     *
     * @param username Username.
     * @return User object containing user data.
     * @throws UsernameNotFoundException It throws if user doesn't exists.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        SBUser user = userDao.findByUsername(username);

        if (user == null)
        {
            throw new NullPointerException("Nie znaleziono użytkownika!");
        }

        return new User(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                getAuthorities(user.getRole())
        );
    }

    /**
     * It returns user authorities.
     *
     * @param role User role.
     * @return User authorities as Collection.
     */
    private Collection<? extends GrantedAuthority> getAuthorities(String role)
    {
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }
}
