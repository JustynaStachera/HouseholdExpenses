package com.justyna.stachera.householdexpenses.service;

import com.justyna.stachera.householdexpenses.domain.joined.SBKindOfLoanJoined;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Interface which contains the custom service method declarations for {@link SBKindOfLoanJoined} class.
 * {@link SBKindOfLoanJoined} combines SBBank and SBCapitalisation classes.
 */
public interface SBKindOfLoanJoinedService
{
    /**
     * It adds a new expense to database.
     *
     * @param kindOfLoanJoined It helps to add {@link com.justyna.stachera.householdexpenses.domain.main.SBKindOfLoan}
     *                         object to database as 'sbkind_of_loan' record.
     */
    void addKindOfLoanJoined(SBKindOfLoanJoined kindOfLoanJoined);
}
