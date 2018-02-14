package com.justyna.stachera.householdexpenses.controller;

import com.justyna.stachera.householdexpenses.constants.SBWebsitesAndMessages;
import com.justyna.stachera.householdexpenses.dao.custom.SBUserCustomDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.*;
import com.justyna.stachera.householdexpenses.domain.enums.SBUserFields;
import com.justyna.stachera.householdexpenses.domain.helpers.SBField;
import com.justyna.stachera.householdexpenses.domain.helpers.SBList;
import com.justyna.stachera.householdexpenses.domain.main.*;
import com.justyna.stachera.householdexpenses.security.enums.SBRoles;
import com.justyna.stachera.householdexpenses.utils.SBCustomUtils;
import com.justyna.stachera.householdexpenses.validation.SBUserPersonValidator;
import com.justyna.stachera.householdexpenses.validation.SBUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
 * {@link Controller} class with methods that support '/edit/users/**' links directly.
 */
@Controller
public class SBEditUsersController implements SBWebsitesAndMessages
{
    private SBPersonDao personDao;
    private SBUserDao userDao;
    private SBUserCustomDao userCustomDao;
    private SBExpenseDao expenseDao;
    private SBLoanDao loanDao;
    private SBTaxDao taxDao;
    private SBUserValidator userValidator;
    private SBUserPersonValidator personValidator;

    /**
     * An argument constructor autowired interfaces extending {@link org.springframework.data.jpa.repository.JpaRepository} and {@link org.springframework.validation.Validator} objects.
     *
     * @param personDao       It provides methods related with 'sbperson' table from database.
     * @param userDao         It provides methods related with 'sbuser' table from database.
     * @param userCustomDao   It provides extra methods related with 'sbuser' table from database.
     * @param expenseDao      It provides methods related with 'sbexpense' table from database.
     * @param loanDao         It provides methods related with 'sbloan' table from database.
     * @param taxDao          It provides methods related with 'sbtax' table from database.
     * @param userValidator   It validates {@link SBUser} object fields.
     * @param personValidator It validates {@link SBPerson} object fields.
     */
    @Autowired
    public SBEditUsersController(SBPersonDao personDao,
                                 SBUserDao userDao,
                                 SBUserCustomDao userCustomDao,
                                 SBExpenseDao expenseDao,
                                 SBLoanDao loanDao,
                                 SBTaxDao taxDao,
                                 SBUserValidator userValidator,
                                 SBUserPersonValidator personValidator)
    {
        this.personDao = personDao;
        this.userDao = userDao;
        this.userCustomDao = userCustomDao;
        this.expenseDao = expenseDao;
        this.loanDao = loanDao;
        this.taxDao = taxDao;
        this.userValidator = userValidator;
        this.personValidator = personValidator;
    }

    /**
     * A method is used to show a user list and sort it.
     *
     * @param field              An chosen field from a sort bar.
     * @param chosenEnum         It informs in which column the table has to be sorted.
     * @param authorizationError An error related to logged in user access rights.
     * @param addInfo            A message about adding a new record to database.
     * @param modifyInfo         A message about modification a record in database.
     * @param removeInfo         A message about deleting record from database.
     * @param modelAndView       It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/users/selectAll", method = RequestMethod.GET)
    public ModelAndView usersSelectAllGet(@ModelAttribute("userField") SBField field,
                                          @ModelAttribute("chosenEnum") String chosenEnum,
                                          @ModelAttribute("userAuthorizationError") SBField authorizationError,
                                          @ModelAttribute("userAddInfo") SBField addInfo,
                                          @ModelAttribute("userModifyInfo") SBField modifyInfo,
                                          @ModelAttribute("userRemoveInfo") SBField removeInfo,
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

        modelAndView.setViewName(menuEditUsers);

        modelAndView.addObject("users", userCustomDao.sortTableBy(chosenEnum));
        modelAndView.addObject("userFields", SBUserFields.getList());

        modelAndView.addObject("userAddInfo", addInfo);
        modelAndView.addObject("userRemoveInfo", removeInfo);
        modelAndView.addObject("userModifyInfo", modifyInfo);

        modelAndView.addObject("userAuthorizationError", authorizationError);

        return modelAndView;
    }

    /**
     * A method is used to remove a user record from database.
     *
     * @param id                 A removed record ID.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/users/remove/{id}", method = RequestMethod.GET)
    public String usersRemoveGet(@PathVariable("id") Long id,
                                 RedirectAttributes redirectAttributes)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        /* ********************************************************************************** */

        SBUser userToRemove = userDao.getOne(id);

        if (userToRemove.getRole().equals(SBRoles.ROLE_SUPER_ADMIN.toString()))
        {
            redirectAttributes.addFlashAttribute("userAuthorizationError", new SBField(SUPER_ADMIN_REMOVE_MSG));

            return "redirect:/edit/users/selectAll";
        }

