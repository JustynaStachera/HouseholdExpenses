package com.justyna.stachera.householdexpenses.controller;

import com.justyna.stachera.householdexpenses.constants.SBWebsitesAndMessages;
import com.justyna.stachera.householdexpenses.dao.jrepository.*;
import com.justyna.stachera.householdexpenses.domain.helpers.SBBoolean;
import com.justyna.stachera.householdexpenses.domain.helpers.SBDate;
import com.justyna.stachera.householdexpenses.domain.helpers.SBForecasting;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.utils.SBCustomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * {@link Controller} class with methods that support '/edit/forecasting/**' links directly.
 */
@Controller
public class SBForecastingController implements SBWebsitesAndMessages
{
    private SBUserDao userDao;
    private SBExpenseDao expenseDao;
    private SBExpenseCategoryDao expenseCategoryDao;
    private SBFormOfPaymentDao formOfPaymentDao;
    private SBKindOfOperationDao kindOfOperationDao;

    /**
     * An argument constructor autowired interfaces extending {@link org.springframework.data.jpa.repository.JpaRepository} and {@link org.springframework.validation.Validator} objects.
     *
     * @param userDao            It provides methods related with 'sbuser' table from database.
     * @param expenseDao         It provides methods related with 'sbexpense' table from database.
     * @param expenseCategoryDao It provides methods related with 'sbexpense_category' table from database.
     * @param formOfPaymentDao   It provides methods related with 'sbform_of_payment' table from database.
     * @param kindOfOperationDao It provides methods related with 'sbkind_of_operation' table from database.
     */
    @Autowired
    public SBForecastingController(SBUserDao userDao,
                                   SBExpenseDao expenseDao,
                                   SBExpenseCategoryDao expenseCategoryDao,
                                   SBFormOfPaymentDao formOfPaymentDao,
                                   SBKindOfOperationDao kindOfOperationDao)
    {
        this.userDao = userDao;
        this.expenseDao = expenseDao;
        this.expenseCategoryDao = expenseCategoryDao;
        this.formOfPaymentDao = formOfPaymentDao;
        this.kindOfOperationDao = kindOfOperationDao;
    }

    /**
     * A method is used to show a forecasting list.
     *
     * @param chosenDate   A chosen date by user.
     * @param isDateError  It checks if date is correct.
     * @param modelAndView It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/forecasting/selectAll", method = RequestMethod.GET)
    public ModelAndView forecastingGet(@ModelAttribute("chosenDate") SBDate chosenDate,
                                       @ModelAttribute("isDateError") SBBoolean isDateError,
                                       ModelAndView modelAndView)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        modelAndView.addObject("logInUser", username);
        modelAndView.addObject("isAdmin", logInUser.getIsAdmin());
        modelAndView.addObject("isAddOnly", logInUser.getIsAddOnly());
        modelAndView.addObject("isReadOnly", logInUser.getIsReadOnly());
        modelAndView.addObject("isModifyOnly", logInUser.getIsModifyOnly());

        /* ********************************************************************************** */

        modelAndView.setViewName(forecasting);

        List<SBForecasting> forecastingList =
                SBCustomUtils.createForecasting(logInUser, userDao.findAll(), expenseDao.findAll(), chosenDate);

        modelAndView.addObject("forecastingList", forecastingList);
        modelAndView.addObject("forecastingListSize", forecastingList.size());
        modelAndView.addObject("forecastingSum",
                               SBCustomUtils.round(forecastingList.stream().mapToDouble(SBForecasting::getPrice).sum()));
        modelAndView.addObject("chosenDate", chosenDate);

        modelAndView.addObject("formsOfPaymentSize", formOfPaymentDao.findAll().size());
        modelAndView.addObject("kindsOfOperationSize", kindOfOperationDao.findAll().size());
        modelAndView.addObject("expenseCategoriesSize", expenseCategoryDao.findAll().size());

        modelAndView.addObject("isDateError", isDateError);

        return modelAndView;
    }

    /**
     * Method is used to make forecast.
     *
     * @param chosenDate         A chosen date by user.
     * @param bindingResult      An interface BindingResult implementation which checks SBDate object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/forecasting/makeForecast", method = RequestMethod.POST)
    public String forecastingMakeForecastPost(@ModelAttribute("chosenDate") @Valid SBDate chosenDate,
                                              BindingResult bindingResult,
                                              RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors())
        {
            chosenDate.setDate(Date.valueOf(LocalDate.now()));

            redirectAttributes.addFlashAttribute("isDateError", new SBBoolean(true));
        }

        redirectAttributes.addFlashAttribute("chosenDate", chosenDate);

        return "redirect:/forecasting/selectAll";
    }
}
