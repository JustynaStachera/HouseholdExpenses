package com.justyna.stachera.householdexpenses.validation;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBKindOfOperationDao;
import com.justyna.stachera.householdexpenses.domain.main.SBKindOfOperation;
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
 * Class which contains all validation for {@link SBKindOfOperation} object.
 * It implements {@link Validator} interface.
 */
@Component
@PropertySource("classpath:messages.properties")
public class SBKindOfOperationValidator implements Validator
{
    private SBKindOfOperationDao kindOfOperationDao;
    private Environment environment;

    /**
     * Argument constructor.
     *
     * @param kindOfOperationDao It provides methods related with 'sbkind_of_operation' table from database.
     * @param environment        It provides methods to get the error messages from messages.properties file.
     */
    @Autowired
    public SBKindOfOperationValidator(
            SBKindOfOperationDao kindOfOperationDao, Environment environment)
    {
        this.kindOfOperationDao = kindOfOperationDao;
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
        return SBKindOfOperation.class.isAssignableFrom(clazz);
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
        SBKindOfOperation kindOfOperation = (SBKindOfOperation) o;

        if (isNameEmpty(kindOfOperation))
        {
            errors.rejectValue("name", "emptyMismatch.name", environment.getProperty("emptyMismatch.name"));
        }

        if (!isNameUnique(kindOfOperation))
        {
            errors.rejectValue("name", "uniqueMismatch.name", environment.getProperty("uniqueMismatch.name"));
        }
    }

    /**
     * It checks if {@link SBKindOfOperation} name field is empty.
     *
     * @param kindOfOperation Object to check.
     * @return True if name field is empty, otherwise false.
     */
    private boolean isNameEmpty(SBKindOfOperation kindOfOperation)
    {
        return kindOfOperation.getName().isEmpty();
    }

    /**
     * It checks if {@link SBKindOfOperation} name field is unique.
     *
     * @param kindOfOperation Object to check.
     * @return True if name field is unique, otherwise false.
     */
    private boolean isNameUnique(SBKindOfOperation kindOfOperation)
    {
        List<SBKindOfOperation> kindsOfOperation = kindOfOperationDao.findAll();
        List<String> kindOfOperationNames = kindsOfOperation.stream().map(SBKindOfOperation::getName).collect(
                Collectors.toList());
        Optional kindOfOperationOptional = kindsOfOperation.stream()
                                                           .filter(b -> b.getId().equals(kindOfOperation.getId()) &&
                                                                        b.getName().equals(kindOfOperation.getName()))
                                                           .findFirst();

        return kindOfOperationOptional.isPresent() ||
               !kindOfOperationNames.stream().anyMatch(kindOfOperation.getName()::equalsIgnoreCase);
    }
}