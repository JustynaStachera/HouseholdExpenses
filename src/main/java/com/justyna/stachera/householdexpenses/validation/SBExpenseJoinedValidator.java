package com.justyna.stachera.householdexpenses.validation;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBExpenseCategoryDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBExpenseDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBKindOfOperationDao;
import com.justyna.stachera.householdexpenses.domain.joined.SBExpenseJoined;
import com.justyna.stachera.householdexpenses.domain.main.SBExpense;
import com.justyna.stachera.householdexpenses.domain.main.SBExpenseCategory;
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
 * Class which contains all validation for {@link SBExpenseJoined} object.
 * It implements {@link Validator} interface.
 */
@Component
@PropertySource("classpath:messages.properties")
public class SBExpenseJoinedValidator implements Validator
{
    private SBExpenseDao expenseDao;
    private SBExpenseCategoryDao expenseCategoryDao;
    private SBKindOfOperationDao kindOfOperationDao;
    private Environment environment;
    private SBUser user;

    /**
     * Argument constructor.
     *
     * @param expenseDao         It provides methods related with 'sbexpense' table from database.
     * @param expenseCategoryDao It provides methods related with 'sbexpense_category' table from database.
     * @param kindOfOperationDao It provides methods related with 'sbkind_of_operation' table from database.
     * @param environment        It provides methods to get the error messages from messages.properties file.
     */
    @Autowired
    public SBExpenseJoinedValidator(SBExpenseDao expenseDao,
                                    SBExpenseCategoryDao expenseCategoryDao,
                                    SBKindOfOperationDao kindOfOperationDao,
                                    Environment environment)
    {
        this.expenseDao = expenseDao;
        this.expenseCategoryDao = expenseCategoryDao;
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
        return SBExpenseJoined.class.isAssignableFrom(clazz);
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
        SBExpenseJoined expenseJoined = (SBExpenseJoined) o;

        if (isKindOfOperationDisposable(expenseJoined))
        {
            if (isNameEmpty(expenseJoined))
            {
                errors.rejectValue("name", "emptyMismatch.name", environment.getProperty("emptyMismatch.name"));
            }
            else if (!isNameUniqueForDisposableExpense(expenseJoined))
            {
                errors.rejectValue("name", "SBExpenseJoined.name.uniqueMismatch",
                                   environment.getProperty("SBExpenseJoined.name.uniqueMismatch"));
            }

            if (isPriceNull(expenseJoined))
            {
                errors.rejectValue("price", "SBExpenseJoined.price.nullMismatch",
                                   environment.getProperty("SBExpenseJoined.price.nullMismatch"));
            }
            else if (!isPriceMin(expenseJoined))
            {
                errors.rejectValue("price", "SBExpenseJoined.price.minMismatch",
                                   environment.getProperty("SBExpenseJoined.price.minMismatch"));
            }

            if (isDateOfPurchaseNull(expenseJoined))
            {
                errors.rejectValue("dateOfPurchase", "SBExpenseJoined.dateOfPurchase.nullMismatch",
                                   environment.getProperty("SBExpenseJoined.dateOfPurchase.nullMismatch"));
            }
        }
        else if (isKindOfOperationPeriodic(expenseJoined))
        {
            if (isNameEmpty(expenseJoined))
            {
                errors.rejectValue("name", "emptyMismatch.name", environment.getProperty("emptyMismatch.name"));
            }
            else if (!isNameUniqueForPeriodicExpense(expenseJoined))
            {
                errors.rejectValue("name", "SBExpenseJoined.name.uniqueMismatch",
                                   environment.getProperty("SBExpenseJoined.name.uniqueMismatch"));
            }

            if (isPriceNull(expenseJoined))
            {
                errors.rejectValue("price", "SBExpenseJoined.price.nullMismatch",
                                   environment.getProperty("SBExpenseJoined.price.nullMismatch"));
            }
            else if (!isPriceMin(expenseJoined))
            {
                errors.rejectValue("price", "SBExpenseJoined.price.minMismatch",
                                   environment.getProperty("SBExpenseJoined.price.minMismatch"));
            }

            if (isDaysLeftNull(expenseJoined))
            {
                errors.rejectValue("daysLeft", "SBExpenseJoined.price.minMismatch",
                                   environment.getProperty("SBExpenseJoined.daysLeft.nullMismatch"));
            }
            else if (!isDaysLeftMin(expenseJoined))
            {
                errors.rejectValue("daysLeft", "SBExpenseJoined.daysLeft.minMismatch",
                                   environment.getProperty("SBExpenseJoined.daysLeft.minMismatch"));
            }

            if (isDateOfPurchaseNull(expenseJoined))
            {
                errors.rejectValue("dateOfPurchase", "SBExpenseJoined.dateOfPurchase.nullMismatch",
                                   environment.getProperty("SBExpenseJoined.dateOfPurchase.nullMismatch"));
            }
            else
            {
                String dateOfPurchasePeriodicMsg = dateOfPurchasePeriodicValidMsg(expenseJoined);

                if (!dateOfPurchasePeriodicMsg.isEmpty())
                {
                    errors.rejectValue("dateOfPurchase", "SBExpenseJoined.dateOfPurchase.validMismatch",
                                       dateOfPurchasePeriodicMsg);
                }
                else
                {
                    String daysLeftAndExpenseCategoryMsg = daysLeftAndExpenseCategoryValidMsg(expenseJoined);

                    if (!daysLeftAndExpenseCategoryMsg.isEmpty())
                    {
                        errors.rejectValue("daysLeft", "SBExpenseJoined.expenseCategory.validMismatch",
                                           daysLeftAndExpenseCategoryMsg);
                    }
                }
            }
        }
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
     * It checks if {@link SBExpenseJoined} name field is empty.
     *
     * @param expenseJoined Object to check.
     * @return True if name is empty, otherwise true.
     */
    private boolean isNameEmpty(SBExpenseJoined expenseJoined)
    {
        return expenseJoined.getName().isEmpty();
    }

    /**
     * It checks if {@link SBExpenseJoined} name field is unique for disposable expenses.
     *
     * @param expenseJoined Object to check.
     * @return True if name is empty, otherwise true.
     */
    private boolean isNameUniqueForDisposableExpense(SBExpenseJoined expenseJoined)
    {
        boolean isNameAlreadyExist = expenseDao
                .findAllByKindOfOperation(kindOfOperationDao.findByName("CYKLICZNA"))
                .stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(expenseJoined.getName()) &&
                               p.getUser().getUsername().equals(user.getUsername()));

        return expenseJoined.getName() == null || !isNameAlreadyExist;
    }

