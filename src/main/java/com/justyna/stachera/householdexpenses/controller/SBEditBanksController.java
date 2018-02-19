package com.justyna.stachera.householdexpenses.controller;

import com.justyna.stachera.householdexpenses.constants.SBWebsitesAndMessages;
import com.justyna.stachera.householdexpenses.dao.custom.SBBankCustomDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBBankDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBKindOfLoanDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBLoanDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBUserDao;
import com.justyna.stachera.householdexpenses.domain.enums.SBBankFields;
import com.justyna.stachera.householdexpenses.domain.helpers.SBField;
import com.justyna.stachera.householdexpenses.domain.helpers.SBList;
import com.justyna.stachera.householdexpenses.domain.main.SBBank;
import com.justyna.stachera.householdexpenses.domain.main.SBKindOfLoan;
import com.justyna.stachera.householdexpenses.domain.main.SBLoan;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.utils.SBCustomUtils;
import com.justyna.stachera.householdexpenses.validation.SBBankValidator;
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
 * {@link Controller} class with methods that support '/edit/banks/**' links directly.
 */
@Controller
public class SBEditBanksController implements SBWebsitesAndMessages
{
    private SBBankDao bankDao;
    private SBBankCustomDao bankCustomDao;
    private SBKindOfLoanDao kindOfLoanDao;
    private SBLoanDao loanDao;
    private SBUserDao userDao;
    private SBBankValidator bankValidator;

    /**
     * An argument constructor autowired interfaces extending
     * {@link org.springframework.data.jpa.repository.JpaRepository} and
     * {@link org.springframework.validation.Validator} objects.
     *
     * @param bankDao       It provides methods related with 'sbbank' table from database.
     * @param bankCustomDao It provides extra methods related with 'sbbank' table from database.
     * @param kindOfLoanDao It provides methods related with 'sbkind_of_loan' table from database.
     * @param loanDao       It provides methods related with {@link SBLoan} table from database.
     * @param userDao       It provides methods related with {@link SBUser} table from database.
     * @param bankValidator It validates {@link SBBank} object fields.
     */
    @Autowired
    public SBEditBanksController(SBBankDao bankDao,
                                 SBBankCustomDao bankCustomDao,
                                 SBKindOfLoanDao kindOfLoanDao,
                                 SBLoanDao loanDao,
                                 SBUserDao userDao,
                                 SBBankValidator bankValidator)
    {
        this.bankDao = bankDao;
        this.bankCustomDao = bankCustomDao;
        this.kindOfLoanDao = kindOfLoanDao;
        this.loanDao = loanDao;
        this.userDao = userDao;
        this.bankValidator = bankValidator;
    }

