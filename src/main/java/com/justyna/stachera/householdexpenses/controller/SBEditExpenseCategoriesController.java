package com.justyna.stachera.householdexpenses.controller;

import com.justyna.stachera.householdexpenses.constants.SBWebsitesAndMessages;
import com.justyna.stachera.householdexpenses.dao.custom.SBExpenseCategoryCustomDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBExpenseCategoryDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBExpenseDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBUserDao;
import com.justyna.stachera.householdexpenses.domain.enums.SBExpenseCategoryFields;
import com.justyna.stachera.householdexpenses.domain.helpers.SBField;
import com.justyna.stachera.householdexpenses.domain.helpers.SBList;
import com.justyna.stachera.householdexpenses.domain.main.SBExpense;
import com.justyna.stachera.householdexpenses.domain.main.SBExpenseCategory;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.utils.SBCustomUtils;
import com.justyna.stachera.householdexpenses.validation.SBExpenseCategoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
 * {@link Controller} class with methods that support '/edit/expenseCategories/**' links directly.
 */
@Controller
public class SBEditExpenseCategoriesController implements SBWebsitesAndMessages
{
    private SBExpenseCategoryDao expenseCategoryDao;
    private SBExpenseCategoryCustomDao expenseCategoryCustomDao;
    private SBExpenseDao expenseDao;
    private SBUserDao userDao;
    private SBExpenseCategoryValidator expenseCategoryValidator;

    /**
     * An argument constructor autowired interfaces extending {@link org.springframework.data.jpa.repository.JpaRepository} and {@link org.springframework.validation.Validator} objects.
     *
     * @param expenseCategoryDao       It provides methods related with 'sbexpense_category' table from database.
     * @param expenseCategoryCustomDao It provides extra methods related with 'sbexpense_category' table from database.
     * @param expenseDao               It provides methods related with 'sbexpense' table from database.
     * @param userDao                  It provides methods related with 'sbuser' table from database.
     * @param expenseCategoryValidator It validates {@link SBExpenseCategory} object fields.
     */
    @Autowired
    public SBEditExpenseCategoriesController(SBExpenseCategoryDao expenseCategoryDao,
                                             SBExpenseCategoryCustomDao expenseCategoryCustomDao,
                                             SBExpenseDao expenseDao,
                                             SBUserDao userDao,
                                             SBExpenseCategoryValidator expenseCategoryValidator)
    {
        this.expenseCategoryDao = expenseCategoryDao;
        this.expenseCategoryCustomDao = expenseCategoryCustomDao;
        this.expenseDao = expenseDao;
        this.userDao = userDao;
        this.expenseCategoryValidator = expenseCategoryValidator;
    }

