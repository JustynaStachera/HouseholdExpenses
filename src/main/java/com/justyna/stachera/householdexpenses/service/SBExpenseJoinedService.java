package com.justyna.stachera.householdexpenses.service;

import com.justyna.stachera.householdexpenses.domain.joined.SBExpenseJoined;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Interface which contains the custom service method declarations for SBExpenseJoined class.
 * SBExpenseJoined combines {@link com.justyna.stachera.householdexpenses.domain.main.SBExpenseCategory},
 * {@link com.justyna.stachera.householdexpenses.domain.main.SBFormOfPayment} and
 * {@link com.justyna.stachera.householdexpenses.domain.main.SBKindOfOperation} classes.
 */
public interface SBExpenseJoinedService
{
    /**
     * It adds a new expense to database.
     *
     * @param expenseJoined It helps to add {@link com.justyna.stachera.householdexpenses.domain.main.SBExpense} object
     *                      to database as 'sbexpense' record.
     * @param user          Logged in user.
     */
    void addExpenseJoined(SBExpenseJoined expenseJoined, SBUser user);
}
