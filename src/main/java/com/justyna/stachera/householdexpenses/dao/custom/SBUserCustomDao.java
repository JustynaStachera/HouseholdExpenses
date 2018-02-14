package com.justyna.stachera.householdexpenses.dao.custom;

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
 * Interface which contains the custom method declarations for {@link SBUser} class.
 * {@link SBUser} class is the equivalent of a 'sbuser' table in the relational database model.
 */
public interface SBUserCustomDao
{
    /**
     * It modifies user date except password.
     *
     * @param user User to modify.
     */
    void modifyData(SBUser user);

    /**
     * It modifies user password.
     *
     * @param user User to modify
     */
    void modifyPassword(SBUser user);

    /**
     * It modifies the record as part of the transaction.
     *
     * @param chosenEnum The column name.
     * @return {@link SBUser} sorted object list.
     */
    List<SBUser> sortTableBy(String chosenEnum);

    /**
     * It assigns user role.
     *
     * @param user User to modify.
     */
    void assignRole(SBUser user);
}