    /**
     * A method is used to show an expense category list and sort it.
     *
     * @param field        An chosen field from a sort bar.
     * @param chosenEnum   It informs in which column the table has to be sorted.
     * @param modelAndView It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/expenseCategories/selectAll", method = RequestMethod.GET)
    public ModelAndView expenseCategorySelectAllGet(@ModelAttribute("expenseCategoryField") SBField field,
                                                    @ModelAttribute("chosenEnum") String chosenEnum,
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

        modelAndView.setViewName(menuEditExpenseCategories);

        modelAndView.addObject("expenseCategories", expenseCategoryCustomDao.sortTableBy(chosenEnum));
        modelAndView.addObject("expenseCategoriesSize", expenseCategoryDao.findAll().size());
        modelAndView.addObject("expenseCategoryFields", SBExpenseCategoryFields.getList());

        return modelAndView;
    }

    /**
     * A method is used to remove a expense category record from database.
     *
     * @param id                 A removed record ID.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/expenseCategories/remove/{id}", method = RequestMethod.GET)
    public String expenseCategoryRemoveGet(@PathVariable("id") Long id,
                                           RedirectAttributes redirectAttributes)
    {
        SBExpenseCategory expenseCategory = expenseCategoryDao.getOne(id);
        List<SBExpense> expenses = expenseDao.findAllByExpenseCategory(expenseCategory);

        expenseDao.delete(expenses);
        expenseCategoryDao.delete(id);

        redirectAttributes.addFlashAttribute("expenseCategoryRemoveInfo", new SBField(DATA_REMOVE_MSG));

        return "redirect:/edit/expenseCategories/selectAll";
    }

    /**
     * A method is used to add a new expense category record to database. It shows form to complete.
     *
     * @param expenseCategory       An object where the data from a form is located.
     * @param expenseCategoryErrors A error list from an expense category form.
     * @param modelAndView          It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/expenseCategories/add", method = RequestMethod.GET)
    public ModelAndView expenseCategoryAddGet(@ModelAttribute("expenseCategory") SBExpenseCategory expenseCategory,
                                              @ModelAttribute("expenseCategoryErrors") SBList expenseCategoryErrors,
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

        modelAndView.setViewName(menuEditExpenseCategoriesAdd);

        modelAndView.addObject("expenseCategory", expenseCategory);

        modelAndView.addObject("expenseCategoryErrors", expenseCategoryErrors.getList());

        return modelAndView;
    }

    /**
     * A method is used to add a new expense category record. It validates {@link SBExpenseCategory} object
     * and saves it in database.
     *
     * @param expenseCategory    An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBExpenseCategory} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/expenseCategories/add", method = RequestMethod.POST)
    public String expenseCategoryAddPost(@ModelAttribute("expenseCategory") @Valid SBExpenseCategory expenseCategory,
                                         BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes)
    {
        expenseCategoryValidator.validate(expenseCategory, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("expenseCategory", expenseCategory);
            redirectAttributes.addFlashAttribute("expenseCategoryErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));

            return "redirect:/edit/expenseCategories/add";
        }

        expenseCategoryDao.save(expenseCategory);

        redirectAttributes.addFlashAttribute("expenseCategoryAddInfo", new SBField(DATA_ADD_MSG));

        return "redirect:/edit/expenseCategories/selectAll";
    }

    /**
     * A method is used to modify a expense category record from database. It shows form to modify.
     *
     * @param modelAndView          It contains all models and a logical view name.
     * @param expenseCategoryErrors A error list from a expense category form.
     * @param id                    A modified record ID.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/expenseCategories/modify/{id}", method = RequestMethod.GET)
    public ModelAndView expensesCategoryModifyGet(ModelAndView modelAndView,
                                                  @ModelAttribute("expenseCategoryErrors") SBList expenseCategoryErrors,
                                                  @PathVariable("id") Long id)
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

        modelAndView.setViewName(menuEditExpenseCategoriesModify);

        modelAndView.addObject("expenseCategory", expenseCategoryDao.getOne(id));

        modelAndView.addObject("expenseCategoryErrors", expenseCategoryErrors.getList());

        return modelAndView;
    }

    /**
     * A method is used to modify a expense category record. It validates {@link SBExpenseCategory} object and modifies it.
     *
     * @param expenseCategory    An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBExpenseCategory} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/expenseCategories/modify", method = RequestMethod.POST)
    public String expensesCategoryModifyPost(
            @ModelAttribute("expenseCategory") @Valid SBExpenseCategory expenseCategory,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes)
    {
        expenseCategoryValidator.validate(expenseCategory, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("expenseCategoryErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));

            return "redirect:/edit/expenseCategories/modify/" + expenseCategory.getId();
        }

        expenseCategoryCustomDao.modify(expenseCategory);

        redirectAttributes.addFlashAttribute("expenseCategoryModifyInfo", new SBField(DATA_MODIFY_MSG));

        return "redirect:/edit/expenseCategories/selectAll";
    }

    /**
     * A method is used to sort a expense category list from '/edit/expenseCategories/selectAll' website.
     *
     * @param field              A selected option from a sort bar.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/expenseCategories/sort", method = RequestMethod.POST)
    public String expenseCategorySortPost(@ModelAttribute("expenseCategoryField") SBField field,
                                          RedirectAttributes redirectAttributes)
    {
        String chosenField = field.getField().split(" - ")[0];
        String chosenOrder = field.getField().split(" - ")[1];

        redirectAttributes.addFlashAttribute("expenseCategoryField", new SBField(chosenField + " - " + chosenOrder));
        redirectAttributes.addFlashAttribute("chosenEnum", SBExpenseCategoryFields.getEnum(chosenField, chosenOrder));

        return "redirect:/edit/expenseCategories/selectAll";
    }
}