package com.justyna.stachera.householdexpenses.dao.jrepository;

import com.justyna.stachera.householdexpenses.domain.main.SBTax;
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
 * Interface which implementation contains the basic and query user methods for SBTax class.
 * SBTax class is the equivalent of a 'sbtax' table in the relational database model.
 */
@Repository
public interface SBTaxDao extends JpaRepository<SBTax, Long>
{
    List<SBTax> findAllByUser(SBUser user);
}
