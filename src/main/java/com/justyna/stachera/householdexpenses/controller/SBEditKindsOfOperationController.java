package com.justyna.stachera.householdexpenses.controller;

import com.justyna.stachera.householdexpenses.constants.SBWebsitesAndMessages;
import com.justyna.stachera.householdexpenses.dao.custom.SBKindOfOperationCustomDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBExpenseDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBKindOfOperationDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBUserDao;
import com.justyna.stachera.householdexpenses.domain.enums.SBKindOfOperationFields;
import com.justyna.stachera.householdexpenses.domain.helpers.SBField;
import com.justyna.stachera.householdexpenses.domain.helpers.SBList;
import com.justyna.stachera.householdexpenses.domain.main.SBExpense;
import com.justyna.stachera.householdexpenses.domain.main.SBKindOfOperation;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.utils.SBCustomUtils;
import com.justyna.stachera.householdexpenses.validation.SBKindOfOperationValidator;
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
 * {@link Controller} class with methods that support '/edit/kindsOfOperation/**' links directly.
 */
@Controller
public class SBEditKindsOfOperationController implements SBWebsitesAndMessages
{
    private SBKindOfOperationDao kindOfOperationDao;
    private SBKindOfOperationCustomDao kindOfOperationsCustomDao;
    private SBExpenseDao expenseDao;
    private SBUserDao userDao;
    private SBKindOfOperationValidator kindOfOperationValidator;

    /**
     * An argument constructor autowired interfaces extending
     * {@link org.springframework.data.jpa.repository.JpaRepository} and
     * {@link org.springframework.validation.Validator} objects.
     *
     * @param kindOfOperationDao        It provides methods related with 'sbkind_of_operation' table from database.
     * @param kindOfOperationsCustomDao It provides extra methods related with 'sbkind_of_operation' table from
     *                                  database.
     * @param expenseDao                It provides methods related with 'sbexpense' table from database.
     * @param userDao                   It provides methods related with 'sbuser' table from database.
     * @param kindOfOperationValidator  It validates {@link SBKindOfOperation} object fields.
     */
    @Autowired
    public SBEditKindsOfOperationController(SBKindOfOperationDao kindOfOperationDao,
                                            SBKindOfOperationCustomDao kindOfOperationsCustomDao,
                                            SBExpenseDao expenseDao,
                                            SBUserDao userDao,
                                            SBKindOfOperationValidator kindOfOperationValidator)
    {
        this.kindOfOperationDao = kindOfOperationDao;
        this.kindOfOperationsCustomDao = kindOfOperationsCustomDao;
        this.expenseDao = expenseDao;
        this.userDao = userDao;
        this.kindOfOperationValidator = kindOfOperationValidator;
    }

