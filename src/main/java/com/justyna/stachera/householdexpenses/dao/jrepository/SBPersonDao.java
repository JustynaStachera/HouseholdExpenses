package com.justyna.stachera.householdexpenses.dao.jrepository;

import com.justyna.stachera.householdexpenses.domain.main.SBPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Interface which implementation contains the basic and query user methods for SBPerson class.
 * SBPerson class is the equivalent of a 'sbperson' table in the relational database model.
 */
@Repository
public interface SBPersonDao extends JpaRepository<SBPerson, Long>
{
}
