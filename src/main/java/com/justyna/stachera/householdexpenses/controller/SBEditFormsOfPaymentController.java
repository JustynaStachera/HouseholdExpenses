package com.justyna.stachera.householdexpenses.controller;

import com.justyna.stachera.householdexpenses.constants.SBWebsitesAndMessages;
import com.justyna.stachera.householdexpenses.dao.custom.SBFormOfPaymentCustomDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBExpenseDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBFormOfPaymentDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBUserDao;
import com.justyna.stachera.householdexpenses.domain.enums.SBFormOfPaymentFields;
import com.justyna.stachera.householdexpenses.domain.helpers.SBField;
import com.justyna.stachera.householdexpenses.domain.helpers.SBList;
import com.justyna.stachera.householdexpenses.domain.main.SBExpense;
import com.justyna.stachera.householdexpenses.domain.main.SBFormOfPayment;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.utils.SBCustomUtils;
import com.justyna.stachera.householdexpenses.validation.SBFormOfPaymentValidator;
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
 * {@link Controller} class with methods that support '/edit/formsOfPayment/**' links directly.
 */
@Controller
public class SBEditFormsOfPaymentController implements SBWebsitesAndMessages
{
    private SBFormOfPaymentDao formOfPaymentDao;
    private SBFormOfPaymentCustomDao formOfPaymentCustomDao;
    private SBExpenseDao expenseDao;
    private SBUserDao userDao;
    private SBFormOfPaymentValidator formOfPaymentValidator;

    /**
     * An argument constructor autowired interfaces extending
     * {@link org.springframework.data.jpa.repository.JpaRepository} and
     * {@link org.springframework.validation.Validator} objects.
     *
     * @param formOfPaymentDao       It provides methods related with 'sbform_of_payment' table from database.
     * @param formOfPaymentCustomDao It provides extra methods related with 'sbform_of_payment' table from database.
     * @param expenseDao             It provides methods related with 'sbexpense' table from database.
     * @param userDao                It provides methods related with {@link SBUser} table from database.
     * @param formOfPaymentValidator It validates {@link SBFormOfPayment} object fields.
     */
    @Autowired
    public SBEditFormsOfPaymentController(SBFormOfPaymentDao formOfPaymentDao,
                                          SBFormOfPaymentCustomDao formOfPaymentCustomDao,
                                          SBExpenseDao expenseDao,
                                          SBUserDao userDao,
                                          SBFormOfPaymentValidator formOfPaymentValidator)
    {
        this.formOfPaymentDao = formOfPaymentDao;
        this.formOfPaymentCustomDao = formOfPaymentCustomDao;
        this.expenseDao = expenseDao;
        this.userDao = userDao;
        this.formOfPaymentValidator = formOfPaymentValidator;
    }

