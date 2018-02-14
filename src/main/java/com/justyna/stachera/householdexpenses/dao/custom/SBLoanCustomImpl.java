package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBLoan;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.utils.SBEconomicUtils;
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
 * Class which contains implemented methods from {@link SBLoanCustomDao} interface.
 */
@Repository
public class SBLoanCustomImpl implements SBLoanCustomDao
{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<SBLoan> sortTableBy(String chosenEnum, SBUser user)
    {
        String username = user.getUsername();

        Query query;

        if (user.getIsReadOnly())
        {
            switch (chosenEnum)
            {
                case "id_asc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.id asc");
                    break;
                case "id_desc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.id desc");
                    break;
                case "name_asc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.kindOfLoan.name asc");
                    break;
                case "name_desc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.kindOfLoan.name desc");
                    break;
                case "user_asc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.user.person.name asc");
                    break;
                case "user_desc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.user.person.name desc");
                    break;
                case "initialAmount_asc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.initialAmount asc");
                    break;
                case "initialAmount_desc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.initialAmount desc");
                    break;
                case "payoffSum_asc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.payoffSum asc");
                    break;
                case "payoffSum_desc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.payoffSum desc");
                    break;
                case "interest_asc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.interest asc");
                    break;
                case "interest_desc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.interest desc");
                    break;
                case "monthPayment_asc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.monthPayment asc");
                    break;
                case "monthPayment_desc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.monthPayment desc");
                    break;
                case "beginDate_asc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.beginDate asc");
                    break;
                case "beginDate_desc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.beginDate desc");
                    break;
                case "endDate_asc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.endDate asc");
                    break;
                case "endDate_desc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.endDate desc");
                    break;
                case "paidUpMonths_asc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.paidUpMonths asc");
                    break;
                case "paidUpMonths_desc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.kindOfLoan.name desc");
                    break;
                case "isActive_asc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.isActive asc");
                    break;
                case "isActive_desc":
                    query = entityManager.createQuery("select b from SBLoan b order by b.isActive desc");
                    break;
                default:
                    query = entityManager.createQuery("select b from SBLoan b order by b.id asc");
                    break;
            }
        }
        else
        {
            switch (chosenEnum)
            {
                case "id_asc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.id asc");
                    query.setParameter("username", username);
                    break;
                case "id_desc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.id desc");
                    query.setParameter("username", username);
                    break;
                case "name_asc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.kindOfLoan.name asc");
                    query.setParameter("username", username);
                    break;
                case "name_desc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.kindOfLoan.name desc");
                    query.setParameter("username", username);
                    break;
                case "user_asc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.user.person.name asc");
                    query.setParameter("username", username);
                    break;
                case "user_desc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.user.person.name desc");
                    query.setParameter("username", username);
                    break;
                case "initialAmount_asc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.initialAmount asc");
                    query.setParameter("username", username);
                    break;
                case "initialAmount_desc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.initialAmount desc");
                    query.setParameter("username", username);
                    break;
                case "payoffSum_asc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.payoffSum asc");
                    query.setParameter("username", username);
                    break;
                case "payoffSum_desc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.payoffSum desc");
                    query.setParameter("username", username);
                    break;
                case "interest_asc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.interest asc");
                    query.setParameter("username", username);
                    break;
                case "interest_desc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.interest desc");
                    query.setParameter("username", username);
                    break;
                case "monthPayment_asc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.monthPayment asc");
                    query.setParameter("username", username);
                    break;
                case "monthPayment_desc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.monthPayment desc");
                    query.setParameter("username", username);
                    break;
                case "beginDate_asc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.beginDate asc");
                    query.setParameter("username", username);
                    break;
                case "beginDate_desc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.beginDate desc");
                    query.setParameter("username", username);
                    break;
                case "endDate_asc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.endDate asc");
                    query.setParameter("username", username);
                    break;
                case "endDate_desc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.endDate desc");
                    query.setParameter("username", username);
                    break;
                case "paidUpMonths_asc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.paidUpMonths asc");
                    query.setParameter("username", username);
                    break;
                case "paidUpMonths_desc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.kindOfLoan.name desc");
                    query.setParameter("username", username);
                    break;
                case "isActive_asc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.isActive asc");
                    query.setParameter("username", username);
                    break;
                case "isActive_desc":
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.isActive desc");
                    query.setParameter("username", username);
                    break;
                default:
                    query = entityManager.createQuery("select b from SBLoan b where b.user.username=:username " +
                                                      "order by b.id asc");
                    query.setParameter("username", username);
                    break;
            }
        }

        return (List<SBLoan>) query.getResultList();
    }

    @Override
    @Transactional
    public void modify(SBLoan loan, SBUser user)
    {
        SBLoan sbLoan = entityManager.find(SBLoan.class, loan.getId());

        sbLoan.setKindOfLoan(loan.getKindOfLoan());
        sbLoan.setInitialAmount(loan.getInitialAmount());
        sbLoan.setBeginDate(loan.getBeginDate());
        sbLoan.setPaidUpMonths(loan.getPaidUpMonths());
        sbLoan.setDescription(loan.getDescription());
        sbLoan.setUser(user);

        SBEconomicUtils.calculateLoan(sbLoan);
    }
}