    /**
     * It checks if {@link SBExpenseJoined} name field is unique for periodic expenses.
     *
     * @param expenseJoined Object to check.
     * @return True if name is unique, otherwise true.
     */
    private boolean isNameUniqueForPeriodicExpense(SBExpenseJoined expenseJoined)
    {
        boolean isNameAlreadyExist = expenseDao
                .findAllByKindOfOperation(kindOfOperationDao.findByName("JEDNORAZOWA"))
                .stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(expenseJoined.getName()) &&
                               p.getUser().getUsername().equals(user.getUsername()));

        return expenseJoined.getName() == null || !isNameAlreadyExist;
    }

    /**
     * It checks if {@link SBExpenseJoined} daysLeft field is NULL.
     *
     * @param expenseJoined Object to check.
     * @return True if daysLeft is NULL, otherwise false.
     */
    private boolean isDaysLeftNull(SBExpenseJoined expenseJoined)
    {
        return expenseJoined.getDaysLeft() == null &&
               kindOfOperationDao.getOne(expenseJoined.getKindOfOperationId())
                                 .getName()
                                 .equalsIgnoreCase("CYKLICZNA");
    }

    /**
     * It checks if {@link SBExpenseJoined} daysLeft is greater than 0.
     *
     * @param expenseJoined Object to check.
     * @return True is daysLeft is greater than 0, otherwise false.
     */
    private boolean isDaysLeftMin(SBExpenseJoined expenseJoined)
    {
        return expenseJoined.getDaysLeft() != null && expenseJoined.getDaysLeft() > 0;
    }

    /**
     * It checks if {@link SBExpenseJoined} daysLeft field and expenseCategory field are valid.
     *
     * @param expenseJoined Object to check.
     * @return Message if daysLeft field and expenseCategory field are invalid, otherwise empty String.
     */
    private String daysLeftAndExpenseCategoryValidMsg(SBExpenseJoined expenseJoined)
    {
        SBKindOfOperation kindOfOperation = kindOfOperationDao.getOne(expenseJoined.getKindOfOperationId());
        SBExpenseCategory expenseCategory = expenseCategoryDao.getOne(expenseJoined.getExpenseCategoryId());
        String dataValidationMsg = "";

        if (kindOfOperation.getName().equalsIgnoreCase("CYKLICZNA"))
        {
            List<SBExpense> expenseList = expenseDao
                    .findAllByNameAndKindOfOperationAndUser(expenseJoined.getName(), kindOfOperation, user);

            boolean isValid = expenseList.isEmpty() ||
                              expenseList.stream().anyMatch(p -> p.getDaysLeft().equals(expenseJoined.getDaysLeft()) &&
                                                                 p.getExpenseCategory().getName().equals(expenseCategory.getName()));

            if (!isValid)
            {
                String expectedData = "[kategoria=" + expenseList.get(0).getExpenseCategory().getName() +
                                      ", okres=" + expenseList.get(0).getDaysLeft() + "]";

                dataValidationMsg = "Opłata cykliczna o podanej 'nazwie' powinna zawierać: " + expectedData;
            }
        }

        return dataValidationMsg;
    }

    /**
     * It checks if {@link SBExpenseJoined} price field is NULL.
     *
     * @param expenseJoined Object to check.
     * @return True if price is NULL, otherwise false.
     */
    private boolean isPriceNull(SBExpenseJoined expenseJoined)
    {
        return expenseJoined.getPrice() == null;
    }

    /**
     * It checks if {@link SBExpenseJoined} price is greater than 0.
     *
     * @param expenseJoined Object to check.
     * @return True is price is greater than 0, otherwise false.
     */
    private boolean isPriceMin(SBExpenseJoined expenseJoined)
    {
        return expenseJoined.getPrice() != null && expenseJoined.getPrice() > 0;
    }

    /**
     * It checks if {@link SBExpenseJoined} dateOfPurchase field is NULL.
     *
     * @param expenseJoined Object to check.
     * @return True is dateOfPurchase field is NULL, otherwise false.
     */
    private boolean isDateOfPurchaseNull(SBExpenseJoined expenseJoined)
    {
        return expenseJoined.getDateOfPurchase() == null;
    }

    /**
     * It checks if {@link SBExpenseJoined} dateOfPurchase field for periodic expenses are valid.
     *
     * @param expenseJoined Object to check.
     * @return Message if dateOfPurchase field for periodic expenses are invalid, otherwise empty String.
     */
    private String dateOfPurchasePeriodicValidMsg(SBExpenseJoined expenseJoined)
    {
        String dataValidationMsg = "";

        SBKindOfOperation kindOfOperation = kindOfOperationDao.getOne(expenseJoined.getKindOfOperationId());

        if (expenseJoined.getDaysLeft() != null && kindOfOperation.getName().equalsIgnoreCase("CYKLICZNA"))
        {
            List<SBExpense> expenseList = expenseDao
                    .findAll()
                    .stream()
                    .filter(p -> p.getKindOfOperation().getName().equalsIgnoreCase("CYKLICZNA") &&
                                 p.getName().equalsIgnoreCase(expenseJoined.getName()) &&
                                 p.getUser().getUsername().equals(user.getUsername()))
                    .collect(Collectors.toList());

            if (!expenseList.isEmpty())
            {
                LocalDate checkDate = expenseJoined.getDateOfPurchase().toLocalDate();
                List<LocalDate> expenseDates = expenseList.stream()
                                                          .sorted(Comparator.comparing(SBExpense::getDateOfPurchase))
                                                          .map(e -> e.getDateOfPurchase().toLocalDate())
                                                          .collect(Collectors.toList());

                if (expenseDates.contains(checkDate))
                {
                    dataValidationMsg = environment.getProperty("SBExpenseJoined.dateOfPurchase.firstValidMismatch");
                }
                else if (checkDate.compareTo(expenseDates.get(0)) < 0)
                {
                    dataValidationMsg = environment.getProperty("SBExpenseJoined.dateOfPurchase.secondValidMismatch");
                }
                else
                {
                    LocalDate beginDate = expenseDates.get(0);
                    LocalDate endDate = expenseDates.get(expenseDates.size() - 1);
                    final int DAYS_LEFT = expenseJoined.getDaysLeft();
                    List<List<LocalDate>> schedule = SBCustomUtils.createSchedule(beginDate, endDate, DAYS_LEFT);

                    if (!SBCustomUtils.isDateUnique(schedule, expenseDates, checkDate))
                    {
                        dataValidationMsg =
                                environment.getProperty("SBExpenseJoined.dateOfPurchase.thirdValidMismatch");
                    }
                }
            }
        }

        return dataValidationMsg;
    }

    /**
     * It checks if {@link SBExpenseJoined} kindOfOperation name field is "DISPOSABLE" in English.
     *
     * @param expenseJoined Object to check.
     * @return True if kindOfOperation name field is "DISPOSABLE" in English, otherwise false.
     */
    private boolean isKindOfOperationDisposable(SBExpenseJoined expenseJoined)
    {
        return expenseJoined.getKindOfOperationId() != null &&
               kindOfOperationDao.getOne(expenseJoined.getKindOfOperationId())
                                 .getName()
                                 .equalsIgnoreCase("JEDNORAZOWA");
    }

    /**
     * It checks if {@link SBExpenseJoined} kindOfOperation name field is "PERIODIC" in English.
     *
     * @param expenseJoined Object to check.
     * @return True if kindOfOperation name field is "PERIODIC" in English, otherwise false.
     */
    private boolean isKindOfOperationPeriodic(SBExpenseJoined expenseJoined)
    {
        return expenseJoined.getKindOfOperationId() != null &&
               kindOfOperationDao.getOne(expenseJoined.getKindOfOperationId())
                                 .getName()
                                 .equalsIgnoreCase("CYKLICZNA");
    }
}
