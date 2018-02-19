package com.justyna.stachera.householdexpenses.controller;

import com.justyna.stachera.householdexpenses.constants.SBWebsitesAndMessages;
import com.justyna.stachera.householdexpenses.dao.custom.SBLoanCustomDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBKindOfLoanDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBLoanDao;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBUserDao;
import com.justyna.stachera.householdexpenses.domain.enums.SBLoanFields;
import com.justyna.stachera.householdexpenses.domain.helpers.*;
import com.justyna.stachera.householdexpenses.domain.joined.SBLoanJoined;
import com.justyna.stachera.householdexpenses.domain.main.SBLoan;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.service.SBLoanJoinedService;
import com.justyna.stachera.householdexpenses.utils.SBCustomUtils;
import com.justyna.stachera.householdexpenses.validation.SBLoanJoinedValidator;
import com.justyna.stachera.householdexpenses.validation.SBLoanValidator;
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
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * {@link Controller} class with methods that support '/loan/**' links directly.
 */
@Controller
public class SBLoansController implements SBWebsitesAndMessages
{
    private SBUserDao userDao;
    private SBLoanDao loanDao;
    private SBLoanCustomDao loanCustomDao;
    private SBLoanJoinedService loanJoinedService;
    private SBKindOfLoanDao kindOfLoanDao;
    private SBLoanValidator loanValidator;
    private SBLoanJoinedValidator loanJoinedValidator;

    /**
     * An argument constructor autowired interfaces extending
     * {@link org.springframework.data.jpa.repository.JpaRepository} and
     * {@link org.springframework.validation.Validator} objects.
     *
     * @param userDao             It provides methods related with 'sbuser' table from database.
     * @param loanDao             It provides methods related with 'sbloan' table from database.
     * @param loanCustomDao       It provides extra methods related with 'sbloan' table from database.
     * @param loanJoinedService   It provides methods related with 'sbkind_of_loan' and 'sbuser' tables from database.
     * @param kindOfLoanDao       It provides methods related with 'sbkind_of_loan' table from database.
     * @param loanValidator       It validates {@link SBLoan} object fields.
     * @param loanJoinedValidator It validates {@link SBLoanJoined} object fields.
     */
    @Autowired
    public SBLoansController(SBUserDao userDao, SBLoanDao loanDao,
                             SBLoanCustomDao loanCustomDao,
                             SBLoanJoinedService loanJoinedService,
                             SBKindOfLoanDao kindOfLoanDao,
                             SBLoanValidator loanValidator,
                             SBLoanJoinedValidator loanJoinedValidator)
    {
        this.userDao = userDao;
        this.loanDao = loanDao;
        this.loanCustomDao = loanCustomDao;
        this.loanJoinedService = loanJoinedService;
        this.kindOfLoanDao = kindOfLoanDao;
        this.loanValidator = loanValidator;
        this.loanJoinedValidator = loanJoinedValidator;
    }

    /**
     * A method is used to show a loan list, sort it and find record by loan ID.
     *
     * @param loanField           An chosen field from a sort bar.
     * @param loanId              An ID value chosen field.
     * @param chosenEnum          It informs in which column the table has to be sorted.
     * @param addInfo             A message about adding a new record to database.
     * @param modifyInfo          A message about modification a record in database.
     * @param removeInfo          A message about deleting record from database.
     * @param isLoanUserIdInvalid It checks if user ID is invalid.
     * @param modelAndView        It contains all models and a logical view name.
     * @return A {@link ModelAndView} interface implementation.
     */
    @RequestMapping(value = "/loans/selectAll", method = RequestMethod.GET)
    public ModelAndView loansSelectAllGet(@ModelAttribute("loanField") SBField loanField,
                                          @ModelAttribute("loanId") SBId loanId,
                                          @ModelAttribute("chosenEnum") String chosenEnum,
                                          @ModelAttribute("loanAddInfo") SBField addInfo,
                                          @ModelAttribute("loanModifyInfo") SBField modifyInfo,
                                          @ModelAttribute("loanRemoveInfo") SBField removeInfo,
                                          @ModelAttribute("isLoanUserIdInvalid") SBBoolean isLoanUserIdInvalid,
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

        modelAndView.setViewName(LOANS);

        List<SBLoan> sortedLoans;

        if (loanId.getId() != null && logInUser.getIsReadOnly())
        {
            sortedLoans = new ArrayList<SBLoan>()
            {{
                add(loanDao.getOne(loanId.getId()));
            }};
            modelAndView.addObject("loans", sortedLoans);
        }
        else if (loanId.getId() != null && !logInUser.getIsReadOnly())
        {
            sortedLoans = new ArrayList<SBLoan>()
            {{
                add(loanDao.getOne(loanId.getId()));
            }};
            modelAndView.addObject("loans", sortedLoans);
        }
        else
        {
            sortedLoans = loanCustomDao.sortTableBy(chosenEnum, logInUser);
            modelAndView.addObject("loans", sortedLoans);
        }

        modelAndView.addObject("loansFields", SBLoanFields.getList());
        modelAndView.addObject("loanInterestSum",
                               SBCustomUtils.round(sortedLoans.stream().mapToDouble(SBLoan::getInterest).sum()));
        modelAndView.addObject("loanPayoffSumSum",
                               SBCustomUtils.round(sortedLoans.stream().mapToDouble(SBLoan::getPayoffSum).sum()));
        modelAndView.addObject("loansSortedSize", sortedLoans.size());
        modelAndView.addObject("loansUserSize", loanDao.findAllByUser(logInUser).size());
        modelAndView.addObject("loanId", loanId);

        modelAndView.addObject("kindsOfLoanSize", kindOfLoanDao.findAll().size());

        List<SBMessage> loanMessages = SBCustomUtils.loanMessages(logInUser, userDao.findAll(), loanDao.findAll());

        modelAndView.addObject("isLoanMessagesEmpty", loanMessages.isEmpty());
        modelAndView.addObject("loanMessages", loanMessages);

        modelAndView.addObject("loanAddInfo", addInfo);
        modelAndView.addObject("loanRemoveInfo", removeInfo);
        modelAndView.addObject("loanModifyInfo", modifyInfo);
        modelAndView.addObject("isLoanUserIdInvalid", isLoanUserIdInvalid);

        return modelAndView;
    }

