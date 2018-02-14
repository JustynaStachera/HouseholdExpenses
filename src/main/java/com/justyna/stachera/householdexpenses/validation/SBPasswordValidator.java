package com.justyna.stachera.householdexpenses.validation;

import com.justyna.stachera.householdexpenses.domain.helpers.SBPassword;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 * <p>
 * Class which contains all validation for SBPassword object.
 * It implements {@link Validator} interface.
 */
@Component
@PropertySource("classpath:messages.properties")
public class SBPasswordValidator implements Validator
{
    private Environment environment;
    private SBUser user;

    /**
     * Argument constructor.
     *
     * @param environment It provides methods to get the error messages from messages.properties file.
     */
    @Autowired
    public SBPasswordValidator(Environment environment)
    {
        this.environment = environment;
    }

    /**
     * It sets logged in user.
     *
     * @param user Logged in user.
     */
    public void setUser(SBUser user)
    {
        this.user = user;
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
        return SBPassword.class.isAssignableFrom(clazz);
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
        SBPassword password = (SBPassword) o;

        if (!isOldPasswordPatternValid(password))
        {
            errors.rejectValue("oldPassword", "SBPassword.oldPassword.regexMismatch", environment.getProperty("SBPassword.oldPassword.regexMismatch"));
        }

        if (!isNewPasswordPatternValid(password))
        {
            errors.rejectValue("newPassword", "SBPassword.newPassword.regexMismatch", environment.getProperty("SBPassword.newPassword.regexMismatch"));
        }

        if (!isPasswordsEquals(password, user))
        {
            errors.rejectValue("oldPassword", "SBPassword.oldPassword.invalidMismatch", environment.getProperty("SBPassword.oldPassword.invalidMismatch"));
        }

        if (!isPasswordMatch(password))
        {
            errors.rejectValue("confirmPassword", "SBPassword.confirmPassword.matchMismatch", environment.getProperty("SBPassword.confirmPassword.matchMismatch"));
        }
    }

    /**
     * It checks if {@link SBPassword} oldPassword pattern is valid.
     *
     * @param password Old password.
     * @return True if oldPassword pattern is valid, otherwise false.
     */
    private boolean isOldPasswordPatternValid(SBPassword password)
    {
        return Pattern.matches(".{5,15}", password.getNewPassword());
    }

    /**
     * It checks if {@link SBPassword} newPassword pattern is valid.
     *
     * @param password New password.
     * @return True if newPassword pattern is valid, otherwise false.
     */
    private boolean isNewPasswordPatternValid(SBPassword password)
    {
        return Pattern.matches(".{5,15}", password.getNewPassword());
    }

    /**
     * It checks if {@link SBPassword} passwords equal.
     *
     * @param password Password to check.
     * @param user     Logged in user.
     * @return True if oldPassword pattern is valid, otherwise false.
     */
    private boolean isPasswordsEquals(SBPassword password, SBUser user)
    {
        return user.getPassword().equals(password.getOldPassword());
    }

    /**
     * It checks if {@link SBPassword} password is correct.
     * @param password Password to check.
     * @return True if password is correct, otherwise false.
     */
    private boolean isPasswordMatch(SBPassword password)
    {
        return password.getNewPassword().equals(password.getConfirmPassword());
    }
}
