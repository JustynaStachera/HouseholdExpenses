package com.justyna.stachera.householdexpenses.domain.main;

import lombok.*;

import javax.persistence.*;
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
 * POJO class which represents 'sbkind_of_operation' table from database.
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
public class SBKindOfOperation
{
    /**
     * {@link SBKindOfLoan} object ID. It is auto-generated.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Kind of operation name.
     */
    private String name;

    /**
     * {@link SBExpense} collection including this {@link SBKindOfOperation} object.
     */
    @OneToMany(mappedBy = "kindOfOperation", cascade = CascadeType.PERSIST)
    private Set<SBExpense> expenses = new LinkedHashSet<>();

    @Override
    public String toString()
    {
        return "SBKindOfOperation{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}