    /**
     * A method is used to show a form of payment list and sort it.
     *
     * @param field        An chosen field from a sort bar.
     * @param chosenEnum   It informs in which column the table has to be sorted.
     * @param addInfo      A message about adding a new record to database.
     * @param modifyInfo   A message about modification a record in database.
     * @param removeInfo   A message about deleting record from database.
     * @param modelAndView It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/formsOfPayment/selectAll", method = RequestMethod.GET)
    public ModelAndView formOfPaymentSelectAllGet(@ModelAttribute("formOfPaymentField") SBField field,
                                                  @ModelAttribute("chosenEnum") String chosenEnum,
                                                  @ModelAttribute("formOfPaymentAddInfo") SBField addInfo,
                                                  @ModelAttribute("formOfPaymentModifyInfo") SBField modifyInfo,
                                                  @ModelAttribute("formOfPaymentRemoveInfo") SBField removeInfo,
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

        modelAndView.setViewName(MENU_EDIT_FORMS_OF_PAYMENT);

        modelAndView.addObject("formsOfPayment", formOfPaymentCustomDao.sortTableBy(chosenEnum));
        modelAndView.addObject("formsOfPaymentSize", formOfPaymentDao.findAll().size());
        modelAndView.addObject("formOfPaymentFields", SBFormOfPaymentFields.getList());

        modelAndView.addObject("formOfPaymentAddInfo", addInfo);
        modelAndView.addObject("formOfPaymentRemoveInfo", removeInfo);
        modelAndView.addObject("formOfPaymentModifyInfo", modifyInfo);

        return modelAndView;
    }

    /**
     * A method is used to remove a form of payment record from database.
     *
     * @param id                 A removed record ID.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/formsOfPayment/remove/{id}", method = RequestMethod.GET)
    public String formOfPaymentRemoveGet(@PathVariable("id") Long id,
                                         RedirectAttributes redirectAttributes)
    {
        SBFormOfPayment formOfPayment = formOfPaymentDao.getOne(id);
        List<SBExpense> expenses = expenseDao.findAllByFormOfPayment(formOfPayment);

        expenseDao.delete(expenses);
        formOfPaymentDao.delete(id);

        redirectAttributes.addFlashAttribute("formOfPaymentRemoveInfo", new SBField(DATA_REMOVE_MSG));

        return "redirect:/edit/formsOfPayment/selectAll";
    }

    /**
     * A method is used to add a new form of payment record to database. It shows form to complete.
     *
     * @param formOfPaymentErrors A error list from a form of payment form.
     * @param formOfPayment       An object where the data from a form is located.
     * @param modelAndView        It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/formsOfPayment/add", method = RequestMethod.GET)
    public ModelAndView formOfPaymentAddGet(@ModelAttribute("formOfPaymentErrors") SBList formOfPaymentErrors,
                                            @ModelAttribute("formOfPayment") SBFormOfPayment formOfPayment,
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

        modelAndView.setViewName(MENU_EDIT_FORMS_OF_PAYMENT_ADD);

        modelAndView.addObject("formOfPayment", formOfPayment);

        modelAndView.addObject("formOfPaymentErrors", formOfPaymentErrors.getList());

        return modelAndView;
    }

    /**
     * A method is used to add a new form of payment record. It validates {@link SBFormOfPayment} object
     * and saves it in database.
     *
     * @param formOfPayment      An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBFormOfPayment} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/formsOfPayment/add", method = RequestMethod.POST)
    public String formOfPaymentAddPost(@ModelAttribute("formOfPayment") @Valid SBFormOfPayment formOfPayment,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes)
    {
        formOfPaymentValidator.validate(formOfPayment, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("formOfPayment", formOfPayment);
            redirectAttributes.addFlashAttribute("formOfPaymentErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));

            return "redirect:/edit/formsOfPayment/add";
        }

        formOfPaymentDao.save(formOfPayment);

        redirectAttributes.addFlashAttribute("formOfPaymentAddInfo", new SBField(DATA_ADD_MSG));

        return "redirect:/edit/formsOfPayment/selectAll";
    }

    /**
     * A method is used to modify a form of payment record from database. It shows form to modify.
     *
     * @param modelAndView        It contains all models and a logical view name.
     * @param formOfPaymentErrors A error list from a form of payment form.
     * @param id                  A modified record ID.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/formsOfPayment/modify/{id}", method = RequestMethod.GET)
    public ModelAndView formOfPaymentModifyGet(ModelAndView modelAndView,
                                               @ModelAttribute("formOfPaymentErrors") SBList formOfPaymentErrors,
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

        modelAndView.setViewName(MENU_EDIT_FORMS_OF_PAYMENT_MODIFY);

        modelAndView.addObject("formOfPayment", formOfPaymentDao.getOne(id));

        modelAndView.addObject("formOfPaymentErrors", formOfPaymentErrors.getList());

        return modelAndView;
    }

    /**
     * A method is used to modify a form of payment record. It validates {@link SBFormOfPayment} object and modifies it.
     *
     * @param formOfPayment      An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBFormOfPayment} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/formsOfPayment/modify", method = RequestMethod.POST)
    public String formOfPaymentModifyPost(@ModelAttribute("formOfPayment") @Valid SBFormOfPayment formOfPayment,
                                          BindingResult bindingResult,
                                          RedirectAttributes redirectAttributes)
    {
        formOfPaymentValidator.validate(formOfPayment, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("formOfPaymentErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));

            return "redirect:/edit/formsOfPayment/modify/" + formOfPayment.getId();
        }

        formOfPaymentCustomDao.modify(formOfPayment);

        redirectAttributes.addFlashAttribute("formOfPaymentModifyInfo", new SBField(DATA_MODIFY_MSG));

        return "redirect:/edit/formsOfPayment/selectAll";
    }

    /**
     * A method is used to sort a form of payment list from '/edit/formOfPayment/selectAll' website.
     *
     * @param field              A selected option from a sort bar.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/formsOfPayment/sort", method = RequestMethod.POST)
    public String formOfPaymentSortPost(@ModelAttribute("formOfPaymentField") SBField field,
                                        RedirectAttributes redirectAttributes)
    {
        String chosenField = field.getField().split(" - ")[0];
        String chosenOrder = field.getField().split(" - ")[1];

        redirectAttributes.addFlashAttribute("formOfPaymentField", new SBField(chosenField + " - " + chosenOrder));
        redirectAttributes.addFlashAttribute("chosenEnum", SBFormOfPaymentFields.getEnum(chosenField, chosenOrder));

        return "redirect:/edit/formsOfPayment/selectAll";
    }
}
