package com.justyna.stachera.householdexpenses.validation;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBKindOfLoanDao;
import com.justyna.stachera.householdexpenses.domain.joined.SBKindOfLoanJoined;
import com.justyna.stachera.householdexpenses.domain.main.SBKindOfLoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 * <p>
 * Class which contains all validation for {@link SBKindOfLoanJoined} object.
 * It implements {@link Validator} interface.
 */
@Component
@PropertySource("classpath:messages.properties")
public class SBKindOfLoanJoinedValidator implements Validator
{
    private SBKindOfLoanDao kindOfLoanDao;
    private Environment environment;

    @Autowired
    public SBKindOfLoanJoinedValidator(SBKindOfLoanDao kindOfLoanDao, Environment environment)
    {
        this.kindOfLoanDao = kindOfLoanDao;
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
        return SBKindOfLoan.class.isAssignableFrom(clazz);
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
        SBKindOfLoanJoined kindOfLoanJoined = (SBKindOfLoanJoined) o;

        if (isNameEmpty(kindOfLoanJoined))
        {
            errors.rejectValue("name", "emptyMismatch.name", environment.getProperty("emptyMismatch.name"));
        }

        if (!isNameUnique(kindOfLoanJoined))
        {
            errors.rejectValue("name", "uniqueMismatch.name", environment.getProperty("uniqueMismatch.name"));
        }

        if (isPercentNull(kindOfLoanJoined))
        {
            errors.rejectValue("percent", "nullMismatch.percent", environment.getProperty("nullMismatch.percent"));
        }

        if (!isPercentNull(kindOfLoanJoined) && !isPercentValid(kindOfLoanJoined))
        {
            errors.rejectValue("percent", "rangeMismatch.percent", environment.getProperty("rangeMismatch.percent"));
        }

        if (isDurationTimeNull(kindOfLoanJoined))
        {
            errors.rejectValue("durationTime", "SBKindOfLoanJoined.durationTime.nullMismatch", environment.getProperty("SBKindOfLoanJoined.durationTime.nullMismatch"));
        }

        if (!isDurationTimeNull(kindOfLoanJoined) && !isDurationTimeValid(kindOfLoanJoined))
        {
            errors.rejectValue("durationTime", "SBKindOfLoanJoined.durationTime.rangeMismatch", environment.getProperty("SBKindOfLoanJoined.durationTime.rangeMismatch"));
        }

        if (isCapitalisationIdNull(kindOfLoanJoined))
        {
            errors.rejectValue("capitalisationId", "defaultNullMismatch", environment.getProperty("defaultNullMismatch"));
        }

        if (isBankIdNull(kindOfLoanJoined))
        {
            errors.rejectValue("bankId", "defaultNullMismatch", environment.getProperty("defaultNullMismatch"));
        }
    }

    /**
     * It checks if {@link SBKindOfLoanJoined} name field is empty.
     *
     * @param kindOfLoanJoined Object to check.
     * @return True if name field is empty, otherwise false.
     */
    private boolean isNameEmpty(SBKindOfLoanJoined kindOfLoanJoined)
    {
        return kindOfLoanJoined.getName().isEmpty();
    }

    /**
     * It checks if {@link SBKindOfLoanJoined} name field is unique.
     *
     * @param kindOfLoanJoined Object to check.
     * @return True if name field is unique, otherwise false.
     */
    private boolean isNameUnique(SBKindOfLoanJoined kindOfLoanJoined)
    {
        return kindOfLoanDao.findByName(kindOfLoanJoined.getName()) == null;
    }

    /**
     * It checks if {@link SBKindOfLoanJoined} percent field is NULL.
     *
     * @param kindOfLoanJoined Object to check.
     * @return True if percent field is NULL, otherwise false.
     */
    private boolean isPercentNull(SBKindOfLoanJoined kindOfLoanJoined)
    {
        return kindOfLoanJoined.getPercent() == null;
    }

    /**
     * It checks if {@link SBKindOfLoanJoined} percent field is valid.
     *
     * @param kindOfLoanJoined Object to check.
     * @return True if percent field is valid, otherwise false.
     */
    private boolean isPercentValid(SBKindOfLoanJoined kindOfLoanJoined)
    {
        return kindOfLoanJoined.getPercent().doubleValue() >= 0.0 &&
               kindOfLoanJoined.getPercent().doubleValue() <= 100.0;
    }

    /**
     * It checks if {@link SBKindOfLoanJoined} durationTime field is valid.
     *
     * @param kindOfLoanJoined Object to check.
     * @return True if durationTime field is valid, otherwise false.
     */
    private boolean isDurationTimeValid(SBKindOfLoanJoined kindOfLoanJoined)
    {
        return kindOfLoanJoined.getDurationTime() >= 2;
    }

    /**
     * It checks if {@link SBKindOfLoanJoined} durationTime field is NULL.
     *
     * @param kindOfLoanJoined Object to check.
     * @return True if durationTime field is NULL, otherwise false.
     */
    private boolean isDurationTimeNull(SBKindOfLoanJoined kindOfLoanJoined)
    {
        return kindOfLoanJoined.getDurationTime() == null;
    }

    /**
     * It checks if {@link SBKindOfLoanJoined} capitalisationId is NULL.
     *
     * @param kindOfLoanJoined Object to check.
     * @return True if capitalisationId is NULL, otherwise false.
     */
    private boolean isCapitalisationIdNull(SBKindOfLoanJoined kindOfLoanJoined)
    {
        return kindOfLoanJoined.getCapitalisationId() == null;
    }

    /**
     * It checks if {@link SBKindOfLoanJoined} bankId is NULL.
     *
     * @param kindOfLoanJoined Object to check.
     * @return True if bankId is NULL, otherwise false.
     */
    private boolean isBankIdNull(SBKindOfLoanJoined kindOfLoanJoined)
    {
        return kindOfLoanJoined.getBankId() == null;
    }
}