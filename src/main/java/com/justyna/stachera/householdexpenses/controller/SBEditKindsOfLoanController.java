package com.justyna.stachera.householdexpenses.controller;

import com.justyna.stachera.householdexpenses.constants.SBWebsitesAndMessages;
import com.justyna.stachera.householdexpenses.dao.custom.SBKindOfLoanCustomDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.*;
import com.justyna.stachera.householdexpenses.domain.enums.SBKindOfLoanFields;
import com.justyna.stachera.householdexpenses.domain.helpers.SBField;
import com.justyna.stachera.householdexpenses.domain.helpers.SBList;
import com.justyna.stachera.householdexpenses.domain.joined.SBKindOfLoanJoined;
import com.justyna.stachera.householdexpenses.domain.main.SBKindOfLoan;
import com.justyna.stachera.householdexpenses.domain.main.SBLoan;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.service.SBKindOfLoanJoinedService;
import com.justyna.stachera.householdexpenses.utils.SBCustomUtils;
import com.justyna.stachera.householdexpenses.validation.SBKindOfLoanJoinedValidator;
import com.justyna.stachera.householdexpenses.validation.SBKindOfLoanValidator;
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
 * {@link Controller} class with methods that support '/edit/kindsOfLoan/**' links directly.
 */
@Controller
public class SBEditKindsOfLoanController implements SBWebsitesAndMessages
{
    private SBKindOfLoanDao kindOfLoanDao;
    private SBKindOfLoanCustomDao kindOfLoanCustomDao;
    private SBBankDao bankDao;
    private SBCapitalisationDao capitalisationDao;
    private SBLoanDao loanDao;
    private SBKindOfLoanJoinedService kindOfLoanJoinedService;
    private SBUserDao userDao;
    private SBKindOfLoanValidator kindOfLoanValidator;
    private SBKindOfLoanJoinedValidator kindOfLoanJoinedValidator;

    /**
     * An argument constructor autowired interfaces extending {@link org.springframework.data.jpa.repository.JpaRepository} and {@link org.springframework.validation.Validator} objects.
     *
     * @param kindOfLoanDao             It provides methods related with 'sbkind_of_loan' table from database.
     * @param kindOfLoanCustomDao       It provides extra methods related with 'sbkind_of_loan' table from database.
     * @param bankDao                   It provides methods related with 'sbbank' table from database.
     * @param capitalisationDao         It provides methods related with 'sbcapitalisation' table from database.
     * @param loanDao                   It provides methods related with 'sbloan' table from database.
     * @param kindOfLoanJoinedService   It provides methods related with 'sbbank' and 'sbkind_of_loan'
     *                                  tables from database.
     * @param userDao                   It provides methods related with 'sbuser' table from database.
     * @param kindOfLoanValidator       It validates {@link SBKindOfLoan} object fields.
     * @param kindOfLoanJoinedValidator It validates {@link SBKindOfLoanJoined} object fields.
     */
    @Autowired
    public SBEditKindsOfLoanController(SBKindOfLoanDao kindOfLoanDao,
                                       SBKindOfLoanCustomDao kindOfLoanCustomDao,
                                       SBBankDao bankDao,
                                       SBCapitalisationDao capitalisationDao,
                                       SBLoanDao loanDao,
                                       SBKindOfLoanJoinedService kindOfLoanJoinedService,
                                       SBUserDao userDao,
                                       SBKindOfLoanValidator kindOfLoanValidator,
                                       SBKindOfLoanJoinedValidator kindOfLoanJoinedValidator)
    {
        this.kindOfLoanDao = kindOfLoanDao;
        this.kindOfLoanCustomDao = kindOfLoanCustomDao;
        this.bankDao = bankDao;
        this.capitalisationDao = capitalisationDao;
        this.loanDao = loanDao;
        this.kindOfLoanJoinedService = kindOfLoanJoinedService;
        this.userDao = userDao;
        this.kindOfLoanValidator = kindOfLoanValidator;
        this.kindOfLoanJoinedValidator = kindOfLoanJoinedValidator;
    }

