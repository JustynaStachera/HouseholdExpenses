package com.justyna.stachera.householdexpenses.service;

import com.justyna.stachera.householdexpenses.domain.joined.SBLoanJoined;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Interface which contains the custom service method declarations for {@link SBLoanJoined} class.
 * {@link SBLoanJoined} combines {@link com.justyna.stachera.householdexpenses.domain.main.SBKindOfLoan} and
 * {@link com.justyna.stachera.householdexpenses.domain.main.SBLoan} classes.
 */
public interface SBLoanJoinedService
{
    /**
     * It adds a new expense to database.
     *
     * @param loanJoined It helps to add {@link com.justyna.stachera.householdexpenses.domain.main.SBLoan}
     *                   object to database as 'sbloan' record.
     * @param user       Logged in user.
     */
    void addLoanJoined(SBLoanJoined loanJoined, SBUser user);
}
