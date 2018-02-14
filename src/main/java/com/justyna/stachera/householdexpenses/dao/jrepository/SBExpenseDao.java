package com.justyna.stachera.householdexpenses.dao.jrepository;

import com.justyna.stachera.householdexpenses.domain.main.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Interface which implementation contains the basic and query user methods for {@link SBExpense} class.
 * {@link SBExpense} class is the equivalent of a 'sbexpense' table in the relational database model.
 */
@Repository
public interface SBExpenseDao extends JpaRepository<SBExpense, Long>
{
    List<SBExpense> findAllByUser(SBUser user);

    List<SBExpense> findAllByExpenseCategory(SBExpenseCategory expenseCategory);

    List<SBExpense> findAllByFormOfPayment(SBFormOfPayment formOfPayment);

    List<SBExpense> findAllByKindOfOperation(SBKindOfOperation kindOfOperation);

    List<SBExpense> findAllByNameAndKindOfOperationAndUser(String name, SBKindOfOperation sbKindOfOperation,
                                                           SBUser user);

    List<SBExpense> findAllByIdAndUser(Long id, SBUser user);

    List<SBExpense> findAllByUserAndName(SBUser user, String name);
}
