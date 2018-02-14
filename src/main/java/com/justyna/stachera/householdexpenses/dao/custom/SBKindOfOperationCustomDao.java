package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBKindOfOperation;

import java.util.List;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Interface which contains the custom method declarations for {@link SBKindOfOperation} class.
 * {@link SBKindOfOperation} class is the equivalent of a 'sbkind_of_operation' table in the relational database model.
 */
public interface SBKindOfOperationCustomDao
{
    /**
     * It modifies the record as part of the transaction.
     *
     * @param kindOfOperation {@link SBKindOfOperation} object to modify.
     */
    void modify(SBKindOfOperation kindOfOperation);

    /**
     * It modifies the record as part of the transaction.
     *
     * @param chosenEnum The column name.
     * @return {@link SBKindOfOperation} sorted object list.
     */
    List<SBKindOfOperation> sortTableBy(String chosenEnum);
}
