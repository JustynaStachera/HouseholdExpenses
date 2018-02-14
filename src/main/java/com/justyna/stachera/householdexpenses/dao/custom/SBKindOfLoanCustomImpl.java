package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBKindOfLoan;
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
 * Class which contains implemented methods from {@link SBKindOfLoanCustomDao} interface.
 */
@Repository
public class SBKindOfLoanCustomImpl implements SBKindOfLoanCustomDao
{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void modify(SBKindOfLoan kindOfLoan)
    {
        SBKindOfLoan kol = entityManager.find(SBKindOfLoan.class, kindOfLoan.getId());

        kol.setBank(kindOfLoan.getBank());
        kol.setCapitalisation(kindOfLoan.getCapitalisation());
        kol.setDurationTime(kindOfLoan.getDurationTime());
        kol.setName(kindOfLoan.getName());
        kol.setPercent(kindOfLoan.getPercent());
    }

    @Override
    @Transactional
    public List<SBKindOfLoan> sortTableBy(String chosenEnum)
    {
        Query query;

        switch (chosenEnum)
        {
            case "id_asc":
                query = entityManager.createQuery("select b from SBKindOfLoan b order by b.id asc");
                break;
            case "id_desc":
                query = entityManager.createQuery("select b from SBKindOfLoan b order by b.id desc");
                break;
            case "name_asc":
                query = entityManager.createQuery("select b from SBKindOfLoan b order by b.name asc");
                break;
            case "name_desc":
                query = entityManager.createQuery("select b from SBKindOfLoan b order by b.name desc");
                break;
            case "durationTime_asc":
                query = entityManager.createQuery("select b from SBKindOfLoan b order by b.durationTime asc");
                break;
            case "durationTime_desc":
                query = entityManager.createQuery("select b from SBKindOfLoan b order by b.durationTime desc");
                break;
            case "bank_asc":
                query = entityManager.createQuery("select b from SBKindOfLoan b order by b.bank.name asc");
                break;
            case "bank_desc":
                query = entityManager.createQuery("select b from SBKindOfLoan b order by b.bank.name desc");
                break;
            case "capitalisation_asc":
                query = entityManager.createQuery("select b from SBKindOfLoan b order by b.capitalisation.name asc");
                break;
            case "capitalisation_desc":
                query = entityManager.createQuery("select b from SBKindOfLoan b order by b.capitalisation.name desc");
                break;
            case "percent_asc":
                query = entityManager.createQuery("select b from SBKindOfLoan b order by b.percent asc");
                break;
            case "percent_desc":
                query = entityManager.createQuery("select b from SBKindOfLoan b order by b.percent desc");
                break;
            default:
                query = entityManager.createQuery("select b from SBKindOfLoan b order by b.id asc");
                break;
        }

        return (List<SBKindOfLoan>) query.getResultList();
    }
}
