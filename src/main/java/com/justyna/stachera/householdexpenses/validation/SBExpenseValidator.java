package com.justyna.stachera.householdexpenses.validation;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBExpenseDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBKindOfOperationDao;
import com.justyna.stachera.householdexpenses.domain.main.SBExpense;
import com.justyna.stachera.householdexpenses.domain.main.SBKindOfOperation;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.utils.SBCustomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 * <p>
 * Class which contains all validation for {@link SBExpense} object.
 * It implements {@link Validator} interface.
 */
@Component
@PropertySource("classpath:messages.properties")
public class SBExpenseValidator implements Validator
{
    private SBExpenseDao expenseDao;
    private SBKindOfOperationDao kindOfOperationDao;
    private Environment environment;
    private SBUser user;

    /**
     * Argument constructor.
     *
     * @param expenseDao         It provides methods related with 'sbexpense' table from database.
     * @param kindOfOperationDao It provides methods related with 'sbkind_of_operation' table from database.
     * @param environment        It provides methods to get the error messages from messages.properties file.
     */
    @Autowired
    public SBExpenseValidator(SBExpenseDao expenseDao,
                              SBKindOfOperationDao kindOfOperationDao,
                              Environment environment)
    {
        this.expenseDao = expenseDao;
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
        return SBExpense.class.isAssignableFrom(clazz);
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
        SBExpense expense = (SBExpense) o;

        if (isKindOfOperationDisposable(expense))
        {
            if (isNameEmpty(expense))
            {
                errors.rejectValue("name", "emptyMismatch.name", environment.getProperty("emptyMismatch.name"));
            }
            else if (!isNameUniqueForDisposableExpense(expense))
            {
                errors.rejectValue("name", "SBExpense.name.uniqueMismatch",
                                   environment.getProperty("SBExpense.name.uniqueMismatch"));
            }

            if (isPriceNull(expense))
            {
                errors.rejectValue("price", "SBExpense.price.nullMismatch",
                                   environment.getProperty("SBExpense.price.nullMismatch"));
            }
            else if (!isPriceMin(expense))
            {
                errors.rejectValue("price", "SBExpense.price.minMismatch",
                                   environment.getProperty("SBExpense.price.minMismatch"));
            }

            if (isDateOfPurchaseNull(expense))
            {
                errors.rejectValue("dateOfPurchase", "SBExpense.dateOfPurchase.nullMismatch",
                                   environment.getProperty("SBExpense.dateOfPurchase.nullMismatch"));
            }
        }
        else if (isKindOfOperationPeriodic(expense))
        {
            if (isNameEmpty(expense))
            {
                errors.rejectValue("name", "emptyMismatch.name", environment.getProperty("emptyMismatch.name"));
            }

            if (isPriceNull(expense))
            {
                errors.rejectValue("price", "SBExpense.price.nullMismatch",
                                   environment.getProperty("SBExpense.price.nullMismatch"));
            }
            else if (!isPriceMin(expense))
            {
                errors.rejectValue("price", "SBExpense.price.minMismatch",
                                   environment.getProperty("SBExpense.price.minMismatch"));
            }

            if (isDateOfPurchaseNull(expense))
            {
                errors.rejectValue("dateOfPurchase", "SBExpense.dateOfPurchase.nullMismatch",
                                   environment.getProperty("SBExpense.dateOfPurchase.nullMismatch"));
            }
            else if (isDateOfPurchaseFirstRecordModify(expense))
            {
                errors.rejectValue("dateOfPurchase", "SBExpense.dateOfPurchase.uniqueFirstRecordMismatch",
                                   environment.getProperty("SBExpense.dateOfPurchase.uniqueFirstRecordMismatch"));
            }
            else
            {
                String dateOfPurchasePeriodicMsg = dateOfPurchasePeriodicValidMsg(expense);

                if (!dateOfPurchasePeriodicMsg.isEmpty())
                {
                    errors.rejectValue("dateOfPurchase", "SBExpense.dateOfPurchase.validMismatch",
                                       dateOfPurchasePeriodicMsg);
                }
            }
        }
    }

    /**
     * It checks if {@link SBExpense} dateOfPurchase field is valid.
     *
     * @param expense Object to valid.
     * @return True if dateOfPurchase field is valid, otherwise false.
     */
    private boolean isDateOfPurchaseFirstRecordModify(SBExpense expense)
    {
        List<Long> idsList = expenseDao.findAll()
                                       .stream()
                                       .map(SBExpense::getId)
                                       .sorted(Comparator.comparing(Long::intValue))
                                       .collect(Collectors.toList());

        return expense != null &
               expense.getId().equals(idsList.get(0)) &&
               !expense.getDateOfPurchase().equals(expenseDao.getOne(expense.getId()).getDateOfPurchase());
    }

    /**
     * It sets logged in user.
     *
     * @param user Logged in user.
     */
    public void setUser(SBUser user)
    {
        this.user = user;
    }

    /**
     * It checks if {@link SBExpense} name field is empty.
     *
     * @param expense Object to check.
     * @return True if name field is empty, otherwise false.
     */
    private boolean isNameEmpty(SBExpense expense)
    {
        return expense.getName().isEmpty();
    }

    /**
     * It checks if {@link SBExpense} name field is unique for disposable expense.
     *
     * @param expense Object to check.
     * @return True if name field is unique for disposable expense, otherwise false.
     */
    private boolean isNameUniqueForDisposableExpense(SBExpense expense)
    {
        boolean isNameAlreadyExist = expenseDao
                .findAllByKindOfOperation(kindOfOperationDao.findByName("CYKLICZNA"))
                .stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(expense.getName()) &&
                               p.getUser().getUsername().equals(user.getUsername()));

