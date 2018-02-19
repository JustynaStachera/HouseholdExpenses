package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBBank;

import java.util.List;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Interface which contains the custom method declarations for {@link SBBank} class.
 * {@link SBBank} class is the equivalent of a 'sbbank' table in the relational database model.
 */
public interface SBBankCustomDao
{
    /**
     * It modifies the record as part of the transaction.
     *
     * @param bank {@link SBBank} object to modify.
     */
    void modify(SBBank bank);
    
    /**
     * It sorts 'sbbank' table records by column name.
     *
     * @param chosenEnum The column name.
     * @return {@link SBBank} sorted object list.
     */
    List<SBBank> sortTableBy(String chosenEnum);
}
