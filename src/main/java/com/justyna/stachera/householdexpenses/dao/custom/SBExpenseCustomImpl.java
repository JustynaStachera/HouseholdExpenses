package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBExpenseDao;
import com.justyna.stachera.householdexpenses.domain.main.SBExpense;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Class which contains implemented methods from {@link SBExpenseCustomDao} interface.
 */
@Repository
public class SBExpenseCustomImpl implements SBExpenseCustomDao
{
    @PersistenceContext
    private EntityManager entityManager;

    private SBExpenseDao expenseDao;

    @Autowired
    public SBExpenseCustomImpl(SBExpenseDao expenseDao)
    {
        this.expenseDao = expenseDao;
    }

    @Override
    @Transactional
    public List<SBExpense> sortTableBy(String chosenEnum, SBUser user)
    {
        String username = user.getUsername();

        Query query;

        if (user.getIsReadOnly())
        {
            switch (chosenEnum)
            {
                case "id_asc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.id asc");
                    break;
                case "id_desc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.id desc");
                    break;
                case "name_asc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.name asc");
                    break;
                case "name_desc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.name desc");
                    break;
                case "formOfPayment_asc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.formOfPayment.name asc");
                    break;
                case "formOfPayment_desc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.formOfPayment.name desc");
                    break;
                case "kindOfOperation_asc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.kindOfOperation.name asc");
                    break;
                case "kindOfOperation_desc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.kindOfOperation.name desc");
                    break;
                case "daysLeft_asc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.daysLeft asc");
                    break;
                case "daysLeft_desc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.daysLeft desc");
                    break;
                case "expenseCategory_asc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.expenseCategory.name asc");
                    break;
                case "expenseCategory_desc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.expenseCategory.name desc");
                    break;
                case "user_asc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.user.person.surname asc");
                    break;
                case "user_desc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.user.person.surname desc");
                    break;
                case "price_asc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.price asc");
                    break;
                case "price_desc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.price desc");
                    break;
                case "dateOfPurchase_asc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.dateOfPurchase asc");
                    break;
                case "dateOfPurchase_desc":
                    query = entityManager.createQuery("select b from SBExpense b order by b.dateOfPurchase desc");
                    break;
                default:
                    query = entityManager.createQuery("select b from SBExpense b order by b.id asc");
                    break;
            }
        }
        else
        {
            switch (chosenEnum)
            {
                case "id_asc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username order by b.id asc");
                    query.setParameter("username", username);
                    break;
                case "id_desc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username " +
                                                      "order by b.id desc");
                    query.setParameter("username", username);
                    break;
                case "name_asc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username order by b.name asc");
                    query.setParameter("username", username);
                    break;
                case "name_desc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username " +
                                                      "order by b.name desc");
                    query.setParameter("username", username);
                    break;
                case "formOfPayment_asc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username " +
                                                      "order by b.formOfPayment.name asc");
                    query.setParameter("username", username);
                    break;
                case "formOfPayment_desc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username " +
                                                      "order by b.formOfPayment.name desc");
                    query.setParameter("username", username);
                    break;
                case "kindOfOperation_asc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username " +
                                                      "order by b.kindOfOperation.name asc");
                    query.setParameter("username", username);
                    break;
                case "kindOfOperation_desc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username " +
                                                      "order by b.kindOfOperation.name desc");
                    query.setParameter("username", username);
                    break;
                case "daysLeft_asc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username " +
                                                      "order by b.daysLeft asc");
                    query.setParameter("username", username);
                    break;
                case "daysLeft_desc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username " +
                                                      "order by b.daysLeft desc");
                    query.setParameter("username", username);
                    break;
                case "expenseCategory_asc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username " +
                                                      "order by b.expenseCategory.name asc");
                    query.setParameter("username", username);
                    break;
                case "expenseCategory_desc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username " +
                                                      "order by b.expenseCategory.name desc");
                    query.setParameter("username", username);
                    break;
                case "user_asc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username " +
                                                      "order by b.user.person.surname asc");
                    query.setParameter("username", username);
                    break;
                case "user_desc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username " +
                                                      "order by b.user.person.surname desc");
                    query.setParameter("username", username);
                    break;
                case "price_asc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username " +
                                                      "order by b.price asc");
                    query.setParameter("username", username);
                    break;
                case "price_desc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username " +
                                                      "order by b.price desc");
                    query.setParameter("username", username);
                    break;
                case "dateOfPurchase_asc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username " +
                                                      "order by b.dateOfPurchase asc");
                    query.setParameter("username", username);
                    break;
                case "dateOfPurchase_desc":
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username " +
                                                      "order by b.dateOfPurchase desc");
                    query.setParameter("username", username);
                    break;
                default:
                    query = entityManager.createQuery("select b from SBExpense b " +
                                                      "where b.user.username=:username order by b.id asc");
                    query.setParameter("username", username);
                    break;
            }
        }

        return (List<SBExpense>) query.getResultList();
    }

    @Override
    @Transactional
    public void modify(SBExpense expense, SBUser user)
    {
        SBExpense sbExpense = entityManager.find(SBExpense.class, expense.getId());

        if (expense.getKindOfOperation().getName().equalsIgnoreCase("JEDNORAZOWA"))
        {
            expense.setDaysLeft(0);
            sbExpense.setName(expense.getName());
            sbExpense.setExpenseCategory(expense.getExpenseCategory());
        }
        else if (expense.getKindOfOperation().getName().equalsIgnoreCase("CYKLICZNA"))
        {
            List<SBExpense> expenses = expenseDao
                    .findAll()
                    .stream()
                    .filter(p -> p.getName().equals(sbExpense.getName()) &&
                                 p.getUser().getUsername().equals(expenseDao.getOne(expense.getId()).getUser().getUsername()))
                    .collect(Collectors.toList());

            for (SBExpense e : expenses)
            {
                e.setName(expense.getName());
                e.setExpenseCategory(expense.getExpenseCategory());
            }
        }

        sbExpense.setDateOfPurchase(expense.getDateOfPurchase());
        sbExpense.setDescription(expense.getDescription());
        sbExpense.setPrice(expense.getPrice());
        sbExpense.setDaysLeft(expense.getDaysLeft());
        sbExpense.setFormOfPayment(expense.getFormOfPayment());
        sbExpense.setKindOfOperation(expense.getKindOfOperation());
        sbExpense.setUser(user);

        expenseDao.findAllByUserAndName(sbExpense.getUser(), sbExpense.getName())
                  .forEach(s -> s.setExpenseCategory(expense.getExpenseCategory()));
    }
}