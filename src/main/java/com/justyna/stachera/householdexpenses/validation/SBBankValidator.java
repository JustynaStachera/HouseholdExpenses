package com.justyna.stachera.householdexpenses.validation;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBBankDao;
import com.justyna.stachera.householdexpenses.domain.main.SBBank;
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
 * Class which contains all validation for {@link SBBank} object.
 * It implements {@link Validator} interface.
 */
@Component
@PropertySource("classpath:messages.properties")
public class SBBankValidator implements Validator
{
    private SBBankDao bankDao;
    private Environment environment;

    /**
     * Argument constructor.
     *
     * @param bankDao     It provides methods related with 'sbbank' table from database.
     * @param environment It provides methods to get the error messages from messages.properties file.
     */
    @Autowired
    public SBBankValidator(SBBankDao bankDao, Environment environment)
    {
        this.bankDao = bankDao;
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
        return SBBank.class.isAssignableFrom(clazz);
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
        SBBank bank = (SBBank) o;

        if (isNameEmpty(bank))
        {
            errors.rejectValue("name", "emptyMismatch.name", environment.getProperty("emptyMismatch.name"));
        }

        if (!isNameUnique(bank))
        {
            errors.rejectValue("name", "uniqueMismatch.name", environment.getProperty("uniqueMismatch.name"));
        }
    }

    /**
     * It checks if {@link SBBank} name field is empty.
     *
     * @param bank Object to check.
     * @return True if name is empty, otherwise true.
     */
    private boolean isNameEmpty(SBBank bank)
    {
        return bank.getName().isEmpty();
    }

    /**
     * It checks if {@link SBBank} name field is unique.
     *
     * @param bank Object to check.
     * @return True if name is unique, otherwise true.
     */
    private boolean isNameUnique(SBBank bank)
    {
        List<SBBank> banks = bankDao.findAll();
        List<String> bankNames = banks.stream().map(SBBank::getName).collect(Collectors.toList());
        Optional bankOptional = banks.stream()
                                     .filter(b -> b.getId().equals(bank.getId()) && b.getName().equals(bank.getName()))
                                     .findFirst();

        return bankOptional.isPresent() || !bankNames.stream().anyMatch(bank.getName()::equalsIgnoreCase);
    }
}