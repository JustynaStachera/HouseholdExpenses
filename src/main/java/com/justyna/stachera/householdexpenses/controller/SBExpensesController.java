package com.justyna.stachera.householdexpenses.controller;

import com.justyna.stachera.householdexpenses.constants.SBWebsitesAndMessages;
import com.justyna.stachera.householdexpenses.dao.custom.SBExpenseCustomDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.*;
import com.justyna.stachera.householdexpenses.domain.enums.SBExpenseFields;
import com.justyna.stachera.householdexpenses.domain.helpers.*;
import com.justyna.stachera.householdexpenses.domain.joined.SBExpenseJoined;
import com.justyna.stachera.householdexpenses.domain.main.SBExpense;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.service.SBExpenseJoinedService;
import com.justyna.stachera.householdexpenses.utils.SBCustomUtils;
import com.justyna.stachera.householdexpenses.validation.SBExpenseJoinedValidator;
import com.justyna.stachera.householdexpenses.validation.SBExpenseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * {@link Controller} class with methods that support '/expenses/**' links directly.
 */
@Controller
public class SBExpensesController implements SBWebsitesAndMessages
{
    private SBExpenseDao expenseDao;
    private SBExpenseCustomDao expenseCustomDao;
    private SBFormOfPaymentDao formOfPaymentDao;
    private SBKindOfOperationDao kindOfOperationDao;
    private SBExpenseCategoryDao expenseCategoryDao;
    private SBExpenseJoinedService expenseJoinedService;
    private SBUserDao userDao;
    private SBExpenseValidator expenseValidator;
    private SBExpenseJoinedValidator expenseJoinedValidator;

    /**
     * An argument constructor autowired interfaces extending {@link org.springframework.data.jpa.repository.JpaRepository} and {@link org.springframework.validation.Validator} objects.
     *
     * @param expenseDao             It provides methods related with 'sbexpense' table from database.
     * @param expenseCustomDao       It provides extra methods related with 'sbexpense' table from database.
     * @param formOfPaymentDao       It provides methods related with 'sbform_of_payment' table from database.
     * @param kindOfOperationDao     It provides methods related with 'sbkind_of_operation' table from database.
     * @param expenseCategoryDao     It provides methods related with 'sbexpense_category' table from database.
     * @param expenseJoinedService   It provides methods related with 'sbexpense_category', 'sbform_of_payment',
     *                               'sbkind_of_operation' and 'sbuser' tables from database.
     * @param userDao                It provides methods related with 'sbuser' table from database.
     * @param expenseValidator       It validates {@link SBExpense} object fields.
     * @param expenseJoinedValidator It validates {@link SBExpenseJoined} object fields.
     */
    @Autowired
    public SBExpensesController(SBExpenseDao expenseDao,
                                SBExpenseCustomDao expenseCustomDao,
                                SBFormOfPaymentDao formOfPaymentDao,
                                SBKindOfOperationDao kindOfOperationDao,
                                SBExpenseCategoryDao expenseCategoryDao,
                                SBExpenseJoinedService expenseJoinedService,
                                SBUserDao userDao,
                                SBExpenseValidator expenseValidator,
                                SBExpenseJoinedValidator expenseJoinedValidator)
    {
        this.expenseDao = expenseDao;
        this.expenseCustomDao = expenseCustomDao;
        this.formOfPaymentDao = formOfPaymentDao;
        this.kindOfOperationDao = kindOfOperationDao;
        this.expenseCategoryDao = expenseCategoryDao;
        this.expenseJoinedService = expenseJoinedService;
        this.userDao = userDao;
        this.expenseValidator = expenseValidator;
        this.expenseJoinedValidator = expenseJoinedValidator;
    }

