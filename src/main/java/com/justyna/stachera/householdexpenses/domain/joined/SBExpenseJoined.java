package com.justyna.stachera.householdexpenses.domain.joined;

import lombok.*;

import java.sql.Date;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 *
 * POJO class which combines {@link com.justyna.stachera.householdexpenses.domain.main.SBFormOfPayment},
 * {@link com.justyna.stachera.householdexpenses.domain.main.SBKindOfOperation},
 * {@link com.justyna.stachera.householdexpenses.domain.main.SBExpenseCategory} and
 * {@link com.justyna.stachera.householdexpenses.domain.main.SBExpense} classes due to IDs.
 * {@link Lombok} library provides set/get methods and argument/non-argument constructors.
 * {@link Data} annotation causes problems. It contains badly overloaded toString, equals and hashCode methods, so it
 * couldn't be used.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SBExpenseJoined
{
    private String name;

    private Double price;

    private Date dateOfPurchase;

    private Integer daysLeft;

    private Long formOfPaymentId;

    private Long kindOfOperationId;

    private Long expenseCategoryId;

    private String description;

    @Override
    public String toString()
    {
        return "SBExpenseJoinedService{" +
               "name='" + name + '\'' +
               ", price=" + price +
               ", dateOfPurchase=" + dateOfPurchase +
               ", formOfPaymentId=" + formOfPaymentId +
               ", kindOfOperationId=" + kindOfOperationId +
               ", expenseCategoryId=" + expenseCategoryId +
               ", description='" + description + '\'' +
               '}';
    }
}
