package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBFormOfPayment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Class which contains implemented methods from {@link SBFormOfPaymentCustomDao} interface.
 */
@Repository
public class SBFormOfPaymentCustomImpl implements SBFormOfPaymentCustomDao
{
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    @Transactional
    public void modify(SBFormOfPayment formOfPayment)
    {
        SBFormOfPayment fop = entityManager.find(SBFormOfPayment.class, formOfPayment.getId());
        
        fop.setName(formOfPayment.getName());
    }
    
    @Override
    @Transactional
    public List<SBFormOfPayment> sortTableBy(String chosenEnum)
    {
        Query query;
        
        switch (chosenEnum)
        {
        case "id_asc":
            query = entityManager.createQuery("select b from SBFormOfPayment b order by b.id asc");
            break;
        case "id_desc":
            query = entityManager.createQuery("select b from SBFormOfPayment b order by b.id desc");
            break;
        case "name_asc":
            query = entityManager.createQuery("select b from SBFormOfPayment b order by b.name asc");
            break;
        case "name_desc":
            query = entityManager.createQuery("select b from SBFormOfPayment b order by b.name desc");
            break;
        default:
            query = entityManager.createQuery("select b from SBFormOfPayment b order by b.id asc");
            break;
        }
        
        return (List<SBFormOfPayment>) query.getResultList();
    }
}
