package com.justyna.stachera.householdexpenses.controller;

import com.justyna.stachera.householdexpenses.constants.SBWebsitesAndMessages;
import com.justyna.stachera.householdexpenses.dao.custom.SBCapitalisationCustomDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBCapitalisationDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBKindOfLoanDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBLoanDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBUserDao;
import com.justyna.stachera.householdexpenses.domain.enums.SBCapitalisationFields;
import com.justyna.stachera.householdexpenses.domain.helpers.SBField;
import com.justyna.stachera.householdexpenses.domain.helpers.SBList;
import com.justyna.stachera.householdexpenses.domain.main.SBCapitalisation;
import com.justyna.stachera.householdexpenses.domain.main.SBKindOfLoan;
import com.justyna.stachera.householdexpenses.domain.main.SBLoan;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.utils.SBCustomUtils;
import com.justyna.stachera.householdexpenses.validation.SBCapitalisationValidator;
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
 * {@link Controller} class with methods that support '/edit/capitalisations/**' links directly.
 */
@Controller
public class SBEditCapitalisationsController implements SBWebsitesAndMessages
{
    private SBCapitalisationDao capitalisationDao;
    private SBCapitalisationCustomDao capitalisationCustomDao;
    private SBKindOfLoanDao kindOfLoanDao;
    private SBLoanDao loanDao;
    private SBUserDao userDao;
    private SBCapitalisationValidator capitalisationValidator;

    /**
     * An argument constructor autowired interfaces extending
     * {@link org.springframework.data.jpa.repository.JpaRepository} and
     * {@link org.springframework.validation.Validator} objects.
     *
     * @param capitalisationDao       It provides methods related with 'sbcapitalisation' table from database.
     * @param capitalisationCustomDao It provides extra methods related with 'sbcapitalisation' table from database.
     * @param kindOfLoanDao           It provides methods related with 'sbkind_of_loan' table from database.
     * @param loanDao                 It provides methods related with 'sbloan' table from database.
     * @param userDao                 It provides methods related with 'sbuser' table from database.
     * @param capitalisationValidator It validates {@link SBCapitalisation} object fields.
     */
    @Autowired
    public SBEditCapitalisationsController(SBCapitalisationDao capitalisationDao,
                                           SBCapitalisationCustomDao capitalisationCustomDao,
                                           SBKindOfLoanDao kindOfLoanDao,
                                           SBLoanDao loanDao,
                                           SBUserDao userDao,
                                           SBCapitalisationValidator capitalisationValidator)
    {
        this.capitalisationDao = capitalisationDao;
        this.capitalisationCustomDao = capitalisationCustomDao;
        this.kindOfLoanDao = kindOfLoanDao;
        this.loanDao = loanDao;
        this.userDao = userDao;
        this.capitalisationValidator = capitalisationValidator;
    }

