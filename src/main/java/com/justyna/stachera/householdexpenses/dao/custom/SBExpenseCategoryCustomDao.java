package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBExpenseCategory;

import java.util.List;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Interface which contains the custom method declarations for {@link SBExpenseCategory} class.
 * {@link SBExpenseCategory} class is the equivalent of a 'sbexpense_category' table in the relational database model.
 */
public interface SBExpenseCategoryCustomDao
{
    /**
     * It modifies the record as part of the transaction.
     *
     * @param expenseCategory {@link SBExpenseCategory} object to modify.
     */
    void modify(SBExpenseCategory expenseCategory);

    /**
     * It sorts 'sbexpense_category' table records by column name.
     *
     * @param chosenEnum The column name.
     * @return {@link SBExpenseCategory} sorted object list.
     */
    List<SBExpenseCategory> sortTableBy(String chosenEnum);
}