package com.justyna.stachera.householdexpenses.domain.joined;

import lombok.*;

import java.math.BigDecimal;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 *
 * POJO class which combines {@link com.justyna.stachera.householdexpenses.domain.main.SBFormOfPayment},
 * {@link com.justyna.stachera.householdexpenses.domain.main.SBBank},
 * {@link com.justyna.stachera.householdexpenses.domain.main.SBCapitalisation} and
 * {@link com.justyna.stachera.householdexpenses.domain.main.SBKindOfLoan} classes due to IDs.
 * {@link Lombok} library provides set/get methods and argument/non-argument constructors.
 * {@link Data} annotation causes problems. It contains badly overloaded toString, equals and hashCode methods, so it
 * couldn't be used.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SBKindOfLoanJoined
{
    private String name;

    private BigDecimal percent;

    private Integer durationTime;

    private Long bankId;

    private Long capitalisationId;

    @Override
    public String toString()
    {
        return "SBKindOfLoanJoined{" +
               "name='" + name + '\'' +
               ", bankId=" + bankId +
               ", percent=" + percent +
               ", durationTime=" + durationTime +
               '}';
    }
}