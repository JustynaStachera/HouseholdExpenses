package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBTax;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.utils.SBEconomicUtils;
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
 * Class which contains implemented methods from {@link SBTaxCustomDao} interface.
 */
@Repository
public class SBTaxCustomImpl implements SBTaxCustomDao
{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public List<SBTax> sortTableBy(String chosenEnum, SBUser user)
    {
        String username = user.getUsername();

        Query query;

        if (user.getIsReadOnly())
        {
            switch (chosenEnum)
            {
            case "id_asc":
                query = entityManager.createQuery("select b from SBTax b order by b.id asc");
                break;
            case "id_desc":
                query = entityManager.createQuery("select b from SBTax b order by b.id desc");
                break;
            case "user_asc":
                query = entityManager.createQuery("select b from SBTax b order by b.user.person.name asc");
                break;
            case "user_desc":
                query = entityManager.createQuery("select b from SBTax b order by b.user.person.name desc");
                break;
            case "year_asc":
                query = entityManager.createQuery("select b from SBTax b order by b.year asc");
                break;
            case "year_desc":
                query = entityManager.createQuery("select b from SBTax b order by b.year desc");
                break;
            case "perCent_asc":
                query = entityManager.createQuery("select b from SBTax b order by b.perCent asc");
                break;
            case "perCent_desc":
                query = entityManager.createQuery("select b from SBTax b order by b.perCent desc");
                break;
            case "amountPayable_asc":
                query = entityManager.createQuery("select b from SBTax b order by b.amountPayable asc");
                break;
            case "amountPayable_desc":
                query = entityManager.createQuery("select b from SBTax b order by b.amountPayable desc");
                break;
            case "taxRefund_asc":
                query = entityManager.createQuery("select b from SBTax b order by b.taxRefund asc");
                break;
            case "taxRefund_desc":
                query = entityManager.createQuery("select b from SBTax b order by b.taxRefund desc");
                break;
            case "isPaid_asc":
                query = entityManager.createQuery("select b from SBTax b order by b.isPaid asc");
                break;
            case "isPaid_desc":
                query = entityManager.createQuery("select b from SBTax b order by b.isPaid desc");
                break;
            default:
                query = entityManager.createQuery("select b from SBTax b order by b.id asc");
                break;
            }
        }
        else
        {
            switch (chosenEnum)
            {
            case "id_asc":
                query = entityManager.createQuery("select b from SBTax b where b.user.username=:username " +
                                                  "order by b.id asc");
                query.setParameter("username", username);
                break;
            case "id_desc":
                query = entityManager.createQuery("select b from SBTax b where b.user.username=:username " +
                                                  "order by b.id desc");
                query.setParameter("username", username);
                break;
            case "user_asc":
                query = entityManager.createQuery("select b from SBTax b where b.user.username=:username " +
                                                  "order by b.user.person.name asc");
                query.setParameter("username", username);
                break;
            case "user_desc":
                query = entityManager.createQuery("select b from SBTax b where b.user.username=:username " +
                                                  "order by b.user.person.name desc");
                query.setParameter("username", username);
                break;
            case "year_asc":
                query = entityManager.createQuery("select b from SBTax b where b.user.username=:username " +
                                                  "order by b.year asc");
                query.setParameter("username", username);
                break;
            case "year_desc":
                query = entityManager.createQuery("select b from SBTax b where b.user.username=:username " +
                                                  "order by b.year desc");
                query.setParameter("username", username);
                break;
            case "perCent_asc":
                query = entityManager.createQuery("select b from SBTax b where b.user.username=:username " +
                                                  "order by b.perCent asc");
                query.setParameter("username", username);
                break;
            case "perCent_desc":
                query = entityManager.createQuery("select b from SBTax b where b.user.username=:username " +
                                                  "order by b.perCent desc");
                query.setParameter("username", username);
                break;
            case "amountPayable_asc":
                query = entityManager.createQuery("select b from SBTax b where b.user.username=:username " +
                                                  "order by b.amountPayable asc");
                query.setParameter("username", username);
                break;
            case "amountPayable_desc":
                query = entityManager.createQuery("select b from SBTax b where b.user.username=:username " +
                                                  "order by b.amountPayable desc");
                query.setParameter("username", username);
                break;
            case "taxRefund_asc":
                query = entityManager.createQuery("select b from SBTax b where b.user.username=:username " +
                                                  "order by b.taxRefund asc");
                query.setParameter("username", username);
                break;
            case "taxRefund_desc":
                query = entityManager.createQuery("select b from SBTax b where b.user.username=:username " +
                                                  "order by b.taxRefund desc");
                query.setParameter("username", username);
                break;
            case "isPaid_asc":
                query = entityManager.createQuery("select b from SBTax b where b.user.username=:username " +
                                                  "order by b.isPaid asc");
                query.setParameter("username", username);
                break;
            case "isPaid_desc":
                query = entityManager.createQuery("select b from SBTax b where b.user.username=:username " +
                                                  "order by b.isPaid desc");
                query.setParameter("username", username);
                break;
            default:
                query = entityManager.createQuery("select b from SBTax b where b.user.username=:username " +
                                                  "order by b.id asc");
                query.setParameter("username", username);
                break;
            }
        }

        return (List<SBTax>) query.getResultList();
    }

    @Override
    @Transactional
    public void modify(SBTax tax, SBUser user)
    {
        SBTax t = entityManager.find(SBTax.class, tax.getId());

        Double perCent = SBEconomicUtils.getTaxPerCent(tax.getAmount());
        Double amountPayable = SBEconomicUtils.getAmountPayable(perCent, tax.getAmount());

        t.setYear(tax.getYear());
        t.setAmount(tax.getAmount());
        t.setPerCent(perCent);
        t.setAmountPayable(amountPayable);
        t.setTaxRefund(tax.getTaxRefund());
        t.setIsPaid(tax.getIsPaid());
        t.setDescription(tax.getDescription());
        t.setUser(user);
    }
}
