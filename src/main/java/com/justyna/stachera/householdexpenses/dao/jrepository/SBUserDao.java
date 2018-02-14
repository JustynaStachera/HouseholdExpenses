package com.justyna.stachera.householdexpenses.dao.jrepository;

import com.justyna.stachera.householdexpenses.domain.main.SBUser;
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
 * Interface which implementation contains the basic and query user methods for {@link SBUser} class.
 * {@link SBUser} class is the equivalent of a 'sbuser' table in the relational database model.
 */
@Repository
public interface SBUserDao extends JpaRepository<SBUser, Long>
{
    SBUser findByUsername(String username);

    SBUser findByRole(String role_admin);
}
