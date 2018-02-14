package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBExpenseCategory;
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
 * Class which contains implemented methods from {@link SBExpenseCategory}CustomDao} interface.
 */
@Repository
public class SBExpenseCategoryCustomImpl implements SBExpenseCategoryCustomDao
{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void modify(SBExpenseCategory expenseCategory)
    {
        SBExpenseCategory ec = entityManager.find(SBExpenseCategory.class, expenseCategory.getId());

        ec.setName(expenseCategory.getName());
    }

    @Override
    @Transactional
    public List<SBExpenseCategory> sortTableBy(String chosenEnum)
    {
        Query query;

        switch (chosenEnum)
        {
            case "id_asc":
                query = entityManager.createQuery("select b from SBExpenseCategory b order by b.id asc");
                break;
            case "id_desc":
                query = entityManager.createQuery("select b from SBExpenseCategory b order by b.id desc");
                break;
            case "name_asc":
                query = entityManager.createQuery("select b from SBExpenseCategory b order by b.name asc");
                break;
            case "name_desc":
                query = entityManager.createQuery("select b from SBExpenseCategory b order by b.name desc");
                break;
            default:
                query = entityManager.createQuery("select b from SBExpenseCategory b order by b.id asc");
                break;
        }

        return (List<SBExpenseCategory>) query.getResultList();
    }
}
