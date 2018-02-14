package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBTax;
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
 * Interface which contains the custom method declarations for SBTax class.
 * SBTax class is the equivalent of a 'sbtax' table in the relational database model.
 */
public interface SBTaxCustomDao
{
    /**
     * It modifies the record as part of the transaction.
     *
     * @param tax  {@link SBTax} object to modify.
     * @param user The logged in user.
     */
    void modify(SBTax tax, SBUser user);

    /**
     * It modifies the record as part of the transaction.
     *
     * @param chosenEnum The column name.
     * @param user       The logged in user.
     * @return SBTax sorted object list.
     */
    List<SBTax> sortTableBy(String chosenEnum, SBUser user);
}