        if (userToRemove.getId().equals(logInUser.getId()))
        {
            redirectAttributes.addFlashAttribute("userAuthorizationError", new SBField(ADMIN_REMOVE_ACCOUNT_MSG));

            return "redirect:/edit/users/selectAll";
        }

        if (userToRemove.getRole().equals(SBRoles.ROLE_ADMIN.toString()) &&
            !logInUser.getRole().equals(SBRoles.ROLE_SUPER_ADMIN.toString()))
        {
            redirectAttributes.addFlashAttribute("userAuthorizationError", new SBField(ADMIN_REMOVE_ACCESS_MSG));

            return "redirect:/edit/users/selectAll";
        }

        /* ********************************************************************************** */

        Long personId = userToRemove.getPerson().getId();
        List<SBExpense> expenses = expenseDao.findAllByUser(userToRemove);
        List<SBLoan> loans = loanDao.findAllByUser(userToRemove);
        List<SBTax> taxes = taxDao.findAllByUser(userToRemove);

        /**
         * Set SBPerson to NULL, otherwise {@link SBUser} don't delete itself because of relation.
         */
        userToRemove.setPerson(null);

        personDao.delete(personId);
        expenseDao.delete(expenses);
        loanDao.delete(loans);
        taxDao.delete(taxes);
        userDao.delete(userToRemove);

        redirectAttributes.addFlashAttribute("userRemoveInfo", new SBField(DATA_REMOVE_MSG));