    /**
     * A method is used to remove a loan record from database.
     *
     * @param id                 A removed record ID.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/loans/remove/{id}", method = RequestMethod.GET)
    public String loansRemoveGet(@PathVariable("id") Long id,
                                 RedirectAttributes redirectAttributes)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser user = userDao.findByUsername(username);

        /* ********************************************************************************** */

        if (user.getId() != loanDao.getOne(id).getUser().getId()
            && !user.getRole().equals("ROLE_ADMIN"))
        {
            redirectAttributes.addFlashAttribute("isExpenseUserIdInvalid", new SBBoolean(true));

            return "redirect:/loans/selectAll";
        }

        /* ********************************************************************************** */


        loanDao.delete(id);

        redirectAttributes.addFlashAttribute("loanRemoveInfo", new SBField(DATA_REMOVE_MSG));

        return "redirect:/loans/selectAll";
    }

    /**
     * A method is used to add a new loan record to database. It shows form to complete.
     *
     * @param loanErrors A error list from a loan form.
     * @param loanJoined An object where the data from a form is located.
     * @param model      It contains all models.
     * @return A logical view name or a value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/loans/add", method = RequestMethod.GET)
    public String loansAddGet(@ModelAttribute("loanErrors") SBList loanErrors,
                              @ModelAttribute("loanJoined") SBLoanJoined loanJoined,
                              Model model)
    {
        if (kindOfLoanDao.findAll().size() == 0)
        {
            return "redirect:/loans/selectAll";
        }

        /* ********************************************************************************** */

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser user = userDao.findByUsername(username);

        model.addAttribute("logInUser", username);
        model.addAttribute("isAdmin", user.getIsAdmin());
        model.addAttribute("isAddOnly", user.getIsAddOnly());
        model.addAttribute("isReadOnly", user.getIsReadOnly());
        model.addAttribute("isModifyOnly", user.getIsModifyOnly());

        /* ********************************************************************************** */

        model.addAttribute("loanJoined", loanJoined);
        model.addAttribute("kindsOfLoan", kindOfLoanDao.findAll());

        model.addAttribute("loanErrors", loanErrors.getList());

        return LOANS_ADD;
    }

    /**
     * A method is used to add a new loan record. It validates {@link SBLoanJoined} object and saves it in database.
     *
     * @param loanJoined         An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBLoanJoined} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/loans/add", method = RequestMethod.POST)
    public String loansAddPost(@ModelAttribute("loanJoined") @Valid SBLoanJoined loanJoined,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes)
    {
        if (kindOfLoanDao.findAll().size() == 0)
        {
            return "redirect:/loans/selectAll";
        }

        /* ********************************************************************************** */

        loanJoinedValidator.validate(loanJoined, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult.getAllErrors()
                                               .stream()
                                               .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                               .collect(Collectors.toList());

            Optional foundErrorOptional =
                    errors.stream()
                          .filter(p -> p.contains("'java.lang.String' to required type 'java.sql.Date'"))
                          .findAny();

            if (foundErrorOptional.isPresent())
            {
                errors.remove(foundErrorOptional.get());
            }

            redirectAttributes.addFlashAttribute("loanErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));
            redirectAttributes.addFlashAttribute("loanJoined", loanJoined);

            return "redirect:/loans/add";
        }

        /* ********************************************************************************** */

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        loanJoinedService.addLoanJoined(loanJoined, logInUser);

        redirectAttributes.addFlashAttribute("loanAddInfo", new SBField(DATA_ADD_MSG));

        return "redirect:/loans/selectAll";
    }

    /**
     * A method is used to modify a loan record from database. It shows form to modify.
     *
     * @param model              It contains all models.
     * @param loanErrors         A error list from a loan form.
     * @param id                 A modified record ID.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A logical view name or a value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/loans/modify/{id}", method = RequestMethod.GET)
    public String loansModifyGet(Model model,
                                 @ModelAttribute("loanErrors") SBList loanErrors,
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

        if (logInUser.getId() != loanDao.getOne(id).getUser().getId() && !logInUser.getIsAdmin())
        {
            redirectAttributes.addFlashAttribute("isLoanUserIdInvalid", new SBBoolean(true));

            return "redirect:/loans/selectAll";
        }

        /* ********************************************************************************** */

        model.addAttribute("loan", loanDao.getOne(id));
        model.addAttribute("kindsOfLoan", kindOfLoanDao.findAll());

        model.addAttribute("loanErrors", loanErrors.getList());

        return LOANS_MODIFY;
    }

    /**
     * A method is used to modify a loan record. It validates {@link SBLoan} object and modifies it.
     *
     * @param loan               An object where the data from a form is located.
     * @param bindingResult      An interface BindingResult implementation which checks {@link SBLoan} object.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/loans/modify", method = RequestMethod.POST)
    public String loansModifyPost(@ModelAttribute("loan") @Valid SBLoan loan,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        /* ********************************************************************************** */

        if (logInUser.getId() != loanDao.getOne(loan.getId()).getUser().getId() && !logInUser.getIsAdmin())
        {
            redirectAttributes.addFlashAttribute("isLoanUserIdInvalid", new SBBoolean(true));

            return "redirect:/loans/selectAll";
        }

        loan.setKindOfLoan(kindOfLoanDao.getOne(loan.getKindOfLoan().getId()));

        /* ********************************************************************************** */

        loanValidator.validate(loan, bindingResult);

        if (bindingResult.hasErrors())
        {
            List<String> errors = bindingResult.getAllErrors()
                                               .stream()
                                               .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                               .collect(Collectors.toList());

            Optional foundErrorOptional =
                    errors.stream()
                          .filter(p -> p.contains("'java.lang.String' to required type 'java.sql.Date'"))
                          .findAny();

            if (foundErrorOptional.isPresent())
            {
                errors.remove(foundErrorOptional.get());
            }

            errors.remove("typeMismatch");

            redirectAttributes.addFlashAttribute("loanErrors",
                                                 new SBList(SBCustomUtils.convertStringListToSBFieldList(errors)));

            return "redirect:/LOANS/modify/" + loan.getId();
        }

        /* ********************************************************************************** */

        SBUser validUser = userDao.getOne(loanDao.getOne(loan.getId()).getUser().getId());

        loanCustomDao.modify(loan, validUser);

        redirectAttributes.addFlashAttribute("loanAddInfo", new SBField(DATA_MODIFY_MSG));

        return "redirect:/loans/selectAll";
    }

    /**
     * A method is used to show loan details.
     *
     * @param model It contains all models.
     * @param id    A chosen record ID.
     * @return A logical view name.
     */
    @RequestMapping(value = "/loans/kindOfLoanDetails/{id}")
    public String loansDetailsGet(Model model,
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

        model.addAttribute("kindOfLoan", loanDao.getOne(id).getKindOfLoan());

        return LOANS_DETAILS;
    }

    /**
     * A method is used to sort a loan list from '/LOANS/selectAll' website.
     *
     * @param loanField          A selected option from a sort bar.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/loans/sort", method = RequestMethod.POST)
    public String loansSortPost(@ModelAttribute("loanField") SBField loanField,
                                RedirectAttributes redirectAttributes)
    {
        String chosenField = loanField.getField().split(" - ")[0];
        String chosenOrder = loanField.getField().split(" - ")[1];

        redirectAttributes.addFlashAttribute("loanField", new SBField(chosenField + " - " + chosenOrder));
        redirectAttributes.addFlashAttribute("chosenEnum", SBLoanFields.getEnum(chosenField, chosenOrder));

        return "redirect:/loans/selectAll";
    }

    /**
     * A method is used to find a record by loan ID.
     *
     * @param loanId             An inserted ID value.
     * @param redirectAttributes An interface {@link RedirectAttributes} implementation
     *                           that allows a sending arguments to other methods.
     * @return A value from @{@link RequestMapping} after redirecting.
     */
    @RequestMapping(value = "/loans/findById", method = RequestMethod.POST)
    public String loansFindByIdPost(@ModelAttribute("expenseId") SBId loanId,
                                    RedirectAttributes redirectAttributes)
    {
        redirectAttributes.addFlashAttribute("loanId", loanId);

        return "redirect:/loans/selectAll";
    }
}
