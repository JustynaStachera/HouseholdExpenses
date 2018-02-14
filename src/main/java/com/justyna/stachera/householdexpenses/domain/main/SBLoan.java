package com.justyna.stachera.householdexpenses.domain.main;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author Justyna Stachera.
 * @version 2.0
 * <p>
 * User: jstachera
 * <p>
 * Date: 02.01.2018
 * <p>
 * POJO class which represents 'sbloan' table from database.
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
public class SBLoan
{
    /**
     * {@link SBLoan} object ID. It is auto-generated.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The amount of the loan.
     */
    private Double initialAmount;

    /**
     * The sum of loan and interests.
     */
    private Double payoffSum;

    /**
     * Loan interests.
     */
    private Double interest;

    /**
     * Monthly fee.
     */
    private Double monthPayment;

    /**
     * Date of loan start.
     */
    private Date beginDate;

    /**
     * Date of loan end.
     */
    private Date endDate;

    /**
     * Number of paid months.
     */
    private Integer paidUpMonths;

    /**
     *  If loan is active.
     */
    private Boolean isActive;

    /**
     * Loan description. It is optional.
     */
    private String description;

    /**
     * {@link SBKindOfLoan} object.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_kind_of_loan")
    private SBKindOfLoan kindOfLoan;

    /**
     * {@link SBUser} object.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_user")
    private SBUser user;

    @Override
    public String toString()
    {
        return "SBLoan{" +
               "id=" + id +
               ", initialAmount=" + initialAmount +
               ", beginDate=" + beginDate +
               ", isActive=" + isActive +
               ", description='" + description + '\'' +
               ", kindOfLoan=" + kindOfLoan +
               ", user=" + user +
               '}';
    }
}