    /**
     * A method is used to show a expense list, sort it and find record by expense ID.
     *
     * @param expenseField             An chosen field from a sort bar.
     * @param expenseId                An ID value chosen field.
     * @param chosenEnum               It informs in which column the table has to be sorted.
     * @param addInfo                  A message about adding a new record to database.
     * @param modifyInfo               A message about modification a record in database.
     * @param removeInfo               A message about deleting record from database.
     * @param isExpenseUserIdInvalid   It checks if user ID is invalid.
     * @param isExpensePeriodicInvalid It checks if periodic is invalid.
     * @param modelAndView             It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/expenses/selectAll", method = RequestMethod.GET)
    public ModelAndView expensesSelectAllGet(@ModelAttribute("expenseField") SBField expenseField,
                                             @ModelAttribute("expenseId") SBId expenseId,
                                             @ModelAttribute("chosenEnum") String chosenEnum,
                                             @ModelAttribute("expenseAddInfo") SBField addInfo,
                                             @ModelAttribute("expenseModifyInfo") SBField modifyInfo,
                                             @ModelAttribute("expenseRemoveInfo") SBField removeInfo,
                                             @ModelAttribute("isExpenseUserIdInvalid") SBBoolean isExpenseUserIdInvalid,
                                             @ModelAttribute("isExpensePeriodicInvalid") SBBoolean isExpensePeriodicInvalid,
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

        modelAndView.setViewName(expenses);

        List<SBExpense> sortedExpenses;

        if (expenseId.getId() != null && logInUser.getIsReadOnly())
        {
            sortedExpenses = new ArrayList<SBExpense>()
            {{
                add(expenseDao.getOne(expenseId.getId()));
            }};
            modelAndView.addObject("expenses", sortedExpenses);
        }
        else if (expenseId.getId() != null && !logInUser.getIsReadOnly())
        {
            sortedExpenses = expenseDao.findAllByIdAndUser(expenseId.getId(), logInUser);
            modelAndView.addObject("expenses", sortedExpenses);
        }
        else
        {
            sortedExpenses = expenseCustomDao.sortTableBy(chosenEnum, logInUser);
            modelAndView.addObject("expenses", sortedExpenses);
        }

        modelAndView.addObject("expenseFields", SBExpenseFields.getList());
        modelAndView.addObject("expensesSum",
                               SBCustomUtils.round(sortedExpenses.stream().mapToDouble(SBExpense::getPrice).sum()));
        modelAndView.addObject("expensesSize", expenseDao.findAll().size());
        modelAndView.addObject("expensesSortedSize", sortedExpenses.size());
        modelAndView.addObject("expensesUserSize", expenseDao.findAllByUser(logInUser).size());
        modelAndView.addObject("expenseId", expenseId);

        modelAndView.addObject("formsOfPaymentSize", formOfPaymentDao.findAll().size());
        modelAndView.addObject("kindsOfOperationSize", kindOfOperationDao.findAll().size());
        modelAndView.addObject("expenseCategoriesSize", expenseCategoryDao.findAll().size());

        List<SBMessage> expenseMessages = SBCustomUtils.expenseMessages(logInUser, userDao.findAll(), expenseDao.findAll());

        modelAndView.addObject("isExpenseMessagesEmpty", expenseMessages.isEmpty());
        modelAndView.addObject("expenseMessages", expenseMessages);

        modelAndView.addObject("expenseAddInfo", addInfo);
        modelAndView.addObject("expenseRemoveInfo", removeInfo);
        modelAndView.addObject("expenseModifyInfo", modifyInfo);
        modelAndView.addObject("isExpenseUserIdInvalid", isExpenseUserIdInvalid);
        modelAndView.addObject("isExpensePeriodicInvalid", isExpensePeriodicInvalid);

        return modelAndView;
    }

    /**
     * A method is used to remove a expense record from database.
     *
     * @param id                 A removed record ID.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/expenses/remove/{id}", method = RequestMethod.GET)
    public String expensesRemoveGet(@PathVariable("id") Long id,
                                    RedirectAttributes redirectAttributes)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        /* ********************************************************************************** */

        if (logInUser.getId() != expenseDao.getOne(id).getUser().getId() && !logInUser.getIsAdmin())
        {
            redirectAttributes.addFlashAttribute("isExpenseUserIdInvalid", new SBBoolean(true));

            return "redirect:/expenses/selectAll";
        }

