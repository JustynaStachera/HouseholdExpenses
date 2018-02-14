package com.justyna.stachera.householdexpenses.domain.main;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * POJO class which represents 'sbkind_of_loan' table from database.
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
public class SBKindOfLoan
{
    /**
     * {@link SBKindOfLoan} object ID. It is auto-generated.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Kind of loan name.
     */
    private String name;

    /**
     * Loan percent.
     */
    private BigDecimal percent;

    /**
     * How long the loan lasts (in months).
     */
    private Integer durationTime;

    /**
     * {@link SBCapitalisation} object.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_capitalisation")
    private SBCapitalisation capitalisation;

    /**
     * {@link SBBank} object.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_bank")
    private SBBank bank;

    @Override
    public String toString()
    {
        return "SBKindOfLoan{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", percent=" + percent +
               ", durationTime=" + durationTime +
               ", capitalisation=" + capitalisation +
               ", bank=" + bank +
               '}';
    }
}