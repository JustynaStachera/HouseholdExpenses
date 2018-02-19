package com.justyna.stachera.householdexpenses.dao.custom;

import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import com.justyna.stachera.householdexpenses.security.enums.SBRoles;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * Class which contains implemented methods from {@link SBUserCustomDao} interface.
 */
@Repository
public class SBUserCustomImpl implements SBUserCustomDao
{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void modifyData(SBUser user)
    {
        SBUser findUser = entityManager.find(SBUser.class, user.getId());

        findUser.setUsername(user.getUsername());
        findUser.setIsAdmin(user.getIsAdmin());
        findUser.setIsAddOnly(user.getIsAddOnly());
        findUser.setIsReadOnly(user.getIsReadOnly());
        findUser.setIsModifyOnly(user.getIsModifyOnly());
        findUser.setRole(user.getRole());

        /**
         * To avoid any errors during comparing passwords, this operation is required.
         */
        findUser.setConfirmPassword(findUser.getPassword());

        findUser.getPerson().setName(user.getPerson().getName());
        findUser.getPerson().setSurname(user.getPerson().getSurname());
        findUser.getPerson().setAge(user.getPerson().getAge());
        findUser.getPerson().setPesel(user.getPerson().getPesel());
    }

    @Override
    @Transactional
    public void modifyPassword(SBUser user)
    {
        SBUser findUser = entityManager.find(SBUser.class, user.getId());

        findUser.setPassword(user.getPassword());

        /**
         * To avoid any errors during comparing passwords, this operation is required.
         */
        findUser.setConfirmPassword(user.getPassword());
    }

    @Override
    @Transactional
    public List<SBUser> sortTableBy(String chosenEnum)
    {
        Query query;

        switch (chosenEnum)
        {
        case "id_asc":
            query = entityManager.createQuery("select b from SBUser b order by b.id asc");
            break;
        case "id_desc":
            query = entityManager.createQuery("select b from SBUser b order by b.id desc");
            break;
        case "username_asc":
            query = entityManager.createQuery("select b from SBUser b order by b.username asc");
            break;
        case "username_desc":
            query = entityManager.createQuery("select b from SBUser b order by b.username desc");
            break;
        case "is_admin_asc":
            query = entityManager.createQuery("select b from SBUser b order by b.isAdmin asc");
            break;
        case "is_admin_desc":
            query = entityManager.createQuery("select b from SBUser b order by b.isAdmin desc");
            break;
        case "is_read_asc":
            query = entityManager.createQuery("select b from SBUser b order by b.isReadOnly asc");
            break;
        case "is_read_desc":
            query = entityManager.createQuery("select b from SBUser b order by b.isReadOnly desc");
            break;
        case "is_add_asc":
            query = entityManager.createQuery("select b from SBUser b order by b.isAddOnly asc");
            break;
        case "is_add_desc":
            query = entityManager.createQuery("select b from SBUser b order by b.isAddOnly desc");
            break;
        case "is_modify_asc":
            query = entityManager.createQuery("select b from SBUser b order by b.isModifyOnly asc");
            break;
        case "is_modify_desc":
            query = entityManager.createQuery("select b from SBUser b order by b.isModifyOnly desc");
            break;
        case "name_asc":
            query = entityManager.createQuery("select b from SBUser b order by b.person.name asc");
            break;
        case "name_desc":
            query = entityManager.createQuery("select b from SBUser b order by b.person.name desc");
            break;
        case "surname_asc":
            query = entityManager.createQuery("select b from SBUser b order by b.person.surname asc");
            break;
        case "surname_desc":
            query = entityManager.createQuery("select b from SBUser b order by b.person.surname desc");
            break;
        case "age_asc":
            query = entityManager.createQuery("select b from SBUser b order by b.person.age asc");
            break;
        case "age_desc":
            query = entityManager.createQuery("select b from SBUser b order by b.person.age desc");
            break;
        case "PESEL_asc":
            query = entityManager.createQuery("select b from SBUser b order by b.person.pesel asc");
            break;
        case "PESEL_desc":
            query = entityManager.createQuery("select b from SBUser b order by b.person.pesel desc");
            break;
        default:
            query = entityManager.createQuery("select b from SBUser b order by b.id asc");
            break;
        }

        return (List<SBUser>) query.getResultList();
    }

    @Override
    @Transactional
    public void assignRole(SBUser user)
    {
        String role = "";

        if (user.getIsAdmin())
        {
            role = SBRoles.ROLE_ADMIN.toString();
        }
        else if (!user.getIsAdmin() && user.getIsReadOnly()
                 && !user.getIsAddOnly() && !user.getIsModifyOnly())
        {
            role = SBRoles.ROLE_USER_R.toString();
        }
        else if (!user.getIsAdmin() && user.getIsReadOnly()
                 && user.getIsAddOnly() && user.getIsModifyOnly())
        {
            role = SBRoles.ROLE_USER_ARM.toString();
        }
        else if (!user.getIsAdmin() && !user.getIsReadOnly()
                 && user.getIsAddOnly() && !user.getIsModifyOnly())
        {
            role = SBRoles.ROLE_USER_A.toString();
        }
        else if (!user.getIsAdmin() && !user.getIsReadOnly()
                 && user.getIsAddOnly() && user.getIsModifyOnly())
        {
            role = SBRoles.ROLE_USER_AM.toString();
        }
        else if (!user.getIsAdmin() && user.getIsReadOnly()
                 && !user.getIsAddOnly() && user.getIsModifyOnly())
        {
            role = SBRoles.ROLE_USER_RM.toString();
        }
        else if (!user.getIsAdmin() && user.getIsReadOnly()
                 && user.getIsAddOnly() && !user.getIsModifyOnly())
        {
            role = SBRoles.ROLE_USER_AR.toString();
        }

        user.setRole(role);
    }
}