package com.justyna.stachera.householdexpenses.validation;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBKindOfLoanDao;
import com.justyna.stachera.householdexpenses.domain.main.SBKindOfLoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 * <p>
 * Class which contains all validation for {@link SBKindOfLoan} object.
 * It implements {@link Validator} interface.
 */
@Component
@PropertySource("classpath:messages.properties")
public class SBKindOfLoanValidator implements Validator
{
    private SBKindOfLoanDao kindOfLoanDao;
    private Environment environment;

    /**
     * @param kindOfLoanDao It provides methods related with 'sbkind_of_loan' table from database.
     * @param environment   It provides methods to get the error messages from messages.properties file.
     */
    @Autowired
    public SBKindOfLoanValidator(SBKindOfLoanDao kindOfLoanDao, Environment environment)
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
        SBKindOfLoan kindOfLoan = (SBKindOfLoan) o;

        if (isNameEmpty(kindOfLoan))
        {
            errors.rejectValue("name", "emptyMismatch.name", environment.getProperty("emptyMismatch.name"));
        }

        if (!isNameUnique(kindOfLoan))
        {
            errors.rejectValue("name", "uniqueMismatch.name", environment.getProperty("uniqueMismatch.name"));
        }

        if (isPercentNull(kindOfLoan))
        {
            errors.rejectValue("percent", "nullMismatch.percent", environment.getProperty("nullMismatch.percent"));
        }

        if (!isPercentNull(kindOfLoan) && !isPercentValid(kindOfLoan))
        {
            errors.rejectValue("percent", "rangeMismatch.percent", environment.getProperty("rangeMismatch.percent"));
        }

        if (isDurationTimeNull(kindOfLoan))
        {
            errors.rejectValue("durationTime", "SBKindOfLoan.durationTime.nullMismatch",
                               environment.getProperty("SBKindOfLoan.durationTime.nullMismatch"));
        }

        if (!isDurationTimeNull(kindOfLoan) && !isDurationTimeValid(kindOfLoan))
        {
            errors.rejectValue("durationTime", "SBKindOfLoan.durationTime.rangeMismatch",
                               environment.getProperty("SBKindOfLoan.durationTime.rangeMismatch"));
        }

        if (isCapitalisationNull(kindOfLoan))
        {
            errors.rejectValue("capitalisationId", "defaultNullMismatch",
                               environment.getProperty("defaultNullMismatch"));
        }

        if (isBankNull(kindOfLoan))
        {
            errors.rejectValue("bankId", "defaultNullMismatch", environment.getProperty("defaultNullMismatch"));
        }
    }

    /**
     * It checks if {@link SBKindOfLoan} name field is empty.
     *
     * @param kindOfLoan Object to check.
     * @return True if name field is empty, otherwise false.
     */
    private boolean isNameEmpty(SBKindOfLoan kindOfLoan)
    {
        return kindOfLoan.getName().isEmpty();
    }

    /**
     * It checks if {@link SBKindOfLoan} name field is unique.
     *
     * @param kindOfLoan Object to check.
     * @return True if name field is unique, otherwise false.
     */
    private boolean isNameUnique(SBKindOfLoan kindOfLoan)
    {
        List<SBKindOfLoan> kindsOfLoan = kindOfLoanDao.findAll();
        List<String> kindOfLoanNames = kindsOfLoan.stream().map(SBKindOfLoan::getName).collect(Collectors.toList());
        Optional kindOfLoanOptional = kindsOfLoan.stream()
                                                 .filter(b -> b.getId().equals(kindOfLoan.getId()) &&
                                                              b.getName().equals(kindOfLoan.getName()))
                                                 .findFirst();

        return kindOfLoanOptional.isPresent() ||
               !kindOfLoanNames.stream().anyMatch(kindOfLoan.getName()::equalsIgnoreCase);
    }

    /**
     * It checks if {@link SBKindOfLoan} percent field is NULL.
     *
     * @param kindOfLoan Object to check.
     * @return True if percent field is NULL, otherwise false.
     */
    private boolean isPercentNull(SBKindOfLoan kindOfLoan)
    {
        return kindOfLoan.getPercent() == null;
    }

    /**
     * It checks if {@link SBKindOfLoan} percent field is valid.
     *
     * @param kindOfLoan Object to check.
     * @return True if percent field is valid, otherwise false.
     */
    private boolean isPercentValid(SBKindOfLoan kindOfLoan)
    {
        return kindOfLoan.getPercent().doubleValue() >= 0.0 &&
               kindOfLoan.getPercent().doubleValue() <= 100.0;
    }

    /**
     * It checks if {@link SBKindOfLoan} durationTime field is valid.
     *
     * @param kindOfLoan Object to check.
     * @return True if durationTime field is valid, otherwise false.
     */
    private boolean isDurationTimeValid(SBKindOfLoan kindOfLoan)
    {
        return kindOfLoan.getDurationTime() >= 2;
    }

    /**
     * It checks if {@link SBKindOfLoan} durationTime field is NULL.
     *
     * @param kindOfLoan Object to check.
     * @return True if durationTime field is NULL, otherwise false.
     */
    private boolean isDurationTimeNull(SBKindOfLoan kindOfLoan)
    {
        return kindOfLoan.getDurationTime() == null;
    }

    /**
     * It checks if {@link SBKindOfLoan} capitalisation object is NULL.
     *
     * @param kindOfLoan Object to check.
     * @return True if capitalisation object is NULL, otherwise false.
     */
    private boolean isCapitalisationNull(SBKindOfLoan kindOfLoan)
    {
        return kindOfLoan.getCapitalisation() == null;
    }

    /**
     * It checks if {@link SBKindOfLoan} bank object is NULL.
     *
     * @param kindOfLoan Object to check.
     * @return True if bank object is NULL, otherwise false.
     */
    private boolean isBankNull(SBKindOfLoan kindOfLoan)
    {
        return kindOfLoan.getBank() == null;
    }
}