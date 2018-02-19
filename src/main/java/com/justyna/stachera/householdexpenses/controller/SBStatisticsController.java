package com.justyna.stachera.householdexpenses.controller;

import com.justyna.stachera.householdexpenses.constants.SBWebsitesAndMessages;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBExpenseDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBLoanDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBTaxDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBUserDao;
import com.justyna.stachera.householdexpenses.domain.helpers.SBField;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.utils.SBStatisticsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * {@link Controller} class with methods that support '/statistics/**' links directly.
 */
@Controller
public class SBStatisticsController implements SBWebsitesAndMessages
{
    private SBUserDao userDao;
    private SBExpenseDao expenseDao;
    private SBLoanDao loanDao;
    private SBTaxDao taxDao;

    /**
     * An argument constructor autowired interfaces extending
     * {@link org.springframework.data.jpa.repository.JpaRepository} and
     * {@link org.springframework.validation.Validator} objects.
     *
     * @param userDao    It provides methods related with 'sbuser' table from database.
     * @param expenseDao It provides methods related with 'sbexpense' table from database.
     * @param loanDao    It provides methods related with 'sbloan' table from database.
     * @param taxDao     It provides methods related with 'sbtax' table from database.
     */
    @Autowired
    public SBStatisticsController(SBUserDao userDao,
                                  SBExpenseDao expenseDao,
                                  SBLoanDao loanDao, SBTaxDao taxDao)
    {
        this.userDao = userDao;
        this.expenseDao = expenseDao;
        this.loanDao = loanDao;
        this.taxDao = taxDao;
    }

    /**
     * A method is used to show statistics menu.
     *
     * @param model It contains all models.
     * @return A logical view name.
     */
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public String statisticsMenuGet(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        model.addAttribute("logInUser", username);
        model.addAttribute("isAdmin", logInUser.getIsAdmin());
        model.addAttribute("isAddOnly", logInUser.getIsAddOnly());
        model.addAttribute("isReadOnly", logInUser.getIsReadOnly());
        model.addAttribute("isModifyOnly", logInUser.getIsModifyOnly());

        return STATISTICS_MENU;
    }

    /**
     * A method is used to show the expense statistics.
     *
     * @param model It contains all models.
     * @return A logical view name.
     */
    @RequestMapping(value = "/statistics/expenses", method = RequestMethod.GET)
    public String statisticsExpensesGet(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        model.addAttribute("logInUser", username);
        model.addAttribute("isAdmin", logInUser.getIsAdmin());
        model.addAttribute("isAddOnly", logInUser.getIsAddOnly());
        model.addAttribute("isReadOnly", logInUser.getIsReadOnly());
        model.addAttribute("isModifyOnly", logInUser.getIsModifyOnly());

        /* ********************************************************************************** */

        SBStatisticsUtils.createExpenseMap(userDao.findAll(), expenseDao.findAll());
        SBStatisticsUtils.createExpenseCategoryMap(expenseDao.findAll());

        model.addAttribute("expenseTitle", new SBField(SBStatisticsUtils.getExpenseTitle()));
        model.addAttribute("expenseLabel", new SBField(SBStatisticsUtils.getExpenseLabel()));
        model.addAttribute("expenseUsers", SBStatisticsUtils.getExpenseUsers());
        model.addAttribute("expensePricesSum", SBStatisticsUtils.getExpensePricesSum());

        model.addAttribute("expenseCategoryTitle", new SBField(SBStatisticsUtils.getExpenseCategoryTitle()));
        model.addAttribute("expenseCategoryLabel", new SBField(SBStatisticsUtils.getExpenseCategoryLabel()));
        model.addAttribute("expenseCategories", SBStatisticsUtils.getExpenseCategories());
        model.addAttribute("expenseCategoryPricesSum", SBStatisticsUtils.getExpenseCategoryPricesSum());

        return STATISTICS_EXPENSES;
    }

    /**
     * A method is used to show the loan statistics.
     *
     * @param model It contains all models.
     * @return A logical view name.
     */
    @RequestMapping(value = "/statistics/loans", method = RequestMethod.GET)
    public String statisticsLoansGet(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        model.addAttribute("logInUser", username);
        model.addAttribute("isAdmin", logInUser.getIsAdmin());
        model.addAttribute("isAddOnly", logInUser.getIsAddOnly());
        model.addAttribute("isReadOnly", logInUser.getIsReadOnly());
        model.addAttribute("isModifyOnly", logInUser.getIsModifyOnly());

        /* ********************************************************************************** */

        SBStatisticsUtils.createLoanMap(userDao.findAll(), loanDao.findAll());

        model.addAttribute("loanTitle", new SBField(SBStatisticsUtils.getLoanTitle()));
        model.addAttribute("loanLabel", new SBField(SBStatisticsUtils.getLoanLabel()));
        model.addAttribute("loanUsers", SBStatisticsUtils.getLoanUsers());
        model.addAttribute("loanPayoffSum", SBStatisticsUtils.getLoanPayoffSum());

        return STATISTICS_LOANS;
    }

    /**
     * A method is used to show the tax statistics.
     *
     * @param model It contains all models.
     * @return A logical view name.
     */
    @RequestMapping(value = "/statistics/taxes", method = RequestMethod.GET)
    public String statisticsTaxesGet(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        model.addAttribute("logInUser", username);
        model.addAttribute("isAdmin", logInUser.getIsAdmin());
        model.addAttribute("isAddOnly", logInUser.getIsAddOnly());
        model.addAttribute("isReadOnly", logInUser.getIsReadOnly());
        model.addAttribute("isModifyOnly", logInUser.getIsModifyOnly());

        /* ********************************************************************************** */

        SBStatisticsUtils.createTaxAmountMap(userDao.findAll(), taxDao.findAll());
        SBStatisticsUtils.createTaxAmountPayableMap(userDao.findAll(), taxDao.findAll());

        model.addAttribute("taxAmountTitle", new SBField(SBStatisticsUtils.getTaxAmountTitle()));
        model.addAttribute("taxAmountLabel", new SBField(SBStatisticsUtils.getTaxAmountLabel()));
        model.addAttribute("taxAmountUsers", SBStatisticsUtils.getTaxAmountUsers());
        model.addAttribute("taxAmountSum", SBStatisticsUtils.getTaxAmountSum());

        model.addAttribute("taxAmountPayableTitle", new SBField(SBStatisticsUtils.getTaxAmountPayableTitle()));
        model.addAttribute("taxAmountPayableLabel", new SBField(SBStatisticsUtils.getTaxAmountPayableLabel()));
        model.addAttribute("taxAmountPayableUsers", SBStatisticsUtils.getTaxAmountPayableUsers());
        model.addAttribute("taxAmountPayableSum", SBStatisticsUtils.getTaxAmountPayableSum());

        return STATISTICS_TAXES;
    }
}
