package com.justyna.stachera.householdexpenses.dao.jrepository;

import com.justyna.stachera.householdexpenses.domain.main.SBFormOfPayment;
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
 * Interface which implementation contains the basic and query user methods for {@link SBFormOfPayment} class.
 * {@link SBFormOfPayment} class is the equivalent of a 'sbform_of_payment' table in the relational database model.
 */
@Repository
public interface SBFormOfPaymentDao extends JpaRepository<SBFormOfPayment, Long>
{
}