    /**
     * A method is used to show a capitalisation list and sort it.
     *
     * @param field        An chosen field from a sort bar.
     * @param chosenEnum   It informs in which column the table has to be sorted.
     * @param addInfo      A message about adding a new record to database.
     * @param modifyInfo   A message about modification a record in database.
     * @param removeInfo   A message about deleting record from database.
     * @param modelAndView It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/capitalisations/selectAll", method = RequestMethod.GET)
    public ModelAndView capitalisationSelectAllGet(@ModelAttribute("capitalisationField") SBField field,
                                                   @ModelAttribute("chosenEnum") String chosenEnum,
                                                   @ModelAttribute("capitalisationAddInfo") SBField addInfo,
                                                   @ModelAttribute("capitalisationModifyInfo") SBField modifyInfo,
                                                   @ModelAttribute("capitalisationRemoveInfo") SBField removeInfo,
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

        modelAndView.setViewName(MENU_EDIT_CAPITALISATIONS);

        modelAndView.addObject("capitalisations", capitalisationCustomDao.sortTableBy(chosenEnum));
        modelAndView.addObject("capitalisationsSize", capitalisationDao.findAll().size());
        modelAndView.addObject("capitalisationFields", SBCapitalisationFields.getList());

        modelAndView.addObject("capitalisationAddInfo", addInfo);
        modelAndView.addObject("capitalisationRemoveInfo", removeInfo);
        modelAndView.addObject("capitalisationModifyInfo", modifyInfo);

        return modelAndView;
    }

    /**
     * A method is used to remove a capitalisation record from database.
     *
     * @param id                 A removed record ID.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/capitalisations/remove/{id}", method = RequestMethod.GET)
    public String capitalisationRemoveGet(@PathVariable("id") Long id,
                                          RedirectAttributes redirectAttributes)
    {
        SBCapitalisation capitalisation = capitalisationDao.getOne(id);
        List<SBKindOfLoan> kindsOfLoan = kindOfLoanDao.findAllByCapitalisation(capitalisation);
        List<SBLoan> loans = loanDao.findAllByKindOfLoan(kindsOfLoan);

        loanDao.delete(loans);
        kindOfLoanDao.delete(kindsOfLoan);
        capitalisationDao.delete(id);

        redirectAttributes.addFlashAttribute("capitalisationRemoveInfo", new SBField(DATA_REMOVE_MSG));

        return "redirect:/edit/capitalisations/selectAll";
    }

    /**
     * A method is used to add a new capitalisation record to database. It shows form to complete.
     *
     * @param capitalisationErrors A error list from a capitalisation form.
     * @param capitalisation       An object where the data from a form is located.
     * @param modelAndView         It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/capitalisations/add", method = RequestMethod.GET)
    public ModelAndView capitalisationAddGet(@ModelAttribute("capitalisationErrors") SBList capitalisationErrors,
                                             @ModelAttribute("capitalisation") SBCapitalisation capitalisation,
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

        modelAndView.setViewName(MENU_EDIT_CAPITALISATIONS_ADD);

        modelAndView.addObject("capitalisation", capitalisation);

        modelAndView.addObject("capitalisationErrors", capitalisationErrors.getList());

        return modelAndView;
    }

    /**
     * A method is used to add a new capitalisation record. It validates {@link SBCapitalisation} object and saves it
     * in database.
     *
     * @param capitalisation     An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBCapitalisation} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/capitalisations/add", method = RequestMethod.POST)
    public String capitalisationAddPost(@ModelAttribute("capitalisation") @Valid SBCapitalisation capitalisation,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes)
    {
        capitalisationValidator.validate(capitalisation, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("capitalisation", capitalisation);
            redirectAttributes.addFlashAttribute("capitalisationErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));

            return "redirect:/edit/capitalisations/add";
        }

        capitalisationDao.save(capitalisation);

        redirectAttributes.addFlashAttribute("capitalisationAddInfo", new SBField(DATA_ADD_MSG));

        return "redirect:/edit/capitalisations/selectAll";
    }

    /**
     * A method is used to modify a capitalisation record from database. It shows form to modify.
     *
     * @param modelAndView         It contains all models and a logical view name.
     * @param capitalisationErrors A error list from a capitalisation form.
     * @param id                   A modified record ID.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/capitalisations/modify/{id}", method = RequestMethod.GET)
    public ModelAndView capitalisationModifyGet(ModelAndView modelAndView,
                                                @ModelAttribute("capitalisationErrors") SBList capitalisationErrors,
                                                @PathVariable("id") Long id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser user = userDao.findByUsername(username);

        modelAndView.addObject("logInUser", username);
        modelAndView.addObject("isAdmin", user.getIsAdmin());

        /* ********************************************************************************** */

        modelAndView.setViewName(MENU_EDIT_CAPITALISATIONS_MODIFY);

        modelAndView.addObject("capitalisation", capitalisationDao.getOne(id));

        modelAndView.addObject("capitalisationErrors", capitalisationErrors.getList());

        return modelAndView;
    }

    /**
     * A method is used to modify a capitalisation record. It validates {@link SBCapitalisation} object and modifies it.
     *
     * @param capitalisation     An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBCapitalisation} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/capitalisations/modify", method = RequestMethod.POST)
    public String capitalisationModifyPost(@ModelAttribute("capitalisation") @Valid SBCapitalisation capitalisation,
                                           BindingResult bindingResult,
                                           RedirectAttributes redirectAttributes)
    {
        capitalisationValidator.validate(capitalisation, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> bindingResults = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("capitalisationErrors",
                                                 new SBList(
                                                         SBCustomUtils.convertStringListToSBFieldList(bindingResults)));

            return "redirect:/edit/capitalisations/modify/" + capitalisation.getId();
        }

        capitalisationCustomDao.modify(capitalisation);

        redirectAttributes.addFlashAttribute("capitalisationModifyInfo", new SBField(DATA_MODIFY_MSG));

        return "redirect:/edit/capitalisations/selectAll";
    }

    /**
     * A method is used to sort a capitalisation list from '/edit/capitalisations/selectAll' website.
     *
     * @param field              A selected option from a sort bar.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/capitalisations/sort", method = RequestMethod.POST)
    public String capitalisationSortPost(@ModelAttribute("capitalisationField") SBField field,
                                         RedirectAttributes redirectAttributes)
    {
        String chosenField = field.getField().split(" - ")[0];
        String chosenOrder = field.getField().split(" - ")[1];

        redirectAttributes.addFlashAttribute("capitalisationField", new SBField(chosenField + " - " + chosenOrder));
        redirectAttributes.addFlashAttribute("chosenEnum", SBCapitalisationFields.getEnum(chosenField, chosenOrder));

        return "redirect:/edit/capitalisations/selectAll";
    }
}