    /**
     * A method is used to show a kind of loan list and sort it.
     *
     * @param field        An chosen field from a sort bar.
     * @param chosenEnum   It informs in which column the table has to be sorted.
     * @param addInfo      A message about adding a new record to database.
     * @param modifyInfo   A message about modification a record in database.
     * @param removeInfo   A message about deleting record from database.
     * @param modelAndView It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/kindsOfLoan/selectAll", method = RequestMethod.GET)
    public ModelAndView kindsOfLoanSelectAllGet(@ModelAttribute("kindOfLoanField") SBField field,
                                                @ModelAttribute("chosenEnum") String chosenEnum,
                                                @ModelAttribute("kindOfLoanAddInfo") SBField addInfo,
                                                @ModelAttribute("kindOfLoanModifyInfo") SBField modifyInfo,
                                                @ModelAttribute("kindOfLoanRemoveInfo") SBField removeInfo,
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

        modelAndView.setViewName(menuEditKindsOfLoan);

        modelAndView.addObject("kindsOfLoan", kindOfLoanCustomDao.sortTableBy(chosenEnum));
        modelAndView.addObject("kindsOfLoanSize", kindOfLoanDao.findAll().size());
        modelAndView.addObject("banksSize", bankDao.findAll().size());
        modelAndView.addObject("capitalisationsSize", capitalisationDao.findAll().size());
        modelAndView.addObject("kindOfLoanFields", SBKindOfLoanFields.getList());

        modelAndView.addObject("kindOfLoanAddInfo", addInfo);
        modelAndView.addObject("kindOfLoanRemoveInfo", removeInfo);
        modelAndView.addObject("kindOfLoanModifyInfo", modifyInfo);

        return modelAndView;
    }

    /**
     * A method is used to remove a kind of loan record from database.
     *
     * @param id                 A removed record ID.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/kindsOfLoan/remove/{id}", method = RequestMethod.GET)
    public String kindsOfLoanRemoveGet(@PathVariable("id") Long id,
                                       RedirectAttributes redirectAttributes)
    {
        SBKindOfLoan kindOfLoan = kindOfLoanDao.getOne(id);
        List<SBLoan> loans = loanDao.findAllByKindOfLoan(kindOfLoan);

        loanDao.delete(loans);
        kindOfLoanDao.delete(id);

        redirectAttributes.addFlashAttribute("kindOfLoanRemoveInfo", new SBField(DATA_REMOVE_MSG));

        return "redirect:/edit/kindsOfLoan/selectAll";
    }

    /**
     * A method is used to add a new kind of loan record to database. It shows form to complete.
     *
     * @param kindOfLoanErrors A error list from a kind of loan form.
     * @param kindOfLoanJoined An object where the data from a form is located.
     * @param model            It contains all models.
     * @return A logical view name or a value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/kindsOfLoan/add", method = RequestMethod.GET)
    public String kindsOfLoanAddGet(@ModelAttribute("kindOfLoanErrors") SBList kindOfLoanErrors,
                                    @ModelAttribute("kindOfLoanJoined") SBKindOfLoanJoined kindOfLoanJoined,
                                    Model model)
    {
        if (bankDao.findAll().size() == 0 || capitalisationDao.findAll().size() == 0)
        {
            return "redirect:/edit/kindsOfLoan/selectAll";
        }

        /* ********************************************************************************** */

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser user = userDao.findByUsername(username);

        model.addAttribute("logInUser", username);
        model.addAttribute("isAdmin", user.getIsAdmin());

        /* ********************************************************************************** */

        model.addAttribute("kindOfLoanJoined", kindOfLoanJoined);
        model.addAttribute("banks", bankDao.findAll());
        model.addAttribute("capitalisations", capitalisationDao.findAll());

        model.addAttribute("kindOfLoanErrors", kindOfLoanErrors.getList());