    /**
     * A method is used to a kind of operation bank list and sort it.
     *
     * @param field        An chosen field from a sort bar.
     * @param chosenEnum   It informs in which column the table has to be sorted.
     * @param addInfo      A message about adding a new record to database.
     * @param modifyInfo   A message about modification a record in database.
     * @param removeInfo   A message about deleting record from database.
     * @param modelAndView It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/kindsOfOperation/selectAll", method = RequestMethod.GET)
    public ModelAndView kindOfOperationSelectAllGet(@ModelAttribute("kindOfOperationField") SBField field,
                                                    @ModelAttribute("chosenEnum") String chosenEnum,
                                                    @ModelAttribute("kindOfOperationAddInfo") SBField addInfo,
                                                    @ModelAttribute("kindOfOperationModifyInfo") SBField modifyInfo,
                                                    @ModelAttribute("kindOfOperationRemoveInfo") SBField removeInfo,
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

        modelAndView.setViewName(MENU_EDIT_KINDS_OF_OPERATION);

        modelAndView.addObject("kindsOfOperation", kindOfOperationsCustomDao.sortTableBy(chosenEnum));
        modelAndView.addObject("kindsOfOperationSize", kindOfOperationDao.findAll().size());
        modelAndView.addObject("kindOfOperationFields", SBKindOfOperationFields.getList());

        modelAndView.addObject("kindOfOperationAddInfo", addInfo);
        modelAndView.addObject("kindOfOperationRemoveInfo", removeInfo);
        modelAndView.addObject("kindOfOperationModifyInfo", modifyInfo);

        return modelAndView;
    }

    /**
     * A method is used to remove a kind of operation record from database.
     *
     * @param id                 A removed record ID.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/kindsOfOperation/remove/{id}", method = RequestMethod.GET)
    public String kindOfOperationRemoveGet(@PathVariable("id") Long id,
                                           RedirectAttributes redirectAttributes)
    {
        SBKindOfOperation kindOfOperation = kindOfOperationDao.getOne(id);
        List<SBExpense> expenses = expenseDao.findAllByKindOfOperation(kindOfOperation);

        expenseDao.delete(expenses);
        kindOfOperationDao.delete(id);

        redirectAttributes.addFlashAttribute("kindOfOperationRemoveInfo", new SBField(DATA_REMOVE_MSG));

        return "redirect:/edit/kindsOfOperation/selectAll";
    }

    /**
     * A method is used to add a new bank record to database. It shows form to complete.
     *
     * @param kindOfOperationErrors A error list from a kind of loan form.
     * @param kindOfOperation       An object where the data from a form is located.
     * @param modelAndView          It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/kindsOfOperation/add", method = RequestMethod.GET)
    public ModelAndView kindOfOperationAddGet(@ModelAttribute("kindOfOperationErrors") SBList kindOfOperationErrors,
                                              @ModelAttribute("kindOfOperation") SBKindOfOperation kindOfOperation,
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

        modelAndView.setViewName(MENU_EDIT_KINDS_OF_OPERATION_ADD);

        modelAndView.addObject("kindOfOperation", kindOfOperation);

        modelAndView.addObject("kindOfOperationErrors", kindOfOperationErrors.getList());

        return modelAndView;
    }

    /**
     * A method is used to add a new kind of operation record. It validates {@link SBKindOfOperation} object
     * and saves it in database.
     *
     * @param kindOfOperation    An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBKindOfOperation}
     *                           object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/kindsOfOperation/add", method = RequestMethod.POST)
    public String kindOfOperationAddPost(@ModelAttribute("kindOfOperation") @Valid SBKindOfOperation kindOfOperation,
                                         BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes)
    {
        kindOfOperationValidator.validate(kindOfOperation, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("kindOfOperation", kindOfOperation);
            redirectAttributes.addFlashAttribute("kindOfOperationErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));

            return "redirect:/edit/kindsOfOperation/add";
        }

        kindOfOperationDao.save(kindOfOperation);

        redirectAttributes.addFlashAttribute("kindOfOperationAddInfo", new SBField(DATA_ADD_MSG));

        return "redirect:/edit/kindsOfOperation/selectAll";
    }

    /**
     * A method is used to modify a kind of operation record from database. It shows form to modify.
     *
     * @param modelAndView          It contains all models and a logical view name.
     * @param kindOfOperationErrors A error list from a kind of loan form.
     * @param id                    A modified record ID.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/kindsOfOperation/modify/{id}", method = RequestMethod.GET)
    public ModelAndView kindOfOperationModifyGet(ModelAndView modelAndView,
                                                 @ModelAttribute("kindOfOperationErrors") SBList kindOfOperationErrors,
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

        modelAndView.setViewName(MENU_EDIT_KINDS_OF_OPERATION_MODIFY);

        modelAndView.addObject("kindOfOperation", kindOfOperationDao.getOne(id));

        modelAndView.addObject("kindOfOperationErrors", kindOfOperationErrors.getList());

        return modelAndView;
    }

    /**
     * A method is used to modify a kind of operation record. It validates {@link SBKindOfOperation} object and
     * modifies it.
     *
     * @param kindOfOperation    An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBKindOfOperation}
     *                           object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/kindsOfOperation/modify", method = RequestMethod.POST)
    public String kindOfOperationModifyPost(@ModelAttribute("kindOfOperation") @Valid SBKindOfOperation kindOfOperation,
                                            BindingResult bindingResult,
                                            RedirectAttributes redirectAttributes)
    {
        kindOfOperationValidator.validate(kindOfOperation, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("kindOfOperationErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));

            return "redirect:/edit/kindsOfOperation/modify/" + kindOfOperation.getId();
        }

        kindOfOperationsCustomDao.modify(kindOfOperation);

        redirectAttributes.addFlashAttribute("kindOfOperationModifyInfo", new SBField(DATA_MODIFY_MSG));

        return "redirect:/edit/kindsOfOperation/selectAll";
    }

    /**
     * A method is used to sort a bank list from '/edit/kindsOfOperation/selectAll' website.
     *
     * @param field              A selected option from a sort bar.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/kindsOfOperation/sort", method = RequestMethod.POST)
    public String kindOfOperationSortPost(@ModelAttribute("kindOfOperationField") SBField field,
                                          RedirectAttributes redirectAttributes)
    {
        String chosenField = field.getField().split(" - ")[0];
        String chosenOrder = field.getField().split(" - ")[1];

        redirectAttributes.addFlashAttribute("kindOfOperationField", new SBField(chosenField + " - " + chosenOrder));
        redirectAttributes.addFlashAttribute("chosenEnum", SBKindOfOperationFields.getEnum(chosenField, chosenOrder));

        return "redirect:/edit/kindsOfOperation/selectAll";
    }
}