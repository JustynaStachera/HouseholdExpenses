package com.justyna.stachera.householdexpenses.controller;

import com.justyna.stachera.householdexpenses.constants.SBWebsitesAndMessages;
import com.justyna.stachera.householdexpenses.dao.custom.SBTaxCustomDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBTaxDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBUserDao;
import com.justyna.stachera.householdexpenses.domain.enums.SBTaxFields;
import com.justyna.stachera.householdexpenses.domain.helpers.*;
import com.justyna.stachera.householdexpenses.domain.main.SBTax;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.utils.SBCustomUtils;
import com.justyna.stachera.householdexpenses.utils.SBEconomicUtils;
import com.justyna.stachera.householdexpenses.validation.SBTaxValidator;
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
 * {@link Controller} class with methods that support '/taxes/**' links directly.
 */
@Controller
public class SBTaxesController implements SBWebsitesAndMessages
{
    private SBUserDao userDao;
    private SBTaxDao taxDao;
    private SBTaxCustomDao taxCustomDao;
    private SBTaxValidator taxValidator;

    /**
     * An argument constructor autowired interfaces extending
     * {@link org.springframework.data.jpa.repository.JpaRepository} and
     * {@link org.springframework.validation.Validator} objects.
     *
     * @param userDao      It provides methods related with 'sbuser' table from database.
     * @param taxDao       It provides methods related with 'sbtax' table from database.
     * @param taxCustomDao It provides extra methods related with 'sbtax' table from database.
     * @param taxValidator It validates {@link SBTax} object fields.
     */
    @Autowired
    public SBTaxesController(SBUserDao userDao,
                             SBTaxDao taxDao,
                             SBTaxCustomDao taxCustomDao,
                             SBTaxValidator taxValidator)
    {
        this.userDao = userDao;
        this.taxDao = taxDao;
        this.taxCustomDao = taxCustomDao;
        this.taxValidator = taxValidator;
    }

    /**
     * A method is used to show a tax list, sort it and find record by tax ID.
     *
     * @param taxField           An chosen field from a sort bar.
     * @param taxId              An ID value chosen field.
     * @param chosenEnum         It informs in which column the table has to be sorted.
     * @param addInfo            A message about adding a new record to database.
     * @param modifyInfo         A message about modification a record in database.
     * @param removeInfo         A message about deleting record from database.
     * @param isTaxUserIdInvalid It checks if user ID is invalid.
     * @param modelAndView       It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/taxes/selectAll", method = RequestMethod.GET)
    public ModelAndView taxesSelectAllGet(@ModelAttribute("taxField") SBField taxField,
                                          @ModelAttribute("taxId") SBId taxId,
                                          @ModelAttribute("chosenEnum") String chosenEnum,
                                          @ModelAttribute("taxAddInfo") SBField addInfo,
                                          @ModelAttribute("taxModifyInfo") SBField modifyInfo,
                                          @ModelAttribute("taxRemoveInfo") SBField removeInfo,
                                          @ModelAttribute("isTaxUserIdInvalid") SBBoolean isTaxUserIdInvalid,
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

        modelAndView.setViewName(TAXES);

        modelAndView.addObject("taxes", taxDao.findAll());
        modelAndView.addObject("taxesSize", taxDao.findAll().size());

        List<SBTax> sortedTaxes;

        if (taxId.getId() != null)
        {
            sortedTaxes = new ArrayList<SBTax>()
            {{
                add(taxDao.getOne(taxId.getId()));
            }};
            modelAndView.addObject("taxes", sortedTaxes);
        }
        else
        {
            sortedTaxes = taxCustomDao.sortTableBy(chosenEnum, logInUser);
            modelAndView.addObject("taxes", sortedTaxes);
        }

        modelAndView.addObject("taxFields", SBTaxFields.getList());
        modelAndView.addObject("taxesSum", sortedTaxes.stream().mapToDouble(SBTax::getAmountPayable).sum());
        modelAndView.addObject("taxesSortedSize", sortedTaxes.size());
        modelAndView.addObject("taxesUserSize", taxDao.findAllByUser(logInUser).size());
        modelAndView.addObject("taxId", taxId);

        List<SBMessage> taxMessages = SBCustomUtils.taxMessages(logInUser, userDao.findAll(), taxDao.findAll());

        modelAndView.addObject("isTaxMessagesEmpty", taxMessages.isEmpty());
        modelAndView.addObject("taxMessages", taxMessages);

        modelAndView.addObject("taxAddInfo", addInfo);
        modelAndView.addObject("taxRemoveInfo", removeInfo);
        modelAndView.addObject("taxModifyInfo", modifyInfo);

        modelAndView.addObject("isTaxUserIdInvalid", isTaxUserIdInvalid);

        return modelAndView;
    }

    /**
     * A method is used to remove a tax record from database.
     *
     * @param id                 A removed record ID.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/taxes/remove/{id}", method = RequestMethod.GET)
    public String taxesRemoveGet(@PathVariable("id") Long id,
                                 RedirectAttributes redirectAttributes)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        /* ********************************************************************************** */

        if (!logInUser.getId().equals(taxDao.getOne(id).getUser().getId()) && !logInUser.getIsAdmin())
        {
            redirectAttributes.addFlashAttribute("isTaxUserIdInvalid", new SBBoolean(true));

            return "redirect:/taxes/selectAll";
        }

