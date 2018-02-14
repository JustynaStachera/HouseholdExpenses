package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBFormOfPayment;

import java.util.List;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Interface which contains the custom method declarations for {@link SBFormOfPayment} class.
 * {@link SBFormOfPayment} class is the equivalent of a 'sbform_of_payment' table in the relational database model.
 */
public interface SBFormOfPaymentCustomDao
{
    /**
     * It modifies the record as part of the transaction.
     *
     * @param formOfPayment {@link SBFormOfPayment} object to modify.
     */
    void modify(SBFormOfPayment formOfPayment);

    /**
     * It modifies the record as part of the transaction.
     *
     * @param chosenEnum The column name.
     * @return {@link SBFormOfPayment} sorted object list.
     */
    List<SBFormOfPayment> sortTableBy(String chosenEnum);
}
