package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBCapitalisation;

import java.util.List;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Interface which contains the custom method declarations for SBCapitalisation class.
 * {@link SBCapitalisation} class is the equivalent of a 'sbcapitalisation' table in the relational database model.
 */
public interface SBCapitalisationCustomDao
{
    /**
     * It modifies the record as part of the transaction.
     *
     * @param capitalisation {@link SBCapitalisation} object to modify.
     */
    void modify(SBCapitalisation capitalisation);

    /**
     * It sorts 'sbcapitalisation' table records by column name.
     *
     * @param chosenEnum The column name.
     * @return {@link SBCapitalisation} sorted object list.
     */
    List<SBCapitalisation> sortTableBy(String chosenEnum);
}