        return menuEditKindsOfLoanAdd;
    }

    /**
     * A method is used to add a new kind of loan record. It validates {@link SBKindOfLoan} object and saves it in database.
     *
     * @param kindOfLoanJoined   An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBKindOfLoanJoined} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/kindsOfLoan/add", method = RequestMethod.POST)
    public String kindsOfLoanAddPost(@ModelAttribute("kindOfLoanJoined") @Valid SBKindOfLoanJoined kindOfLoanJoined,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes)
    {
        kindOfLoanJoinedValidator.validate(kindOfLoanJoined, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("kindOfLoanJoined", kindOfLoanJoined);
            redirectAttributes.addFlashAttribute("kindOfLoanErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));

            return "redirect:/edit/kindsOfLoan/add";
        }

        kindOfLoanJoinedService.addKindOfLoanJoined(kindOfLoanJoined);

        redirectAttributes.addFlashAttribute("kindOfLoanAddInfo", new SBField(DATA_ADD_MSG));

        return "redirect:/edit/kindsOfLoan/selectAll";
    }

    /**
     * A method is used to modify a kind of loan record from database. It shows form to modify.
     *
     * @param modelAndView     It contains all models and a logical view name.
     * @param kindOfLoanErrors A error list from a kind of loan form.
     * @param id               A modified record ID.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/kindsOfLoan/modify/{id}", method = RequestMethod.GET)
    public ModelAndView kindsOfLoanModifyGet(ModelAndView modelAndView,
                                             @ModelAttribute("kindOfLoanErrors") SBList kindOfLoanErrors,
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

        modelAndView.setViewName(menuEditKindsOfLoanModify);

        modelAndView.addObject("kindOfLoan", kindOfLoanDao.getOne(id));
        modelAndView.addObject("banks", bankDao.findAll());
        modelAndView.addObject("capitalisations", capitalisationDao.findAll());

        modelAndView.addObject("kindOfLoanErrors", kindOfLoanErrors.getList());

        return modelAndView;
    }

    /**
     * A method is used to modify a kind of loan record. It validates {@link SBKindOfLoan} object and modifies it.
     *
     * @param kindOfLoan         An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBKindOfLoan} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/kindsOfLoan/modify", method = RequestMethod.POST)
    public String kindsOfLoadModifyPost(@ModelAttribute("kindOfLoan") @Valid SBKindOfLoan kindOfLoan,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes)
    {
        kindOfLoan.setBank(bankDao.getOne(kindOfLoan.getBank().getId()));
        kindOfLoan.setCapitalisation(capitalisationDao.getOne(kindOfLoan.getCapitalisation().getId()));

        kindOfLoanValidator.validate(kindOfLoan, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("kindOfLoanErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));

            return "redirect:/edit/kindsOfLoan/modify/" + kindOfLoan.getId();
        }

        kindOfLoanCustomDao.modify(kindOfLoan);

        redirectAttributes.addFlashAttribute("kindOfLoanModifyInfo", new SBField(DATA_MODIFY_MSG));

        return "redirect:/edit/kindsOfLoan/selectAll";
    }

    /**
     * A method is used to sort a kind of loan list from '/edit/kindsOfLoan/selectAll' website.
     *
     * @param field              A selected option from a sort bar.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/kindsOfLoan/sort", method = RequestMethod.POST)
    public String kindOfLoanSortPost(@ModelAttribute("kindOfLoanField") SBField field,
                                     RedirectAttributes redirectAttributes)
    {
        String chosenField = field.getField().split(" - ")[0];
        String chosenOrder = field.getField().split(" - ")[1];

        redirectAttributes.addFlashAttribute("kindOfLoanField", new SBField(chosenField + " - " + chosenOrder));
        redirectAttributes.addFlashAttribute("chosenEnum", SBKindOfLoanFields.getEnum(chosenField, chosenOrder));

        return "redirect:/edit/kindsOfLoan/selectAll";
    }
}
