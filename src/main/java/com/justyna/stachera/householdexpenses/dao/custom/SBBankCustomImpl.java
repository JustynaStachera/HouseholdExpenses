package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBBank;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Class which contains implemented methods from {@link SBBankCustomDao} interface.
 */
@Repository
public class SBBankCustomImpl implements SBBankCustomDao
{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void modify(SBBank bank)
    {
        SBBank b = entityManager.find(SBBank.class, bank.getId());

        b.setName(bank.getName());
    }

    @Override
    @Transactional
    public List<SBBank> sortTableBy(String chosenEnum)
    {
        Query query;

        switch (chosenEnum)
        {
            case "id_asc":
                query = entityManager.createQuery("select b from SBBank b order by b.id asc");
                break;
            case "id_desc":
                query = entityManager.createQuery("select b from SBBank b order by b.id desc");
                break;
            case "name_asc":
                query = entityManager.createQuery("select b from SBBank b order by b.name asc");
                break;
            case "name_desc":
                query = entityManager.createQuery("select b from SBBank b order by b.name desc");
                break;
            default:
                query = entityManager.createQuery("select b from SBBank b order by b.id asc");
                break;
        }

        return (List<SBBank>) query.getResultList();
    }
}