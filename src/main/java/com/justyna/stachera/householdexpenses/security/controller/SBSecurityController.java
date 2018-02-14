package com.justyna.stachera.householdexpenses.security.controller;

import com.justyna.stachera.householdexpenses.constants.SBWebsitesAndMessages;
import com.justyna.stachera.householdexpenses.dao.jrepository.*;
import com.justyna.stachera.householdexpenses.domain.helpers.SBField;
import com.justyna.stachera.householdexpenses.domain.main.*;
import com.justyna.stachera.householdexpenses.security.enums.SBRoles;
import com.justyna.stachera.householdexpenses.utils.SBCustomUtils;
import com.justyna.stachera.householdexpenses.validation.SBUserPersonValidator;
import com.justyna.stachera.householdexpenses.validation.SBUserValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
 * {@link Controller} class which contains logging in, logging out and registering new user methods.
 */
@Controller
public class SBSecurityController implements SBWebsitesAndMessages
{
    private Logger logger = Logger.getLogger(this.getClass());

    private SBPersonDao personDao;
    private SBExpenseDao expenseDao;
    private SBLoanDao loanDao;
    private SBTaxDao taxDao;
    private SBUserDao userDao;
    private SBUserValidator userValidator;
    private SBUserPersonValidator personValidator;

    /**
     * Argument constructor.
     *
     * @param personDao       It provides methods related with 'sbperson' table from database.
     * @param expenseDao      It provides methods related with 'sbexpense' table from database.
     * @param loanDao         It provides methods related with 'sbloan' table from database.
     * @param taxDao          It provides methods related with 'sbtax' table from database.
     * @param userDao         It provides methods related with 'sbuser' table from database.
     * @param userValidator   It validates {@link SBUser} object fields.
     * @param personValidator It validates {@link SBPerson} object fields.
     */
    @Autowired
    public SBSecurityController(SBPersonDao personDao,
                                SBExpenseDao expenseDao,
                                SBLoanDao loanDao,
                                SBTaxDao taxDao,
                                SBUserDao userDao,
                                SBUserValidator userValidator,
                                SBUserPersonValidator personValidator)
    {
        this.personDao = personDao;
        this.expenseDao = expenseDao;
        this.loanDao = loanDao;
        this.taxDao = taxDao;
        this.userDao = userDao;
        this.userValidator = userValidator;
        this.personValidator = personValidator;
    }

    /**
     * A method is used to register a new user. It shows form to complete.
     *
     * @param model It contains all models.
     * @return A logical view name.
     */
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registrationGet(Model model)
    {
        model.addAttribute("user", new SBUser());

        return registration;
    }

    /**
     * A method is used to register a new user. It validates {@link SBUser} object and saves it in database.
     *
     * @param user          An object where the data from a form is located.
     * @param bindingResult An interface BindingResult implementation which checks {@link SBUser} object.
     * @param model         It contains all models.
     * @return A logical view name.
     */
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registrationSuccessGet(@ModelAttribute("user") @Valid SBUser user,
                                         BindingResult bindingResult,
                                         Model model)
    {
        userValidator.validate(user, bindingResult);
        personValidator.validate(user.getPerson(), bindingResult);

        final String RIGHTS_MSG_TO_REMOVE = "Nie zaznaczono żadnego pola w sekcji 'Prawa'!";
        List<String> bindingResultFields = bindingResult.getFieldErrors()
                                                        .stream()
                                                        .map(FieldError::getField)
                                                        .collect(Collectors.toList());
        bindingResultFields.remove("isAdmin");

        if (!bindingResultFields.isEmpty())
        {
            List<String> bindingResults = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            if (bindingResults.contains(RIGHTS_MSG_TO_REMOVE))
            {
                bindingResults.remove(RIGHTS_MSG_TO_REMOVE);
            }

            model.addAttribute("userErrors", SBCustomUtils.convertStringListToSBFieldList(bindingResults));

            return registration;
        }

        user.setRole("ROLE_USER_ARM");
        user.setIsAdmin(false);
        user.setIsReadOnly(true);
        user.setIsAddOnly(true);
        user.setIsModifyOnly(true);

        SBPerson savePerson = user.getPerson();
        savePerson.setUser(user);
        personDao.save(savePerson);

        return registrationSuccess;
    }

    /**
     * It shows a registration success methods.
     * @return A local view name.
     */
    @RequestMapping(value = "/registrationSuccess", method = RequestMethod.GET)
    public String registrationSuccessGet()
    {
        return registrationSuccess;
    }

    /**
     * It log in user to application.
     *
     * @return A logical view name.
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGet()
    {
        if (userDao.findByRole(SBRoles.ROLE_SUPER_ADMIN.toString()) == null)
        {
            SBUser defaultUser = SBUser.builder()
                                       .username("admin")
                                       .password("admin123")
                                       .confirmPassword("admin123")
                                       .role(SBRoles.ROLE_SUPER_ADMIN.toString())
                                       .isAdmin(true)
                                       .isAddOnly(true)
                                       .isReadOnly(true)
                                       .isModifyOnly(true)
                                       .build();

            SBPerson defaultPerson = SBPerson.builder()
                                             .name("Default")
                                             .surname("Default")
                                             .age(24)
                                             .pesel("00321217394")
                                             .user(defaultUser)
                                             .build();

            personDao.save(defaultPerson);

            logger.info("No SUPER_ADMIN found - create default SUPER_ADMIN: " +
                        "{username: " + defaultUser.getUsername()
                        + ", password: " + defaultUser.getPassword()
                        + "}");
        }

        return loginPage;
    }

    /**
     * It log in user to application. It is required to log in.
     *
     * @return A logical view name.
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginPost()
    {
        return infoAndHelp;
    }

    /**
     * It log out user to application.
     *
     * @return A logical view name.
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutGet()
    {
        return logoutPage;
    }

    /**
     * It log out user to application. It is required to log out.
     * @return A logical view name.
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logoutPost()
    {
        return logoutPage;
    }

    /**
     * It removes user from database if he wants to do it.
     *
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A logical view name and a value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/removeAccount", method = RequestMethod.GET)
    public String removeAccountGet(RedirectAttributes redirectAttributes)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser user = userDao.findByUsername(username);

        if (user.getRole().equals(SBRoles.ROLE_SUPER_ADMIN.toString()))
        {
            redirectAttributes.addFlashAttribute("removeAccountError",
                                                 new SBField("Nie można usunąć tego konta z poziomu aplikacji!"));

            return "redirect:/accountData";
        }

        Long personId = user.getPerson().getId();
        List<SBExpense> expenses = expenseDao.findAllByUser(user);
        List<SBLoan> loans = loanDao.findAllByUser(user);
        List<SBTax> taxes = taxDao.findAllByUser(user);

        user.setPerson(null);

        personDao.delete(personId);
        expenseDao.delete(expenses);
        loanDao.delete(loans);
        taxDao.delete(taxes);
        userDao.delete(user);

        return removePage;
    }
}