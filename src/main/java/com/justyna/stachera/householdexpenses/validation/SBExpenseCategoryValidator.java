package com.justyna.stachera.householdexpenses.validation;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBExpenseCategoryDao;
import com.justyna.stachera.householdexpenses.domain.main.SBExpenseCategory;
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
 * Class which contains all validation for {@link SBExpenseCategory} object.
 * It implements {@link Validator} interface.
 */
@Component
@PropertySource("classpath:messages.properties")
public class SBExpenseCategoryValidator implements Validator
{
    private SBExpenseCategoryDao expenseCategoryDao;
    private Environment environment;
    
    /**
     * Argument constructor.
     *
     * @param expenseCategoryDao It provides methods related with 'sbexpense_category' table from database.
     * @param environment        It provides methods to get the error messages from messages.properties file.
     */
    @Autowired
    public SBExpenseCategoryValidator(SBExpenseCategoryDao expenseCategoryDao, Environment environment)
    {
        this.expenseCategoryDao = expenseCategoryDao;
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
        return SBExpenseCategory.class.isAssignableFrom(clazz);
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
        SBExpenseCategory expenseCategory = (SBExpenseCategory) o;
        
        if (isNameEmpty(expenseCategory))
        {
            errors.rejectValue("name", "emptyMismatch.name", environment.getProperty("emptyMismatch.name"));
        }
        
        if (!isNameUnique(expenseCategory))
        {
            errors.rejectValue("name", "uniqueMismatch.name", environment.getProperty("uniqueMismatch.name"));
        }
    }
    
    /**
     * It checks if {@link SBExpenseCategory} name field is empty.
     *
     * @param expenseCategory Object to check.
     * @return True if name is empty, otherwise true.
     */
    private boolean isNameEmpty(SBExpenseCategory expenseCategory)
    {
        return expenseCategory.getName().isEmpty();
    }
    
    /**
     * It checks if {@link SBExpenseCategory} name field is unique.
     *
     * @param expenseCategory Object to check.
     * @return True if name is unique, otherwise true.
     */
    private boolean isNameUnique(SBExpenseCategory expenseCategory)
    {
        List<SBExpenseCategory> expenseCategories = expenseCategoryDao.findAll();
        List<String> expenseCategoryNames = expenseCategories.stream().map(SBExpenseCategory::getName).collect(
                Collectors.toList());
        Optional expenseCategoryOptional = expenseCategories.stream()
                                                            .filter(b -> b.getId().equals(expenseCategory.getId()) &&
                                                                         b.getName().equals(expenseCategory.getName()))
                                                            .findFirst();
        
        return expenseCategoryOptional.isPresent() ||
               !expenseCategoryNames.stream().anyMatch(expenseCategory.getName()::equalsIgnoreCase);
    }
}