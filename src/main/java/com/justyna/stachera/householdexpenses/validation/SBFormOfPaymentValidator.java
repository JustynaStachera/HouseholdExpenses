package com.justyna.stachera.householdexpenses.validation;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBFormOfPaymentDao;
import com.justyna.stachera.householdexpenses.domain.main.SBFormOfPayment;
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
 * Class which contains all validation for {@link SBFormOfPayment} object.
 * It implements {@link Validator} interface.
 */
@Component
@PropertySource("classpath:messages.properties")
public class SBFormOfPaymentValidator implements Validator
{
    private SBFormOfPaymentDao formOfPaymentDao;
    private Environment environment;
    
    /**
     * Argument constructor.
     *
     * @param formOfPaymentDao It provides methods related with 'sbform_of_payment' table from database.
     * @param environment      It provides methods to get the error messages from messages.properties file.
     */
    @Autowired
    public SBFormOfPaymentValidator(
            SBFormOfPaymentDao formOfPaymentDao, Environment environment)
    {
        this.formOfPaymentDao = formOfPaymentDao;
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
        return SBFormOfPayment.class.isAssignableFrom(clazz);
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
        SBFormOfPayment formOfPayment = (SBFormOfPayment) o;
        
        if (isEmpty(formOfPayment))
        {
            errors.rejectValue("name", "emptyMismatch.name", environment.getProperty("emptyMismatch.name"));
        }
        
        if (!isNameUnique(formOfPayment))
        {
            errors.rejectValue("name", "uniqueMismatch.name", environment.getProperty("uniqueMismatch.name"));
        }
    }
    
    /**
     * It checks if {@link SBFormOfPayment} name field is empty.
     *
     * @param formOfPayment Object to check.
     * @return True if name field is empty, otherwise false.
     */
    private boolean isEmpty(SBFormOfPayment formOfPayment)
    {
        return formOfPayment.getName().isEmpty();
    }
    
    /**
     * It checks if {@link SBFormOfPayment} name field is unique.
     *
     * @param formOfPayment Object to check.
     * @return True if name field is unique, otherwise false.
     */
    private boolean isNameUnique(SBFormOfPayment formOfPayment)
    {
        List<SBFormOfPayment> formsOfPayment = formOfPaymentDao.findAll();
        List<String> formOfPaymentNames = formsOfPayment.stream().map(SBFormOfPayment::getName).collect(
                Collectors.toList());
        Optional formOfPaymentOptional = formsOfPayment.stream()
                                                       .filter(b -> b.getId().equals(formOfPayment.getId()) &&
                                                                    b.getName().equals(formOfPayment.getName()))
                                                       .findFirst();
        
        return formOfPaymentOptional.isPresent() ||
               !formOfPaymentNames.stream().anyMatch(formOfPayment.getName()::equalsIgnoreCase);
    }
}