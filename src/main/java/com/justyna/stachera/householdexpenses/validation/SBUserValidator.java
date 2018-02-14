package com.justyna.stachera.householdexpenses.validation;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBUserDao;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 * <p>
 * Class which contains all validation for {@link SBUser} object.
 * It implements {@link Validator} interface.
 */
@Component
@PropertySource("classpath:messages.properties")
public class SBUserValidator implements Validator
{
    private SBUserDao userDao;
    private Environment environment;

    /**
     * Argument constructor.
     *
     * @param userDao     It provides methods related with 'sbuser' table from database.
     * @param environment It provides methods to get the error messages from messages.properties file.
     */
    @Autowired
    public SBUserValidator(SBUserDao userDao, Environment environment)
    {
        this.userDao = userDao;
        this.environment = environment;
    }

    /**
     * It checks if class to check equals {@link Validator} class type.
     *
     * @param clazz Class to check.
     * @return True if class to check equals {@link Validator} class type, otherwise false.
     */
    @Override
    public boolean supports(Class<?> clazz)
    {
        return SBUser.class.isAssignableFrom(clazz);
    }

    /**
     * It validates the {@link Object} fields.
     *
     * @param o      {@link Object} to validate.
     * @param errors It provides an error field list.
     */
    @Override
    public void validate(Object o, Errors errors)
    {
        SBUser user = (SBUser) o;

        if (!isUsernameValid(user))
        {
            errors.rejectValue("username",
                               "SBUser.username.regexMismatch",
                               environment.getProperty("SBUser.username.regexMismatch"));
        }

        if (!isUsernameUnique(user))
        {
            errors.rejectValue("username",
                               "SBUser.username.uniqueMismatch",
                               environment.getProperty("SBUser.username.uniqueMismatch"));
        }

        if (!isPasswordValid(user))
        {
            errors.rejectValue("password",
                               "SBUser.password.regexMismatch",
                               environment.getProperty("SBUser.password.regexMismatch"));
        }

        if (!isPasswordMatch(user))
        {
            errors.rejectValue("password",
                               "SBUser.confirmPassword.matchMismatch",
                               environment.getProperty("SBUser.confirmPassword.matchMismatch"));
        }

        if (!isAnyRoleChecked(user))
        {
            errors.rejectValue("isAdmin",
                               "SBUser.isAdmin.checkedMismatch",
                               environment.getProperty("SBUser.isAdmin.checkedMismatch"));
        }
    }

    /**
     * It check if {@link SBUser} username is valid.
     *
     * @param user Object to check.
     * @return True if username is valid, otherwise false.
     */
    private boolean isUsernameValid(SBUser user)
    {
        return Pattern.matches("^[a-z0-9_-]{3,15}$", user.getUsername());
    }

    /**
     * It check if {@link SBUser} username is unique.
     *
     * @param user Object to check.
     * @return True if username is unique, otherwise false.
     */
    private boolean isUsernameUnique(SBUser user)
    {
        List<SBUser> users = userDao.findAll();
        List<String> userUsernames = users.stream().map(SBUser::getUsername).collect(Collectors.toList());
        Optional userOptional = users.stream()
                                     .filter(b -> b.getId().equals(user.getId()) &&
                                                  b.getUsername().equals(user.getUsername()))
                                     .findFirst();

        return userOptional.isPresent() || !userUsernames.stream().anyMatch(user.getUsername()::equalsIgnoreCase);
    }

    /**
     * It check if {@link SBUser} password is valid.
     *
     * @param user Object to check.
     * @return True if password is valid, otherwise false.
     */
    private boolean isPasswordValid(SBUser user)
    {
        return Pattern.matches(".{5,15}", user.getPassword());
    }

    /**
     * It check if {@link SBUser} password matches to confirmPassword.
     *
     * @param user Object to check.
     * @return True if password matches to confirmPassword, otherwise false.
     */
    private boolean isPasswordMatch(SBUser user)
    {
        return user.getPassword().equals(user.getConfirmPassword());
    }

    /**
     * It check if {@link SBUser} any role is checked.
     *
     * @param user Object to check.
     * @return True if any role is checked, otherwise false.
     */
    private boolean isAnyRoleChecked(SBUser user)
    {
        Boolean[] checkboxValues = {user.getIsAdmin(),
                                    user.getIsAddOnly(),
                                    user.getIsReadOnly(),
                                    user.getIsModifyOnly()};

        return Arrays.asList(checkboxValues).contains(true);
    }
}