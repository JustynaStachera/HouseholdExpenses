package com.justyna.stachera.householdexpenses.dao.jrepository;

import com.justyna.stachera.householdexpenses.domain.main.SBBank;
import com.justyna.stachera.householdexpenses.domain.main.SBCapitalisation;
import com.justyna.stachera.householdexpenses.domain.main.SBKindOfLoan;
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
 * Interface which implementation contains the basic and query user methods for {@link SBKindOfLoan} class.
 * {@link SBKindOfLoan} class is the equivalent of a 'sbkind_of_loan' table in the relational database model.
 */
@Repository
public interface SBKindOfLoanDao extends JpaRepository<SBKindOfLoan, Long>
{
    List<SBKindOfLoan> findAllByBank(SBBank bank);

    List<SBKindOfLoan> findAllByCapitalisation(SBCapitalisation capitalisation);

    SBKindOfLoan findByName(String name);
}
