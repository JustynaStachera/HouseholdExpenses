package com.justyna.stachera.householdexpenses.validation;

import com.justyna.stachera.householdexpenses.dao.jrepository.SBPersonDao;
import com.justyna.stachera.householdexpenses.domain.main.SBPerson;
import com.justyna.stachera.householdexpenses.utils.SBPeselValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 * <p>
 * Class which contains all validation for {@link SBPerson} object.
 * It implements {@link Validator} interface.
 */
@Component
@PropertySource("classpath:messages.properties")
public class SBPersonValidator implements Validator
{
    private SBPersonDao personDao;
    private Environment environment;

    /**
     * Argument constructor.
     *
     * @param personDao     It provides methods related with 'sbperson' table from database.
     * @param environment It provides methods to get the error messages from messages.properties file.
     */
    @Autowired
    public SBPersonValidator(SBPersonDao personDao, Environment environment)
    {
        this.personDao = personDao;
        this.environment = environment;
    }

    /**
     * It checks if class to check equals {@link Validator} class type.
     *
     * @param clazz Class to check.
     * @return True if class to check equals {@link Validator} class type, otherwise false.
     */
    @Override
    public boolean supports(Class<?> clazz)
    {
        return SBPerson.class.isAssignableFrom(clazz);
    }

    /**
     * It validates the {@link Object} fields.
     *
     * @param o      {@link Object} to validate.
     * @param errors It provides an error field list.
     */
    @Override
    public void validate(Object o, Errors errors)
    {
        SBPerson person = (SBPerson) o;

        if (isNameEmpty(person))
        {
            errors.rejectValue("name", "SBPerson.name.emptyMismatch", environment.getProperty("SBPerson.name.emptyMismatch"));
        }

        if (isSurnameEmpty(person))
        {
            errors.rejectValue("surname", "SBPerson.surname.emptyMismatch", environment.getProperty("SBPerson.surname.emptyMismatch"));
        }

        if (isAgeNull(person))
        {
            errors.rejectValue("age", "SBPerson.age.nullMismatch", environment.getProperty("SBPerson.age.nullMismatch"));
        }

        if (!isPeselValid(person))
        {
            errors.rejectValue("pesel", "SBPerson.pesel.validMismatch", environment.getProperty("SBPerson.pesel.validMismatch"));
        }

        if (!isPeselUnique(person))
        {
            errors.rejectValue("pesel", "SBPerson.pesel.uniqueMismatch", environment.getProperty("SBPerson.pesel.uniqueMismatch"));
        }
    }

    /**
     * It checks if {@link SBPerson} name field is empty.
     *
     * @param person Object to check.
     * @return True if name field is empty, otherwise false.
     */
    private boolean isNameEmpty(SBPerson person)
    {
        return person.getName().isEmpty();
    }

    /**
     * It checks if {@link SBPerson} surname field is empty.
     *
     * @param person Object to check.
     * @return True if surname field is empty, otherwise false.
     */
    private boolean isSurnameEmpty(SBPerson person)
    {
        return person.getSurname().isEmpty();
    }

    /**
     * It checks if {@link SBPerson} age field is NULL.
     *
     * @param person Object to check.
     * @return True if age field is NULL, otherwise false.
     */
    private boolean isAgeNull(SBPerson person)
    {
        return person.getAge() == null;
    }

    /**
     * It checks if {@link SBPerson} pesel field is valid.
     *
     * @param person Object to check.
     * @return True if pesel field is valid, otherwise false.
     */
    private boolean isPeselValid(SBPerson person)
    {
        return new SBPeselValidator(person.getPesel()).isValid();
    }

    /**
     * It checks if {@link SBPerson} pesel field is unique.
     *
     * @param person Object to check.
     * @return True if pesel field is unique, otherwise false.
     */
    private boolean isPeselUnique(SBPerson person)
    {
        List<SBPerson> people = personDao.findAll();
        List<String> personPesels = people.stream().map(SBPerson::getPesel).collect(Collectors.toList());
        Optional personOptional = people.stream()
                                        .filter(b -> (b.getPesel().equals(person.getPesel())) ||
                                                     (b.getName().equals(person.getName()) &&
                                                      b.getSurname().equals(person.getSurname()) &&
                                                      b.getAge().equals(person.getAge()) &&
                                                      b.getPesel().equals(person.getPesel())))
                                        .findFirst();

        return personOptional.isPresent() || !personPesels.stream().anyMatch(person.getPesel()::equals);
    }
}
