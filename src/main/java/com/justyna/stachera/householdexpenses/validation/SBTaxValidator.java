package com.justyna.stachera.householdexpenses.validation;

import com.justyna.stachera.householdexpenses.domain.main.SBTax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 * <p>
 * Class which contains all validation for {@link SBTax} object.
 * It implements {@link Validator} interface.
 */
@Component
@PropertySource("classpath:messages.properties")
public class SBTaxValidator implements Validator
{
    private Environment environment;

    /**
     * Argument constructor.
     *
     * @param environment It provides methods to get the error messages from messages.properties file.
     */
    @Autowired
    public SBTaxValidator(Environment environment)
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
        return SBTax.class.isAssignableFrom(clazz);
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
        SBTax tax = (SBTax) o;

        if (isYearNull(tax))
        {
            errors.rejectValue("year", "SBTax.year.nullMismatch", environment.getProperty("SBTax.year.nullMismatch"));
        }
        else if (!isYearMin(tax))
        {
            errors.rejectValue("year", "SBTax.year.minMismatch", environment.getProperty("SBTax.year.minMismatch"));
        }

        if (isAmountNull(tax))
        {
            errors.rejectValue("amount", "SBTax.amount.nullMismatch",
                               environment.getProperty("SBTax.amount.nullMismatch"));
        }
        else if (!isAmountMin(tax))
        {
            errors.rejectValue("amount", "SBTax.amount.minMismatch",
                               environment.getProperty("SBTax.amount.minMismatch"));
        }

        if (isTaxRefundNull(tax))
        {
            errors.rejectValue("taxRefund", "SBTax.taxRefund.nullMismatch",
                               environment.getProperty("SBTax.taxRefund.nullMismatch"));
        }
        else if (!isTaxRefundMin(tax))
        {
            errors.rejectValue("taxRefund", "SBTax.taxRefund.minMismatch",
                               environment.getProperty("SBTax.taxRefund.minMismatch"));
        }

        if (!isPaidValid(tax))
        {
            errors.rejectValue("isPaid", "SBTax.isPaid.validMismatch",
                               environment.getProperty("SBTax.isPaid.validMismatch"));
        }
    }

    /**
     * It checks if {@link SBTax} year is NULL.
     *
     * @param tax Object to check.
     * @return True if year is null, otherwise false.
     */
    private boolean isYearNull(SBTax tax)
    {
        return tax.getYear() == null;
    }

    /**
     * It check if {@link SBTax} year is greater than 0.
     *
     * @param tax Object to check.
     * @return True if year is greater than 0, otherwise false.
     */
    private boolean isYearMin(SBTax tax) { return tax.getYear() != null && tax.getYear() > 0; }

    /**
     * It check if {@link SBTax} amount is NULL.
     *
     * @param tax Object to check.
     * @return True if amount is NULL, otherwise false.
     */
    private boolean isAmountNull(SBTax tax)
    {
        return tax.getAmount() == null;
    }

    /**
     * It check if {@link SBTax} amount is greater than 0.
     *
     * @param tax Object to check.
     * @return True if amount is greater than 0, otherwise false.
     */
    private boolean isAmountMin(SBTax tax) { return tax.getAmount() != null && tax.getAmount() > 0; }

    /**
     * It check if {@link SBTax} taxRefund is NULL.
     *
     * @param tax Object to check.
     * @return True if taxRefund is NULL, otherwise false.
     */
    private boolean isTaxRefundNull(SBTax tax)
    {
        return tax.getTaxRefund() == null;
    }

    /**
     * It check if {@link SBTax} taxRefund is greater than 0.
     *
     * @param tax Object to check.
     * @return True if taxRefund is greater than 0, otherwise false.
     */
    private boolean isTaxRefundMin(SBTax tax) { return tax.getTaxRefund() != null && tax.getTaxRefund() >= 0; }

    /**
     * It check if {@link SBTax} isPaid conditions are valid.
     *
     * @param tax Object to check.
     * @return True if isPaid conditions are valid, otherwise false.
     */
    private boolean isPaidValid(SBTax tax)
    {
        return !(tax.getIsPaid() && tax.getYear() >= LocalDate.now().getYear());
    }
}