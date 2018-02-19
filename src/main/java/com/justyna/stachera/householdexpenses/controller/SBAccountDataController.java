package com.justyna.stachera.householdexpenses.controller;

import com.justyna.stachera.householdexpenses.constants.SBWebsitesAndMessages;
import com.justyna.stachera.householdexpenses.dao.custom.SBUserCustomDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBUserDao;
import com.justyna.stachera.householdexpenses.domain.helpers.SBField;
import com.justyna.stachera.householdexpenses.domain.helpers.SBPassword;
import com.justyna.stachera.householdexpenses.domain.main.SBPerson;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.utils.SBCustomUtils;
import com.justyna.stachera.householdexpenses.validation.SBPasswordValidator;
import com.justyna.stachera.householdexpenses.validation.SBPersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
 * {@link Controller} class with methods that support '/accountData/**' links directly.
 */
@Controller
public class SBAccountDataController implements SBWebsitesAndMessages
{
    private SBUserDao userDao;
    private SBUserCustomDao userCustomDao;
    private SBPersonValidator personValidator;
    private SBPasswordValidator passwordValidator;

    /**
     * An argument constructor autowired interfaces extending
     * {@link org.springframework.data.jpa.repository.JpaRepository}
     * and {@link org.springframework.validation.Validator} objects.
     *
     * @param userDao           It provides methods related with 'sbuser' table from database.
     * @param userCustomDao     It provides extra methods related with 'sbuser' table from database.
     * @param personValidator   It validates {@link SBPerson} object fields.
     * @param passwordValidator It validates {@link SBPassword} object fields.
     */
    @Autowired
    public SBAccountDataController(SBUserDao userDao,
                                   SBUserCustomDao userCustomDao,
                                   SBPersonValidator personValidator,
                                   SBPasswordValidator passwordValidator)
    {
        this.userDao = userDao;
        this.userCustomDao = userCustomDao;
        this.personValidator = personValidator;
        this.passwordValidator = passwordValidator;
    }

    /**
     * A method is used to show the '/accountData' page content like a name, surname, age etc. for logged in user.
     *
     * @param removeAccountError A {@link SBField} object contains a remove account error message.
     * @param modelAndView       It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/accountData", method = RequestMethod.GET)
    public ModelAndView accountDataGet(@ModelAttribute("removeAccountError") SBField removeAccountError,
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

        modelAndView.setViewName(ACCOUNT_DATA);

        modelAndView.addObject("person", userDao.findByUsername(username).getPerson());
        modelAndView.addObject("password", new SBPassword());
        modelAndView.addObject("removeAccountError", removeAccountError);

        return modelAndView;
    }

    /**
     * A method is used to change a user data.
     *
     * @param person             An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBPerson} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/accountData/modify/data", method = RequestMethod.POST)
    public String modifyAccountDataPost(@ModelAttribute("person") @Valid SBPerson person,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        /* ********************************************************************************** */

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("dataErrors", SBCustomUtils.convertStringListToSBFieldList(errors));

            return "redirect:/accountData";
        }

        logInUser.getPerson().setName(person.getName());
        logInUser.getPerson().setSurname(person.getSurname());
        logInUser.getPerson().setAge(person.getAge());
        logInUser.getPerson().setPesel(person.getPesel());

        userCustomDao.modifyData(logInUser);

        redirectAttributes.addFlashAttribute("dataModifyInfo", new SBField(DATA_MODIFY_MSG));

        return "redirect:/accountData";
    }

    /**
     * A method is used to change a user password.
     *
     * @param password           An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBPassword} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/accountData/modify/password")
    public String modifyPasswordPost(@ModelAttribute("password") @Valid SBPassword password,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser user = userDao.findByUsername(username);

        /* ********************************************************************************** */

        passwordValidator.setUser(user);
        passwordValidator.validate(password, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("passwordErrors",
                                                 SBCustomUtils.convertStringListToSBFieldList(errors));

            return "redirect:/accountData";
        }

        user.setPassword(password.getNewPassword());
        user.setConfirmPassword(password.getConfirmPassword());

        userCustomDao.modifyPassword(user);

        redirectAttributes.addFlashAttribute("passwordModifyInfo", new SBField(PASSWORD_MODIFY_MSG));

        return "redirect:/accountData";
    }
}