        return expense.getName() == null || !isNameAlreadyExist;
    }

    /**
     * It checks if {@link SBExpense} price field is NULL.
     *
     * @param expense Object to check.
     * @return True if price field is NULL, otherwise false.
     */
    private boolean isPriceNull(SBExpense expense)
    {
        return expense.getPrice() == null;
    }

    /**
     * It checks if {@link SBExpense} price is greater than 0.
     *
     * @param expense Object to check.
     * @return True if price is greater than 0, otherwise false.
     */
    private boolean isPriceMin(SBExpense expense)
    {
        return expense.getPrice() != null && expense.getPrice() > 0;
    }

    /**
     * It checks if {@link SBExpense} dateOfPurchase field is NULL.
     *
     * @param expense Object to check.
     * @return True if dateOfPurchase field is NULL, otherwise false.
     */
    private boolean isDateOfPurchaseNull(SBExpense expense)
    {
        return expense.getDateOfPurchase() == null;
    }

    /**
     * It checks if {@link SBExpense} dateOfPurchase field is valid for periodic expenses.
     *
     * @param expense Object to check.
     * @return An error message if dateOfPurchase field for periodic expenses is invalid, otherwise empty String.
     */
    private String dateOfPurchasePeriodicValidMsg(SBExpense expense)
    {
        String dataValidationMsg = "";

        SBKindOfOperation kindOfOperation = expense.getKindOfOperation();

        if (expense.getDaysLeft() != null)
        {
            if (kindOfOperation.getName().equalsIgnoreCase("CYKLICZNA"))
            {
                List<String> expenseNames = expenseDao.findAll()
                                                      .stream()
                                                      .filter(p -> p.getUser().getUsername().equals(user.getUsername()))
                                                      .map(SBExpense::getName)
                                                      .distinct()
                                                      .collect(Collectors.toList());

                expenseNames.remove(expenseDao.getOne(expense.getId()).getName());

                if (expenseNames.contains(expense.getName()))
                {
                    dataValidationMsg = environment.getProperty("SBExpense.name.uniqueMismatch");
                }
                else
                {
                    List<SBExpense> expenseList = expenseDao
                            .findAll()
                            .stream()
                            .filter(p -> p.getKindOfOperation().getName().equalsIgnoreCase("CYKLICZNA") &&
                                         p.getName().equals(expenseDao.getOne(expense.getId()).getName()) &&
                                         p.getUser().getUsername().equals(user.getUsername()))
                            .collect(Collectors.toList());

                    if (!expenseList.isEmpty())
                    {
                        SBExpense sbExpense = expenseDao.getOne(expense.getId());

                        boolean isEquals = sbExpense.getId().equals(expense.getId()) &&
                                           sbExpense.getName().equals(expense.getName()) &&
                                           sbExpense.getDateOfPurchase().equals(expense.getDateOfPurchase());

                        if (!isEquals)
                        {
                            LocalDate checkDate = expense.getDateOfPurchase().toLocalDate();
                            List<LocalDate> expenseDates = expenseList.stream()
                                                                      .sorted(Comparator.comparing(
                                                                              SBExpense::getDateOfPurchase))
                                                                      .map(e -> e.getDateOfPurchase().toLocalDate())
                                                                      .collect(Collectors.toList());

                            expenseDates.remove(sbExpense.getDateOfPurchase().toLocalDate());

                            if (expenseDates.contains(checkDate))
                            {
                                dataValidationMsg = environment.getProperty(
                                        "SBExpense.dateOfPurchase.firstValidMismatch");
                            }
                            else if (checkDate.compareTo(expenseDates.get(0)) < 0)
                            {
                                dataValidationMsg = environment.getProperty(
                                        "SBExpense.dateOfPurchase.secondValidMismatch");
                            }
                            else
                            {
                                final int DAYS_LEFT = expense.getDaysLeft();
                                LocalDate beginDate = expenseDates.get(0);
                                LocalDate endDate = expenseDates.get(expenseDates.size() - 1);
                                List<List<LocalDate>> schedule = SBCustomUtils.createSchedule(beginDate, endDate,
                                                                                              DAYS_LEFT);

                                if (!SBCustomUtils.isDateUnique(schedule, expenseDates, checkDate))
                                {
                                    dataValidationMsg =
                                            environment.getProperty("SBExpense.dateOfPurchase.thirdValidMismatch");
                                }
                            }
                        }
                    }
                }
            }
        }

        return dataValidationMsg;
    }

    /**
     * It checks if {@link SBExpense} kindOfOperation name field is 'periodic' in English.
     *
     * @param expense Object to check.
     * @return True if kindOfOperation name field is 'periodic' in English, otherwise false.
     */
    private boolean isKindOfOperationPeriodic(SBExpense expense)
    {
        return expense.getKindOfOperation() != null &&
               expense.getKindOfOperation()
                      .getName()
                      .equalsIgnoreCase("CYKLICZNA");
    }

    /**
     * It checks if {@link SBExpense} kindOfOperation name field is 'disposable' in English.
     *
     * @param expense Object to check.
     * @return True if kindOfOperation name field is 'disposable' in English, otherwise false.
     */
    private boolean isKindOfOperationDisposable(SBExpense expense)
    {
        return expense.getKindOfOperation() != null &&
               expense.getKindOfOperation()
                      .getName()
                      .equalsIgnoreCase("JEDNORAZOWA");
    }
}