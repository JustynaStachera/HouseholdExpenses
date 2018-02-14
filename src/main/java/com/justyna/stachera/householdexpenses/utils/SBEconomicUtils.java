package com.justyna.stachera.householdexpenses.utils;

import com.justyna.stachera.householdexpenses.domain.main.SBLoan;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 *
 * Class which contains economic utils to generate data.
 */
public class SBEconomicUtils
{
    /**
     * Threshold constant for year 2017.
     */
    public final static Double THRESHOLD = 85528.0;

    /**
     * First tax threshold for year 2017.
     */
    public final static Double FIRST_TAX_THRESHOLD = 18.0;

    /**
     * Second tax threshold for year 2017.
     */
    public final static Double SECOND_TAX_THRESHOLD = 32.0;

    /**
     *
     * It calculates loan fields like month payment, payoff sum and interests.
     * It also checks if loan is active.
     *
     * We assume that we have the capitalisations: monthly, quarterly.
     *
     * @param loan Loan to calculate.
     * @return Loan with completed data.
     */
    public static SBLoan calculateLoan(SBLoan loan)
    {
        LocalDate convertBeginDate = loan.getBeginDate().toLocalDate();
        loan.setEndDate(Date.valueOf(convertBeginDate.plusMonths(loan.getKindOfLoan().getDurationTime())));

        LocalDate convertEndDate = loan.getEndDate().toLocalDate();

        Integer capitalisationValue = loan.getKindOfLoan()
                                          .getCapitalisation()
                                          .getName()
                                          .equalsIgnoreCase("MIESIĘCZNA") ? 12 : 4;

        Double y = 1 + (loan.getKindOfLoan().getPercent().doubleValue() / 100.0) / capitalisationValue;

        Integer localDurationTime = loan.getKindOfLoan().getCapitalisation().getName().equalsIgnoreCase
                ("MIESIĘCZNA") ? loan.getKindOfLoan().getDurationTime() : loan.getKindOfLoan().getDurationTime() / 3;

        Double localMonthPayment = SBCustomUtils.round(loan.getInitialAmount() * Math.pow(y, localDurationTime) *
                                                       (y - 1.0) / (Math.pow(y, localDurationTime) - 1.0));
        loan.setMonthPayment(localMonthPayment);

        Double localPayoffSum = SBCustomUtils.round(loan.getMonthPayment() * localDurationTime);
        loan.setPayoffSum(localPayoffSum);

        Double localInterest = SBCustomUtils.round(loan.getPayoffSum() - loan.getInitialAmount());
        loan.setInterest(localInterest);

        if (LocalDate.now().equals(convertBeginDate)
            || LocalDate.now().isAfter(convertBeginDate) && LocalDate.now().isBefore(convertEndDate))
        {
            loan.setIsActive(true);
        }
        else
        {
            loan.setIsActive(false);
        }

        return loan;
    }

    /**
     * It returns tax per cent.
     *
     * @param amount Earnings.
     * @return Tax per cent.
     */
    public static Double getTaxPerCent(Double amount)
    {
        if (amount <= THRESHOLD)
        {
            return FIRST_TAX_THRESHOLD;
        }
        else if (amount > THRESHOLD)
        {
            return SECOND_TAX_THRESHOLD;
        }

        return 0.0;
    }

    /**
     * It returns tax to pay.
     *
     * @param taxPerCent Tax per cent.
     * @param amount Earnings.
     * @return Tax to pay.
     */
    public static Double getAmountPayable(Double taxPerCent, Double amount)
    {
        return taxPerCent / 100.0 * amount;
    }
}
