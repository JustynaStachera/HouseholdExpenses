package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBKindOfLoan;

import java.util.List;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Interface which contains the custom method declarations for {@link SBKindOfLoan} class.
 * {@link SBKindOfLoan} class is the equivalent of a 'sbkind_of_loan' table in the relational database model.
 */
public interface SBKindOfLoanCustomDao
{
    /**
     * It modifies the record as part of the transaction.
     *
     * @param kindOfLoan {@link SBKindOfLoan} object to modify.
     */
    void modify(SBKindOfLoan kindOfLoan);

    /**
     * It modifies the record as part of the transaction.
     *
     * @param chosenEnum The column name.
     * @return {@link SBKindOfLoan} sorted object list.
     */
    List<SBKindOfLoan> sortTableBy(String chosenEnum);
}
