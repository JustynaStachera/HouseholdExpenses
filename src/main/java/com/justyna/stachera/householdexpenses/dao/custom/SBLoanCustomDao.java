package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBLoan;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;

import java.util.List;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Interface which contains the custom method declarations for {@link SBLoan} class.
 * {@link SBLoan} class is the equivalent of a 'sbloan' table in the relational database model.
 */
public interface SBLoanCustomDao
{
    /**
     * It modifies the record as part of the transaction.
     *
     * @param loan {@link SBLoan} object to modify.
     * @param user The logged in user.
     */
    void modify(SBLoan loan, SBUser user);

    /**
     * It modifies the record as part of the transaction.
     *
     * @param chosenEnum The column name.
     * @param user       The logged in user.
     * @return {@link SBLoan} sorted object list.
     */
    List<SBLoan> sortTableBy(String chosenEnum, SBUser user);
}