        /* ********************************************************************************** */

        taxDao.delete(id);

        redirectAttributes.addFlashAttribute("taxRemoveInfo", new SBField(DATA_REMOVE_MSG));

        return "redirect:/taxes/selectAll";
    }

    /**
     * A method is used to add a new tax record to database. It shows form to complete.
     *
     * @param taxErrors A error list from a tax form.
     * @param tax       An object where the data from a form is located.
     * @param model     It contains all models.
     * @return A logical view name or a value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/taxes/add", method = RequestMethod.GET)
    public String taxesAddGet(@ModelAttribute("taxErrors") SBList taxErrors,
                              @ModelAttribute("tax") SBTax tax,
                              Model model)
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

        model.addAttribute("tax", tax);

        model.addAttribute("taxErrors", taxErrors.getList());

        return TAXES_ADD;
    }

    /**
     * A method is used to add a new tax record. It validates {@link SBTax} object and saves it in database.
     *
     * @param tax                An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBTax} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/taxes/add", method = RequestMethod.POST)
    public String taxesAddPost(@ModelAttribute("tax") @Valid SBTax tax,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        /* ********************************************************************************** */

        taxValidator.validate(tax, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult.getAllErrors()
                                               .stream()
                                               .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                               .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("taxErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));
            redirectAttributes.addFlashAttribute("tax", tax);

            return "redirect:/taxes/add";
        }

        /* ********************************************************************************** */

        Double perCent = SBEconomicUtils.getTaxPerCent(tax.getAmount());
        Double amountPayable = SBEconomicUtils.getAmountPayable(perCent, tax.getAmount());

        tax.setUser(logInUser);
        tax.setPerCent(perCent);
        tax.setAmountPayable(amountPayable);

        taxDao.save(tax);

        redirectAttributes.addFlashAttribute("taxesAddInfo", new SBField(DATA_ADD_MSG));

        return "redirect:/taxes/selectAll";
    }

    /**
     * A method is used to modify a tax record from database. It shows form to modify.
     *
     * @param model              It contains all models.
     * @param taxErrors          A error list from a tax form.
     * @param id                 A modified record ID.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A logical view name or a value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/taxes/modify/{id}", method = RequestMethod.GET)
    public String taxesModifyGet(Model model,
                                 @ModelAttribute("taxErrors") SBList taxErrors,
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

        if (!logInUser.getId().equals(taxDao.getOne(id).getUser().getId()) && !logInUser.getIsAdmin())
        {
            redirectAttributes.addFlashAttribute("isTaxUserIdInvalid", new SBBoolean(true));

            return "redirect:/taxes/selectAll";
        }

        /* ********************************************************************************** */

        model.addAttribute("tax", taxDao.getOne(id));

        model.addAttribute("taxErrors", taxErrors.getList());

        return TAXES_MODIFY;
    }

    /**
     * A method is used to modify a tax record. It validates {@link SBTax} object and modifies it.
     *
     * @param tax                An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBTax} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/taxes/modify", method = RequestMethod.POST)
    public String taxesModifyPost(@ModelAttribute("tax") @Valid SBTax tax,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        /* ********************************************************************************** */

        if (!logInUser.getId().equals(taxDao.getOne(tax.getId()).getUser().getId()) && !logInUser.getIsAdmin())
        {
            redirectAttributes.addFlashAttribute("isTaxUserIdInvalid", new SBBoolean(true));

            return "redirect:/taxes/selectAll";
        }

        /* ********************************************************************************** */

        taxValidator.validate(tax, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult.getAllErrors()
                                               .stream()
                                               .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                               .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("taxErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));

            return "redirect:/TAXES/modify/" + tax.getId();
        }

        /* ********************************************************************************** */

        SBUser validUser = logInUser.getIsAdmin() ? taxDao.getOne(tax.getId()).getUser() : logInUser;

        taxCustomDao.modify(tax, validUser);

        redirectAttributes.addFlashAttribute("taxAddInfo", new SBField(DATA_MODIFY_MSG));

        return "redirect:/taxes/selectAll";
    }

    /**
     * A method is used to sort a tax list from '/taxes/selectAll' website.
     *
     * @param taxField           A selected option from a sort bar.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/taxes/sort", method = RequestMethod.POST)
    public String taxesSortPost(@ModelAttribute("taxField") SBField taxField,
                                RedirectAttributes redirectAttributes)
    {
        String chosenField = taxField.getField().split(" - ")[0];
        String chosenOrder = taxField.getField().split(" - ")[1];

        redirectAttributes.addFlashAttribute("taxField", new SBField(chosenField + " - " + chosenOrder));
        redirectAttributes.addFlashAttribute("chosenEnum", SBTaxFields.getEnum(chosenField, chosenOrder));

        return "redirect:/taxes/selectAll";
    }

    /**
     * A method is used to find a record by tax ID.
     *
     * @param taxId              An inserted ID value.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/taxes/findById", method = RequestMethod.POST)
    public String taxesFindByIdPost(@ModelAttribute("taxId") SBId taxId,
                                    RedirectAttributes redirectAttributes)
    {
        redirectAttributes.addFlashAttribute("taxId", taxId);

        return "redirect:/taxes/selectAll";
    }
}
