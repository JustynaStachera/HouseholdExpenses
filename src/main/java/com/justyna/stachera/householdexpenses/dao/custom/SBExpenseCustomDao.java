package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBExpense;
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
 * Interface which contains the custom method declarations for {@link SBExpense} class.
 * {@link SBExpense} class is the equivalent of a 'sbexpense' table in the relational database model.
 */
public interface SBExpenseCustomDao
{
    /**
     * It modifies the record as part of the transaction.
     *
     * @param expense {@link SBExpense} object to modify.
     * @param user    The logged in user.
     */
    void modify(SBExpense expense, SBUser user);

    /**
     * It modifies the record as part of the transaction.
     *
     * @param chosenEnum The column name.
     * @param user       The logged in user.
     * @return {@link SBExpense} sorted object list.
     */
    List<SBExpense> sortTableBy(String chosenEnum, SBUser user);
}
