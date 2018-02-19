package com.justyna.stachera.householdexpenses.domain.helpers;

import com.justyna.stachera.householdexpenses.domain.main.SBExpenseCategory;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 * <p>
 * POJO class which represents single forecast.
 * {@link lombok.Lombok} library provides set/get methods and argument/non-argument constructors.
 * {@link Data} annotation causes problems. It contains badly overloaded toString, equals and hashCode methods, so it
 * couldn't be used.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SBForecasting
{
    private String id;

    private String name;

    private SBUser user;

    private SBExpenseCategory expenseCategory;

    private Double price;

    private LocalDate fromDate;

    private LocalDate toDate;

    @Override
    public String toString()
    {
        return "SBForecasting{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", user=" + user +
               ", expenseCategory=" + expenseCategory +
               ", price=" + price +
               ", fromDate=" + fromDate +
               ", toDate=" + toDate +
               '}';
    }
}
