package com.justyna.stachera.householdexpenses.validation;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBCapitalisationDao;
import com.justyna.stachera.householdexpenses.domain.main.SBCapitalisation;
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
 * Class which contains all validation for {@link SBCapitalisation} object.
 * It implements {@link Validator} interface.
 */
@Component
@PropertySource("classpath:messages.properties")
public class SBCapitalisationValidator implements Validator
{
    private SBCapitalisationDao capitalisationDao;
    private Environment environment;

    /**
     * Argument constructor.
     *
     * @param capitalisationDao It provides methods related with 'sbcapitalisation' table from database.
     * @param environment       It provides methods to get the error messages from messages.properties file.
     */
    @Autowired
    public SBCapitalisationValidator(
            SBCapitalisationDao capitalisationDao, Environment environment)
    {
        this.capitalisationDao = capitalisationDao;
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
        return SBCapitalisation.class.isAssignableFrom(clazz);
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
        SBCapitalisation capitalisation = (SBCapitalisation) o;

        if (isNameEmpty(capitalisation))
        {
            errors.rejectValue("name", "emptyMismatch.name", environment.getProperty("emptyMismatch.name"));
        }

        if (!isNameUnique(capitalisation))
        {
            errors.rejectValue("name", "uniqueMismatch.name", environment.getProperty("uniqueMismatch.name"));
        }
    }

    /**
     * It checks if {@link SBCapitalisation} name field is empty.
     *
     * @param capitalisation Object to check.
     * @return True if name is empty, otherwise true.
     */
    private boolean isNameEmpty(SBCapitalisation capitalisation)
    {
        return capitalisation.getName().isEmpty();
    }

    /**
     * It checks if {@link SBCapitalisation} name field is unique.
     *
     * @param capitalisation Object to check.
     * @return True if name is unique, otherwise true.
     */
    private boolean isNameUnique(SBCapitalisation capitalisation)
    {
        List<SBCapitalisation> capitalisations = capitalisationDao.findAll();
        List<String> capitalisationNames = capitalisations.stream().map(SBCapitalisation::getName).collect(
                Collectors.toList());
        Optional capitalisationOptional = capitalisations.stream()
                                                         .filter(b -> b.getId().equals(capitalisation.getId()) &&
                                                                      b.getName().equals(capitalisation.getName()))
                                                         .findFirst();

        return capitalisationOptional.isPresent() ||
               !capitalisationNames.stream().anyMatch(capitalisation.getName()::equalsIgnoreCase);
    }
}