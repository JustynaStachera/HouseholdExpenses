package com.justyna.stachera.householdexpenses.domain.joined;

import lombok.*;

import java.sql.Date;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 *
 * POJO class which combines {@link com.justyna.stachera.householdexpenses.domain.main.SBLoan} and
 * {@link com.justyna.stachera.householdexpenses.domain.main.SBKindOfLoan} classes due to IDs.
 * {@link Lombok} library provides set/get methods and argument/non-argument constructors.
 * {@link Data} annotation causes problems. It contains badly overloaded toString, equals and hashCode methods, so it
 * couldn't be used.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SBLoanJoined
{
    private Double initialAmount;

    private Double payoffSum;

    private Double interest;

    private Double monthPayment;

    private Date beginDate;

    private Date endDate;

    private Integer paidUpMonths;

    private Boolean isActive;

    private String description;

    private Long kindOfLoanId;

    @Override
    public String toString()
    {
        return "SBLoanJoined{" +
               "initialAmount=" + initialAmount +
               ", payoffSum=" + payoffSum +
               ", interest=" + interest +
               ", monthPayment=" + monthPayment +
               ", beginDate=" + beginDate +
               ", endDate=" + endDate +
               ", paidUpMonths=" + paidUpMonths +
               ", isActive=" + isActive +
               ", description='" + description + '\'' +
               ", kindOfLoanId=" + kindOfLoanId +
               '}';
    }
}
