package com.justyna.stachera.householdexpenses.controller;

import com.justyna.stachera.householdexpenses.constants.SBWebsitesAndMessages;
import com.justyna.stachera.householdexpenses.dao.jrepository.SBUserDao;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * {@link Controller} class with methods that support '/help' website directly.
 */
@Controller
public class SBInfoController implements SBWebsitesAndMessages
{
    private SBUserDao userDao;

    /**
     * An argument constructor autowired interfaces extending
     * {@link org.springframework.data.jpa.repository.JpaRepository}.
     *
     * @param userDao It provides methods related with {@link SBUser} table from database.
     */
    @Autowired
    public SBInfoController(SBUserDao userDao)
    {
        this.userDao = userDao;
    }

    /**
     * A method is used to show a manual.
     *
     * @param model It contains all models.
     * @return A logical view name.
     */
    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String welcomeSiteGet(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        model.addAttribute("logInUser", username);
        model.addAttribute("isAdmin", logInUser.getIsAdmin());
        model.addAttribute("isAddOnly", logInUser.getIsAddOnly());
        model.addAttribute("isReadOnly", logInUser.getIsReadOnly());
        model.addAttribute("isModifyOnly", logInUser.getIsModifyOnly());

        return INFO_AND_HELP;
    }

    /**
     * A method is used to show a manual. It's necessary to log in because its method is POST.
     *
     * @param model It contains all models.
     * @return A logical view name.
     */
    @RequestMapping(value = "/help", method = RequestMethod.POST)
    public String welcomeSitePost(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SBUser logInUser = userDao.findByUsername(username);

        model.addAttribute("logInUser", username);
        model.addAttribute("isAdmin", logInUser.getIsAdmin());
        model.addAttribute("isAddOnly", logInUser.getIsAddOnly());
        model.addAttribute("isReadOnly", logInUser.getIsReadOnly());
        model.addAttribute("isModifyOnly", logInUser.getIsModifyOnly());

        return INFO_AND_HELP;
    }
}