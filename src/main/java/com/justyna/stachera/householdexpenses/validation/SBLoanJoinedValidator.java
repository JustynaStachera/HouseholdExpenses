package com.justyna.stachera.householdexpenses.validation;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBKindOfLoanDao;
import com.justyna.stachera.householdexpenses.domain.joined.SBLoanJoined;
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
 * Class which contains all validation for {@link SBLoanJoined} object.
 * It implements {@link Validator} interface.
 */
@Component
@PropertySource("classpath:messages.properties")
public class SBLoanJoinedValidator implements Validator
{
    private SBKindOfLoanDao kindOfLoanDao;
    private Environment environment;
    
    /**
     * Argument constructor.
     *
     * @param kindOfLoanDao It provides methods related with 'sbkind_of_loan' table from database.
     * @param environment   It provides methods to get the error messages from messages.properties file.
     */
    @Autowired
    public SBLoanJoinedValidator(SBKindOfLoanDao kindOfLoanDao, Environment environment)
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
        return SBLoanJoinedValidator.class.isAssignableFrom(clazz);
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
        SBLoanJoined loanJoined = (SBLoanJoined) o;
        
        if (isInitialAmountNull(loanJoined))
        {
            errors.rejectValue("initialAmount", "SBLoanJoined.initialAmount.nullMismatch",
                               environment.getProperty("SBLoanJoined.initialAmount.nullMismatch"));
        }
        else if (!isInitialAmountMin(loanJoined))
        {
            errors.rejectValue("initialAmount", "SBLoanJoined.initialAmount.minMismatch",
                               environment.getProperty("SBLoanJoined.initialAmount.minMismatch"));
        }
        
        if (isBeginDateNull(loanJoined))
        {
            errors.rejectValue("beginDate", "SBLoanJoined.beginDate.nullMismatch",
                               environment.getProperty("SBLoanJoined.beginDate.nullMismatch"));
        }
        
        if (isPaidUpMonthsNull(loanJoined))
        {
            errors.rejectValue("paidUpMonths", "SBLoanJoined.paidUpMonths.nullMismatch",
                               environment.getProperty("SBLoanJoined.paidUpMonths.nullMismatch"));
        }
        else if (!isPaidUpMonthsMin(loanJoined))
        {
            errors.rejectValue("paidUpMonths", "SBLoanJoined.paidUpMonths.minMismatch",
                               environment.getProperty("SBLoanJoined.paidUpMonths.minMismatch"));
        }
        else if (!isPaidUpMonthsMax(loanJoined))
        {
            errors.rejectValue("paidUpMonths", "SBLoanJoined.paidUpMonths.maxMismatch",
                               environment.getProperty("SBLoanJoined.paidUpMonths.maxMismatch"));
        }
    }
    
    /**
     * It checks if {@link SBLoanJoined} initialAmount is NULL.
     *
     * @param loanJoined Object to check.
     * @return True if initialAmount is NULL, otherwise false.
     */
    private boolean isInitialAmountNull(SBLoanJoined loanJoined)
    {
        return loanJoined.getInitialAmount() == null;
    }
    
    /**
     * It checks if {@link SBLoanJoined} initialAmount is greater than 0.
     *
     * @param loanJoined Object to check.
     * @return True if initialAmount is greater than 0, otherwise false.
     */
    private boolean isInitialAmountMin(SBLoanJoined loanJoined)
    {
        return loanJoined.getInitialAmount() != null && loanJoined.getInitialAmount() >= 0;
    }
    
    /**
     * It checks if {@link SBLoanJoined} beginDate is NULL.
     *
     * @param loanJoined Object to check.
     * @return True if beginDate is NULL, otherwise false.
     */
    private boolean isBeginDateNull(SBLoanJoined loanJoined)
    {
        return loanJoined.getBeginDate() == null;
    }
    
    /**
     * It checks if {@link SBLoanJoined} paidUpMonths is NULL.
     *
     * @param loanJoined Object to check.
     * @return True if paidUpMonths is NULL, otherwise false.
     */
    private boolean isPaidUpMonthsNull(SBLoanJoined loanJoined)
    {
        return loanJoined.getPaidUpMonths() == null;
    }
    
    /**
     * It checks if {@link SBLoanJoined} paidUpMonths is greater than 0.
     *
     * @param loanJoined Object to check.
     * @return True if paidUpMonths is greater than 0, otherwise false.
     */
    private boolean isPaidUpMonthsMin(SBLoanJoined loanJoined)
    {
        return loanJoined.getPaidUpMonths() != null && loanJoined.getPaidUpMonths() >= 0;
    }
    
    /**
     * It checks if {@link SBLoanJoined} paidUpMonths is equal or less than durationTime.
     *
     * @param loanJoined Object to check.
     * @return True if paidUpMonths is equal or less than durationTime, otherwise false.
     */
    private boolean isPaidUpMonthsMax(SBLoanJoined loanJoined)
    {
        return loanJoined.getPaidUpMonths() != null &&
               loanJoined.getPaidUpMonths() <= kindOfLoanDao.getOne(loanJoined.getKindOfLoanId()).getDurationTime();
    }
}
