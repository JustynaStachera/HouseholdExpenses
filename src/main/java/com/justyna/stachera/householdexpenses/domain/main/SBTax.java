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
 * POJO class which represents 'sbtax' table from database.
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
public class SBTax
{
    /**
     * {@link SBTax} object ID. It is auto-generated.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * A year for which you have to pay tax.
     */
    private Integer year;

    /**
     * Earnings.
     */
    private Double amount;

    /**
     * VAT.
     */
    private Double perCent;

    /**
     * Tax to pay.
     */
    private Double amountPayable;

    /**
     * Tax refund.
     */
    private Double taxRefund;

    /**
     * If tax is paid.
     */
    private Boolean isPaid;

    /**
     * Tax description. It is optional.
     */
    private String description;

    /**
     * {@link SBUser} object.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_user")
    private SBUser user;

    @Override
    public String toString()
    {
        return "SBTax{" +
               "id=" + id +
               ", year=" + year +
               ", amount=" + amount +
               ", perCent=" + perCent +
               ", amountPayable=" + amountPayable +
               ", taxRefund=" + taxRefund +
               ", isPaid=" + isPaid +
               ", description='" + description + '\'' +
               ", user=" + user +
               '}';
    }
}
