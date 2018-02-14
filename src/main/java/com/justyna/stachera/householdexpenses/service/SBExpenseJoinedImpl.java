package com.justyna.stachera.householdexpenses.service;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBExpenseCategoryDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBExpenseDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBFormOfPaymentDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBKindOfOperationDao;
import com.justyna.stachera.householdexpenses.domain.joined.SBExpenseJoined;
import com.justyna.stachera.householdexpenses.domain.main.SBExpense;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 *
 * Class which contains implemented methods from SBExpenseJoinedService interface.
 */
@Service
public class SBExpenseJoinedImpl implements SBExpenseJoinedService
{
    private SBFormOfPaymentDao formOfPaymentDao;
    private SBKindOfOperationDao kindOfOperationDao;
    private SBExpenseCategoryDao expenseCategoryDao;
    private SBExpenseDao expenseDao;

    /**
     * Argument constructor.
     *
     * @param formOfPaymentDao It provides methods related with 'sbform_of_payment' table from database.
     * @param kindOfOperationDao It provides methods related with 'sbkind_of_operation' table from database.
     * @param expenseCategoryDao It provides methods related with 'sbexpense_category' table from database.
     * @param expenseDao It provides methods related with 'sbexpense' table from database.
     */
    @Autowired
    public SBExpenseJoinedImpl(SBFormOfPaymentDao formOfPaymentDao,
                               SBKindOfOperationDao kindOfOperationDao,
                               SBExpenseCategoryDao expenseCategoryDao,
                               SBExpenseDao expenseDao)
    {
        this.formOfPaymentDao = formOfPaymentDao;
        this.kindOfOperationDao = kindOfOperationDao;
        this.expenseCategoryDao = expenseCategoryDao;
        this.expenseDao = expenseDao;
    }

    @Override
    @Transactional
    public void addExpenseJoined(SBExpenseJoined expenseJoined, SBUser user)
    {
        SBExpense expense = SBExpense
                .builder()
                .name(expenseJoined.getName())
                .price(expenseJoined.getPrice())
                .dateOfPurchase(expenseJoined.getDateOfPurchase())
                .description(expenseJoined.getDescription())
                .daysLeft(expenseJoined.getDaysLeft())
                .formOfPayment(formOfPaymentDao.getOne(expenseJoined.getFormOfPaymentId()))
                .kindOfOperation(kindOfOperationDao.getOne(expenseJoined.getKindOfOperationId()))
                .expenseCategory(expenseCategoryDao.getOne(expenseJoined.getExpenseCategoryId()))
                .user(user)
                .build();

        if (expense.getKindOfOperation().getName().equalsIgnoreCase("JEDNORAZOWA"))
        {
            expense.setDaysLeft(0);
        }

        expenseDao.save(expense);
    }
}