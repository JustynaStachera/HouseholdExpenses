package com.justyna.stachera.householdexpenses.validation;

import com.justyna.stachera.householdexpenses.domain.main.SBLoan;
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
 * Class which contains all validation for {@link SBLoan} object.
 * It implements {@link Validator} interface.
 */
@Component
@PropertySource("classpath:messages.properties")
public class SBLoanValidator implements Validator
{
    private Environment environment;

    /**
     * Argument constructor.
     *
     * @param environment It provides methods to get the error messages from messages.properties file.
     */
    @Autowired
    public SBLoanValidator(Environment environment)
    {
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
        return SBLoan.class.isAssignableFrom(clazz);
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
        SBLoan loan = (SBLoan) o;

        if (isInitialAmountNull(loan))
        {
            errors.rejectValue("initialAmount", "SBLoan.initialAmount.nullMismatch",
                               environment.getProperty("SBLoan.initialAmount.nullMismatch"));
        }
        else if (!isInitialAmountMin(loan))
        {
            errors.rejectValue("initialAmount", "SBLoan.initialAmount.minMismatch",
                               environment.getProperty("SBLoan.initialAmount.minMismatch"));
        }

        if (isBeginDateNull(loan))
        {
            errors.rejectValue("beginDate", "SBLoan.beginDate.nullMismatch",
                               environment.getProperty("SBLoan.beginDate.nullMismatch"));
        }

        if (isPaidUpMonthsNull(loan))
        {
            errors.rejectValue("paidUpMonths", "SBLoan.paidUpMonths.nullMismatch",
                               environment.getProperty("SBLoan.paidUpMonths.nullMismatch"));
        }
        else if (!isPaidUpMonthsMin(loan))
        {
            errors.rejectValue("paidUpMonths", "SBLoan.paidUpMonths.minMismatch",
                               environment.getProperty("SBLoan.paidUpMonths.minMismatch"));
        }
        else if (!isPaidUpMonthsMax(loan))
        {
            errors.rejectValue("paidUpMonths", "SBLoan.paidUpMonths.maxMismatch",
                               environment.getProperty("SBLoan.paidUpMonths.maxMismatch"));
        }
    }

    /**
     * It checks if {@link SBLoan} initialAmount is NULL.
     *
     * @param loan Object to check.
     * @return True if initialAmount is NULL, otherwise false.
     */
    private boolean isInitialAmountNull(SBLoan loan)
    {
        return loan.getInitialAmount() == null;
    }

    /**
     * It checks if {@link SBLoan} initialAmount is greater than 0.
     *
     * @param loan Object to check.
     * @return True if initialAmount is greater than 0, otherwise false.
     */
    private boolean isInitialAmountMin(SBLoan loan)
    {
        return loan.getInitialAmount() != null && loan.getInitialAmount() >= 0;
    }

    /**
     * It checks if {@link SBLoan} beginDate is NULL.
     *
     * @param loan Object to check.
     * @return True if beginDate is NULL, otherwise false.
     */
    private boolean isBeginDateNull(SBLoan loan)
    {
        return loan.getBeginDate() == null;
    }

    /**
     * It checks if {@link SBLoan} paidUpMonths is NULL.
     *
     * @param loan Object to check.
     * @return True if paidUpMonths is NULL, otherwise false.
     */
    private boolean isPaidUpMonthsNull(SBLoan loan)
    {
        return loan.getPaidUpMonths() == null;
    }

    /**
     * It checks if {@link SBLoan} paidUpMonths is greater than 0.
     *
     * @param loan Object to check.
     * @return True if paidUpMonths is greater than 0, otherwise false.
     */
    private boolean isPaidUpMonthsMin(SBLoan loan)
    {
        return loan.getPaidUpMonths() != null && loan.getPaidUpMonths() >= 0;
    }

    /**
     * It checks if {@link SBLoan} paidUpMonths is equal or less than durationTime.
     *
     * @param loan Object to check.
     * @return True if paidUpMonths is equal or less than durationTime, otherwise false.
     */
    private boolean isPaidUpMonthsMax(SBLoan loan)
    {
        return loan.getPaidUpMonths() != null &&
               loan.getPaidUpMonths() <= loan.getKindOfLoan().getDurationTime();
    }
}
