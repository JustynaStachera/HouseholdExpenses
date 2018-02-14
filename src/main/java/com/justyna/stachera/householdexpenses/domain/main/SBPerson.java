package com.justyna.stachera.householdexpenses.domain.main;

import lombok.*;

import javax.persistence.*;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * POJO class which represents 'sbperson' table from database.
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
public class SBPerson
{
    /**
     * {@link SBPerson} object ID. It is auto-generated.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Person name.
     */
    private String name;

    /**
     * Person surname.
     */
    private String surname;

    /**
     * Person age.
     */
    private Integer age;

    /**
     * Person PESEL.
     */
    private String pesel;

    /**
     * {@link SBUser} object.
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_user")
    private SBUser user;

    @Override
    public String toString()
    {
        return "SBPerson{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               ", age=" + age +
               ", PESEL='" + pesel + '\'' +
               '}';
    }
}
