package com.justyna.stachera.householdexpenses.domain.main;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 * <p>
 * POJO class which represents 'sbcapitalisation' table from database.
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
public class SBCapitalisation
{
    /**
     * {@link SBCapitalisation} object ID. It is auto-generated.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Capitalisation name.
     */
    private String name;

    /**
     * {@link SBKindOfLoan} collection including this SBCapitalisation.
     */
    @OneToMany(mappedBy = "capitalisation", cascade = CascadeType.PERSIST)
    private Set<SBKindOfLoan> kindsOfLoan = new LinkedHashSet<>();

    @Override
    public String toString()
    {
        return "SBCapitalisation{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}