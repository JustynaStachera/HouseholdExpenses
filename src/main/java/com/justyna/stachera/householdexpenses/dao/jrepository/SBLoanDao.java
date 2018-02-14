package com.justyna.stachera.householdexpenses.dao.jrepository;

import com.justyna.stachera.householdexpenses.domain.main.SBKindOfLoan;
import com.justyna.stachera.householdexpenses.domain.main.SBLoan;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
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
 * Interface which implementation contains the basic and query user methods for {@link SBLoan} class.
 * {@link SBLoan} class is the equivalent of a 'sbloan' table in the relational database model.
 */
@Repository
public interface SBLoanDao extends JpaRepository<SBLoan, Long>
{
    List<SBLoan> findAllByUser(SBUser user);

    List<SBLoan> findAllByKindOfLoan(SBKindOfLoan kindOfLoan);

    List<SBLoan> findAllByKindOfLoan(List<SBKindOfLoan> kindsOfLoan);
}