    /**
     * A method is used to show a bank list and sort it.
     *
     * @param field        An chosen field from a sort bar.
     * @param chosenEnum   It informs in which column the table has to be sorted.
     * @param addInfo      A message about adding a new record to database.
     * @param modifyInfo   A message about modification a record in database.
     * @param removeInfo   A message about deleting record from database.
     * @param modelAndView It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/banks/selectAll", method = RequestMethod.GET)
    public ModelAndView bankSelectAllGet(@ModelAttribute("bankField") SBField field,
                                         @ModelAttribute("chosenEnum") String chosenEnum,
                                         @ModelAttribute("bankAddInfo") SBField addInfo,
                                         @ModelAttribute("bankModifyInfo") SBField modifyInfo,
                                         @ModelAttribute("bankRemoveInfo") SBField removeInfo,
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

        modelAndView.setViewName(MENU_EDIT_BANKS);

        modelAndView.addObject("banks", bankCustomDao.sortTableBy(chosenEnum));
        modelAndView.addObject("banksSize", bankDao.findAll().size());
        modelAndView.addObject("bankFields", SBBankFields.getList());

        modelAndView.addObject("bankAddInfo", addInfo);
        modelAndView.addObject("bankRemoveInfo", removeInfo);
        modelAndView.addObject("bankModifyInfo", modifyInfo);

        return modelAndView;
    }

    /**
     * A method is used to remove a bank record from database.
     *
     * @param id                 A removed record ID.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/banks/remove/{id}", method = RequestMethod.GET)
    public String bankRemoveGet(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes)
    {
        SBBank bank = bankDao.getOne(id);
        List<SBKindOfLoan> kindsOfLoan = kindOfLoanDao.findAllByBank(bank);
        List<SBLoan> loans = loanDao.findAllByKindOfLoan(kindsOfLoan);

        loanDao.delete(loans);
        kindOfLoanDao.delete(kindsOfLoan);
        bankDao.delete(id);

        redirectAttributes.addFlashAttribute("bankRemoveInfo",
                                             new SBField(DATA_REMOVE_MSG));

        return "redirect:/edit/banks/selectAll";
    }

    /**
     * A method is used to add a new bank record to database. It shows form to complete.
     *
     * @param bankErrors   A error list from a bank form.
     * @param bank         An object where the data from a form is located.
     * @param modelAndView It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/banks/add", method = RequestMethod.GET)
    public ModelAndView bankAddGet(@ModelAttribute("bankErrors") SBList bankErrors,
                                   @ModelAttribute("bank") SBBank bank,
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

        modelAndView.setViewName(MENU_EDIT_BANKS_ADD);

        modelAndView.addObject("bank", bank);

        modelAndView.addObject("bankErrors", bankErrors.getList());

        return modelAndView;
    }

    /**
     * A method is used to add a new bank record. It validates {@link SBBank} object and saves it in database.
     *
     * @param bank               An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBBank} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/banks/add", method = RequestMethod.POST)
    public String bankAddPost(@ModelAttribute("bank") @Valid SBBank bank,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes)
    {
        bankValidator.validate(bank, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("bank", bank);
            redirectAttributes.addFlashAttribute("bankErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));

            return "redirect:/edit/banks/add";
        }

        bankDao.save(bank);

        redirectAttributes.addFlashAttribute("bankAddInfo", new SBField(DATA_ADD_MSG));

        return "redirect:/edit/banks/selectAll";
    }

    /**
     * A method is used to modify a bank record from database. It shows form to modify.
     *
     * @param modelAndView It contains all models and a logical view name.
     * @param bankErrors   A error list from a bank form.
     * @param id           A modified record ID.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/banks/modify/{id}", method = RequestMethod.GET)
    public ModelAndView bankModifyGet(ModelAndView modelAndView,
                                      @ModelAttribute("bankErrors") SBList bankErrors,
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

        modelAndView.setViewName(MENU_EDIT_BANKS_MODIFY);

        modelAndView.addObject("bank", bankDao.getOne(id));

        modelAndView.addObject("bankErrors", bankErrors.getList());

        return modelAndView;
    }

    /**
     * A method is used to modify a bank record. It validates {@link SBBank} object and modifies it.
     *
     * @param bank               An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBBank} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/banks/modify", method = RequestMethod.POST)
    public String bankModifyPost(@ModelAttribute("bank") @Valid SBBank bank,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes)
    {
        bankValidator.validate(bank, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> bindingResults = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("bankErrors",
                                                 new SBList(
                                                         SBCustomUtils.convertStringListToSBFieldList(bindingResults)));

            return "redirect:/edit/banks/modify/" + bank.getId();
        }

        bankCustomDao.modify(bank);

        redirectAttributes.addFlashAttribute("bankModifyInfo", new SBField(DATA_MODIFY_MSG));

        return "redirect:/edit/banks/selectAll";
    }

    /**
     * A method is used to sort a bank list from '/edit/bank/selectAll' website.
     *
     * @param field              A selected option from a sort bar.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/banks/sort", method = RequestMethod.POST)
    public String bankSortPost(@ModelAttribute("bankField") SBField field,
                               RedirectAttributes redirectAttributes)
    {
        String chosenField = field.getField().split(" - ")[0];
        String chosenOrder = field.getField().split(" - ")[1];

        redirectAttributes.addFlashAttribute("bankField", new SBField(chosenField + " - " + chosenOrder));
        redirectAttributes.addFlashAttribute("chosenEnum", SBBankFields.getEnum(chosenField, chosenOrder));

        return "redirect:/edit/banks/selectAll";
    }
}