        return "redirect:/edit/users/selectAll";
    }

    /**
     * A method is used to add a new user record to database. It shows form to complete.
     *
     * @param userErrors   A error list from a user form.
     * @param user         An object where the data from a form is located.
     * @param modelAndView It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/users/add", method = RequestMethod.GET)
    public ModelAndView usersAddGet(@ModelAttribute("userErrors") SBList userErrors,
                                    @ModelAttribute("user") SBUser user,
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

        modelAndView.setViewName(menuEditUsersAdd);

        modelAndView.addObject("user", user);

        modelAndView.addObject("userErrors", userErrors.getList());

        return modelAndView;
    }

    /**
     * A method is used to add a new user record. It validates {@link SBUser} object and saves it in database.
     *
     * @param user               An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBUser} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/users/add", method = RequestMethod.POST)
    public String usersAddPost(@ModelAttribute("user") @Valid SBUser user,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes)
    {
        userValidator.validate(user, bindingResult);
        personValidator.validate(user.getPerson(), bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("userErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));

            return "redirect:/edit/users/add";
        }

        /* ********************************************************************************** */

        userCustomDao.assignRole(user);

        SBPerson savePerson = user.getPerson();
        savePerson.setUser(user);
        personDao.save(savePerson);

        redirectAttributes.addFlashAttribute(new SBField(DATA_ADD_MSG));

        return "redirect:/edit/users/selectAll";
    }

    /**
     * A method is used to modify a user record from database. It shows form to modify.
     *
     * @param model              It contains all models.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @param userErrors         A error list from a user form.
     * @param authorizationError An error related to logged in user access rights.
     * @param id                 A modified record ID.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/users/modify/{id}", method = RequestMethod.GET)
    public String usersModifyGet(Model model,
                                 RedirectAttributes redirectAttributes,
                                 @ModelAttribute("userErrors") SBList userErrors,
                                 @ModelAttribute("userAuthorizationError") SBField authorizationError,
                                 @PathVariable("id") Long id)
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

        SBUser userToModify = userDao.getOne(id);

        if (userToModify.getRole().equals(SBRoles.ROLE_SUPER_ADMIN.toString()) &&
            logInUser.getRole().equals(SBRoles.ROLE_ADMIN.toString()))
        {
            redirectAttributes.addFlashAttribute("userAuthorizationError", new SBField(ADMIN_MODIFY_ACCESS_MSG));

            return "redirect:/edit/users/selectAll";
        }

        if (userToModify.getRole().equals(SBRoles.ROLE_ADMIN.toString()) &&
            !logInUser.getId().equals(userToModify.getId()) &&
            !logInUser.getRole().equals(SBRoles.ROLE_SUPER_ADMIN.toString()))
        {
            redirectAttributes.addFlashAttribute("userAuthorizationError", new SBField(ADMIN_MODIFY_ACCESS_MSG));

            return "redirect:/edit/users/selectAll";
        }

        /* ********************************************************************************** */

        {
            model.addAttribute("user", userDao.getOne(id));
        }

        model.addAttribute("userAuthorizationError", authorizationError);
        model.addAttribute("userErrors", userErrors.getList());

        return menuEditUsersModify;
    }

    /**
     * A method is used to modify a user record. It validates {@link SBUser} object and modifies it.
     *
     * @param user               An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBUser} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/users/modify", method = RequestMethod.POST)
    public String usersModifyPost(@ModelAttribute("user") @Valid SBUser user,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        /* ********************************************************************************** */

        SBUser userToModify = userDao.getOne(user.getId());

        if (userToModify.getId().equals(logInUser.getId()) && !user.getIsAdmin() && logInUser.getIsAdmin())
        {
            redirectAttributes.addFlashAttribute("userAuthorizationError", new SBField(ADMIN_RIGHTS_ACCESS_MSG));

            return "redirect:/edit/users/modify/" + user.getId();
        }

        if (!userToModify.getId().equals(logInUser.getId()) && userToModify.getIsAdmin() &&
            !logInUser.getRole().equals(SBRoles.ROLE_SUPER_ADMIN.toString()))
        {
            redirectAttributes.addFlashAttribute("userAuthorizationError", new SBField(ADMIN_MODIFY_ACCESS_MSG));

            return "redirect:/edit/users/modify/" + user.getId();
        }

        user.setPassword(userToModify.getPassword());
        user.setConfirmPassword(userToModify.getPassword());

        userValidator.validate(user, bindingResult);
        personValidator.setUser(userDao.getOne(user.getId()));
        personValidator.validate(user.getPerson(), bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> bindingResults = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("userErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(bindingResults)));

            return "redirect:/edit/users/modify/" + user.getId();
        }

        /* ********************************************************************************** */

        if (logInUser.getRole().equals(SBRoles.ROLE_SUPER_ADMIN.toString())
            && user.getId().equals(logInUser.getId()))
        {
            user.setRole(SBRoles.ROLE_SUPER_ADMIN.toString());
        }
        else
        {
            userCustomDao.assignRole(user);
        }

        userCustomDao.modifyData(user);

        authentication = SecurityContextHolder.getContext().getAuthentication();

        if (userDao.findByUsername(authentication.getName()) == null)
        {
            Authentication newAuthentication = new UsernamePasswordAuthenticationToken(user.getUsername(),
                                                                                       authentication.getCredentials(),
                                                                                       authentication.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuthentication);
        }

        redirectAttributes.addFlashAttribute("userModifyInfo", new SBField(DATA_MODIFY_MSG));

        return "redirect:/edit/users/selectAll";
    }

    /**
     * A method is used to modify a password. It shows form to modify.
     *
     * @param model              It contains all models.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @param userErrors         A error list from a user form.
     * @param id                 A modified record ID.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/edit/users/password/{id}", method = RequestMethod.GET)
    public String usersPasswordGet(Model model,
                                   RedirectAttributes redirectAttributes,
                                   @ModelAttribute("userErrors") SBList userErrors,
                                   @PathVariable("id") Long id)
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

        SBUser userToModify = userDao.getOne(id);

        if (logInUser.getRole().equals(SBRoles.ROLE_ADMIN.toString()) && userToModify.getIsAdmin() &&
            !logInUser.getId().equals(userToModify.getId()))
        {
            redirectAttributes.addFlashAttribute("userAuthorizationError", new SBField(PASSWORD_MODIFY_ACCESS_MSG));

            return "redirect:/edit/users/selectAll";
        }

        /* ********************************************************************************** */

        userDao.getOne(id).setPassword("");
        userDao.getOne(id).setConfirmPassword("");

        model.addAttribute("user", userDao.getOne(id));

        model.addAttribute("userErrors", userErrors.getList());

        return menuEditUsersPassword;
    }

    /**
     * A method is used to modify a password. It validates {@link SBUser} object and modifies it.
     *
     * @param user               An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBUser} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/users/password", method = RequestMethod.POST)
    public String usersPasswordPost(@ModelAttribute("user") @Valid SBUser user,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        /* ********************************************************************************** */

        SBUser userToModify = userDao.getOne(user.getId());

        if (userToModify.getRole().equals(SBRoles.ROLE_SUPER_ADMIN.toString()) &&
            logInUser.getRole().equals(SBRoles.ROLE_ADMIN.toString()))
        {
            redirectAttributes.addFlashAttribute("userAuthorizationError", new SBField(PASSWORD_MODIFY_ACCESS_MSG));

            return "redirect:/edit/users/selectAll";
        }

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> bindingResults = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("userErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(bindingResults)));

            return "redirect:/edit/users/password/" + user.getId();
        }

        /* ********************************************************************************** */

        userCustomDao.modifyPassword(user);

        redirectAttributes.addFlashAttribute("userModifyInfo", new SBField(PASSWORD_RESET_MSG));

        return "redirect:/edit/users/selectAll";
    }

    /**
     * A method is used to sort a user list from '/edit/user/selectAll' website.
     *
     * @param field              A selected option from a sort bar.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/edit/users/sort", method = RequestMethod.POST)
    public String usersSortPost(@ModelAttribute("userField") SBField field,
                                RedirectAttributes redirectAttributes)
    {
        String chosenField = field.getField().split(" - ")[0];
        String chosenOrder = field.getField().split(" - ")[1];

        redirectAttributes.addFlashAttribute("userField", new SBField(chosenField + " - " + chosenOrder));
        redirectAttributes.addFlashAttribute("chosenEnum", SBUserFields.getEnum(chosenField, chosenOrder));

        return "redirect:/edit/users/selectAll";
    }
}