package com.justyna.stachera.householdexpenses.domain.main;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * POJO class which represents 'sbuser' table from database.
 * {@link Lombok} library provides set/get methods and argument/non-argument constructors.
 * {@link Data} annotation causes problems. It contains badly overloaded toString, equals and hashCode methods, so it
 * couldn't be used.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SBUser
{
    /**
     * {@link SBUser} object ID. It is auto-generated.
     */
    @Id
    @GeneratedValue
    private Long id;
    
    /**
     * Username.
     */
    private String username;
    
    /**
     * Password.
     */
    private String password;
    
    /**
     * Variable to check password. It's not saved in the database.
     */
    @Transient
    private String confirmPassword;
    
    /**
     * If user is admin.
     */
    private Boolean isAdmin;
    
    /**
     * If user has READ_ONLY right.
     */
    private Boolean isReadOnly;
    
    /**
     * If user has ADD_ONLY right.
     */
    private Boolean isAddOnly;
    
    /**
     * If user has MODIFY_ONLY right.
     */
    private Boolean isModifyOnly;
    
    /**
     * User role.
     */
    private String role;
    
    /**
     * {@link SBPerson} object.
     * {@literal @}Valid annotation is required because BindingResult catches errors from SBPerson, when
     * {@link SBUser} is validated.
     */
    @Valid
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private SBPerson person;
    
    /**
     * SBTax collection including this {@link SBUser} object.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Set<SBTax> taxes = new LinkedHashSet<>();
    
    /**
     * {@link SBLoan} collection including this {@link SBUser} object.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Set<SBLoan> loans = new LinkedHashSet<>();
    
    /**
     * {@link SBExpense} collection including this {@link SBUser} object.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Set<SBExpense> expenses = new LinkedHashSet<>();
    
    @Override
    public String toString()
    {
        return "SBUser{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", isAdmin=" + isAdmin +
               ", isReadOnly=" + isReadOnly +
               ", isAddOnly=" + isAddOnly +
               ", isModifyOnly=" + isModifyOnly +
               ", role='" + role + '\'' +
               ", person='" + person + '\'' +
               '}';
    }
}