        List<SBExpense> expenseList = expenseDao
                .findAll()
                .stream()
                .filter(p -> p.getName().equals(expenseDao.getOne(id).getName()) &&
                             p.getKindOfOperation().getName().equalsIgnoreCase("CYKLICZNA") &&
                             p.getUser().getUsername().equals(expenseDao.getOne(id).getUser().getUsername()))
                .sorted(Comparator.comparing(SBExpense::getDateOfPurchase))
                .collect(Collectors.toList());

        if (expenseList.size() > 1 &&
            expenseList.get(0).getDateOfPurchase().equals(expenseDao.getOne(id).getDateOfPurchase()))
        {
            redirectAttributes.addFlashAttribute("isExpensePeriodicInvalid", new SBBoolean(true));

            return "redirect:/expenses/selectAll";
        }

        /* ********************************************************************************** */


        expenseDao.delete(id);

        redirectAttributes.addFlashAttribute("expenseRemoveInfo", new SBField(DATA_REMOVE_MSG));

        return "redirect:/expenses/selectAll";
    }

    /**
     * A method is used to add a new expense record to database. It shows form to complete.
     *
     * @param expenseErrors A error list from a expense form.
     * @param expenseJoined An object where the data from a form is located.
     * @param model         It contains all models.
     * @return A logical view name or a value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/expenses/add", method = RequestMethod.GET)
    public String expensesAddGet(@ModelAttribute("expenseErrors") SBList expenseErrors,
                                 @ModelAttribute("expenseJoined") SBExpenseJoined expenseJoined,
                                 Model model)
    {
        if (expenseCategoryDao.findAll().size() == 0 ||
            formOfPaymentDao.findAll().size() == 0 ||
            kindOfOperationDao.findAll().size() == 0)
        {
            return "redirect:/expenses/selectAll";
        }

        /* ********************************************************************************** */

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        model.addAttribute("logInUser", username);
        model.addAttribute("isAdmin", logInUser.getIsAdmin());
        model.addAttribute("isAddOnly", logInUser.getIsAddOnly());
        model.addAttribute("isReadOnly", logInUser.getIsReadOnly());
        model.addAttribute("isModifyOnly", logInUser.getIsModifyOnly());

        /* ********************************************************************************** */

        model.addAttribute("expenseJoined", expenseJoined);
        model.addAttribute("formsOfPayment", formOfPaymentDao.findAll());
        model.addAttribute("kindsOfOperation", kindOfOperationDao.findAll());
        model.addAttribute("expenseCategories", expenseCategoryDao.findAll());

        model.addAttribute("expenseErrors", expenseErrors.getList());

        return expensesAdd;
    }

    /**
     * A method is used to add a new expense record. It validates {@link SBExpense} object and saves it in database.
     *
     * @param expenseJoined      An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBExpenseJoined} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/expenses/add", method = RequestMethod.POST)
    public String expensesAddPost(@ModelAttribute("expenseJoined") @Valid SBExpenseJoined expenseJoined,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        /* ********************************************************************************** */

        expenseJoinedValidator.setUser(logInUser);
        expenseJoinedValidator.validate(expenseJoined, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult.getAllErrors()
                                               .stream()
                                               .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                               .collect(Collectors.toList());

            Optional foundErrorOptional =
                    errors.stream()
                          .filter(p -> p.contains("'java.lang.String' to required type 'java.sql.Date'"))
                          .findAny();

            if (foundErrorOptional.isPresent())
            {
                errors.remove(foundErrorOptional.get());
            }

            redirectAttributes.addFlashAttribute("expenseErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));
            redirectAttributes.addFlashAttribute("expenseJoined", expenseJoined);

            return "redirect:/expenses/add";
        }

        /* ********************************************************************************** */

        expenseJoinedService.addExpenseJoined(expenseJoined, logInUser);

        redirectAttributes.addFlashAttribute("expenseAddInfo", new SBField(DATA_ADD_MSG));

        return "redirect:/expenses/selectAll";
    }

    /**
     * A method is used to modify a expense record from database. It shows form to modify.
     *
     * @param model              It contains all models.
     * @param expenseErrors      A error list from a expense form.
     * @param id                 A modified record ID.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A logical view name or a value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/expenses/modify/{id}", method = RequestMethod.GET)
    public String expensesModifyGet(Model model,
                                    @ModelAttribute("expenseErrors") SBList expenseErrors,
                                    @PathVariable("id") Long id,
                                    RedirectAttributes redirectAttributes)
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

        if (logInUser.getId() != expenseDao.getOne(id).getUser().getId() && !logInUser.getIsAdmin())
        {
            redirectAttributes.addFlashAttribute("isExpenseUserIdInvalid", new SBBoolean(true));

            return "redirect:/expenses/selectAll";
        }

        /* ********************************************************************************** */

        model.addAttribute("expense", expenseDao.getOne(id));
        model.addAttribute("formsOfPayment", formOfPaymentDao.findAll());
        model.addAttribute("expenseCategories", expenseCategoryDao.findAll());

        model.addAttribute("expenseErrors", expenseErrors.getList());

        return expensesModify;
    }

    /**
     * A method is used to modify a expense record. It validates {@link com.justyna.stachera.householdexpenses.domain.main.SBBank} object and modifies it.
     *
     * @param expense            An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBExpense} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/expenses/modify", method = RequestMethod.POST)
    public String expensesModifyPost(@ModelAttribute("expense") @Valid SBExpense expense,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        /* ********************************************************************************** */

        if (logInUser.getId() != expenseDao.getOne(expense.getId()).getUser().getId() && !logInUser.getIsAdmin())
        {
            redirectAttributes.addFlashAttribute("isExpenseUserIdInvalid", new SBBoolean(true));

            return "redirect:/expenses/selectAll";
        }

        /* ********************************************************************************** */

        expense.setKindOfOperation(expenseDao.getOne(expense.getId()).getKindOfOperation());
        expense.setFormOfPayment(formOfPaymentDao.getOne(expense.getFormOfPayment().getId()));
        expense.setExpenseCategory(expenseCategoryDao.getOne(expense.getExpenseCategory().getId()));
        expense.setDaysLeft(expenseDao.getOne(expense.getId()).getDaysLeft());

        SBUser validUser = logInUser.getIsAdmin() ? expenseDao.getOne(expense.getId()).getUser() : logInUser;

        expenseValidator.setUser(validUser);
        expenseValidator.validate(expense, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult.getAllErrors()
                                               .stream()
                                               .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                               .collect(Collectors.toList());

            Optional foundErrorOptional =
                    errors.stream()
                          .filter(p -> p.contains("'java.lang.String' to required type 'java.sql.Date'"))
                          .findAny();

            if (foundErrorOptional.isPresent())
            {
                errors.remove(foundErrorOptional.get());
            }

            redirectAttributes.addFlashAttribute("expenseErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));

            return "redirect:/expenses/modify/" + expense.getId();
        }

        /* ********************************************************************************** */

        expenseCustomDao.modify(expense, validUser);

        redirectAttributes.addFlashAttribute("expenseAddInfo", new SBField(DATA_MODIFY_MSG));

        return "redirect:/expenses/selectAll";
    }

    /**
     * A method is used to sort a expense list from '/expenses/selectAll' website.
     *
     * @param expenseField       A selected option from a sort bar.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/expenses/sort", method = RequestMethod.POST)
    public String expensesSortPost(@ModelAttribute("expenseField") SBField expenseField,
                                   RedirectAttributes redirectAttributes)
    {
        String chosenField = expenseField.getField().split(" - ")[0];
        String chosenOrder = expenseField.getField().split(" - ")[1];

        redirectAttributes.addFlashAttribute("expenseField", new SBField(chosenField + " - " + chosenOrder));
        redirectAttributes.addFlashAttribute("chosenEnum", SBExpenseFields.getEnum(chosenField, chosenOrder));

        return "redirect:/expenses/selectAll";
    }

    /**
     * A method is used to find a record by expense ID.
     *
     * @param expenseId          An inserted ID value.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/expenses/findById", method = RequestMethod.POST)
    public String expensesFindByIdPost(@ModelAttribute("expenseId") SBId expenseId,
                                       RedirectAttributes redirectAttributes)
    {
        redirectAttributes.addFlashAttribute("expenseId", expenseId);

        return "redirect:/expenses/selectAll";
    }
}