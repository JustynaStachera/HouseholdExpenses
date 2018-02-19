package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBKindOfOperation;
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
 * Class which contains implemented methods from {@link SBKindOfOperationCustomDao} interface.
 */
@Repository
public class SBKindOfOperationCustomImpl implements SBKindOfOperationCustomDao
{
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    @Transactional
    public void modify(SBKindOfOperation kindOfOperation)
    {
        SBKindOfOperation fop = entityManager.find(SBKindOfOperation.class, kindOfOperation.getId());
        
        fop.setName(kindOfOperation.getName());
    }
    
    @Override
    @Transactional
    public List<SBKindOfOperation> sortTableBy(String chosenEnum)
    {
        Query query;
        
        switch (chosenEnum)
        {
        case "id_asc":
            query = entityManager.createQuery("select b from SBKindOfOperation b order by b.id asc");
            break;
        case "id_desc":
            query = entityManager.createQuery("select b from SBKindOfOperation b order by b.id desc");
            break;
        case "name_asc":
            query = entityManager.createQuery("select b from SBKindOfOperation b order by b.name asc");
            break;
        case "name_desc":
            query = entityManager.createQuery("select b from SBKindOfOperation b order by b.name desc");
            break;
        default:
            query = entityManager.createQuery("select b from SBKindOfOperation b order by b.id asc");
            break;
        }
        
        return (List<SBKindOfOperation>) query.getResultList();
    }
}
