package com.justyna.stachera.householdexpenses.domain.main;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 *
 * POJO class which represents 'sbexpense' table from database.
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
public class SBExpense
{
    /**
     * {@link SBExpense} object ID. It is auto-generated.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Expense name.
     */
    private String name;

    /**
     * Expense price.
     */
    private Double price;

    /**
     * Date of purchase.
     */
    private Date dateOfPurchase;

    /**
     * Interval time between two dates in days. It concerns the recurring payments.
     */
    private Integer daysLeft;

    /**
     * Expense description. It is optional.
     */
    private String description;

    /**
     * {@link SBFormOfPayment} object.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_form_of_payment")
    private SBFormOfPayment formOfPayment;

    /**
     * {@link SBKindOfOperation} object.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_kind_of_operation")
    private SBKindOfOperation kindOfOperation;

    /**
     * {@link SBExpenseCategory} object.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_expense_category")
    private SBExpenseCategory expenseCategory;

    /**
     * {@link SBUser} object.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_user")
    private SBUser user;

    @Override
    public String toString()
    {
        return "SBExpense{" +
               "id=" + id +
               ", formOfPayment=" + formOfPayment.getName() +
               ", kindOfOperation=" + kindOfOperation.getName() +
               ", expenseCategory=" + expenseCategory.getName() +
               ", name='" + name + '\'' +
               ", price=" + price +
               ", dateOfPurchase=" + dateOfPurchase + '\'' +
               ", daysLeft=" + daysLeft + '\'' +
               ", description='" + description + '\'' +
               '}';
    }
}