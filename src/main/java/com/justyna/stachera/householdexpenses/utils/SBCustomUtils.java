package com.justyna.stachera.householdexpenses.utils;

import com.justyna.stachera.householdexpenses.domain.helpers.SBDate;
import com.justyna.stachera.householdexpenses.domain.helpers.SBField;
import com.justyna.stachera.householdexpenses.domain.helpers.SBForecasting;
import com.justyna.stachera.householdexpenses.domain.helpers.SBMessage;
import com.justyna.stachera.householdexpenses.domain.main.SBExpense;
import com.justyna.stachera.householdexpenses.domain.main.SBLoan;
import com.justyna.stachera.householdexpenses.domain.main.SBTax;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 * <p>
 * Class which contains custom utils like converting, rounding etc.
 */
public class SBCustomUtils
{
    /**
     * It convert String list to {@link SBField} list.
     *
     * @param stringList List to convert.
     * @return Converted list.
     */
    public static List<SBField> convertStringListToSBFieldList(List<String> stringList)
    {
        List<SBField> fieldList = new ArrayList<>();

        for (String s : stringList)
        {
            fieldList.add(new SBField(s));
        }

        return fieldList;
    }

    /**
     * It rounds value to two decimal places.
     *
     * @param value Value to round.
     * @return Rounded value.
     */
    public static Double round(Double value)
    {
        return new BigDecimal(value.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * It create the recurring payments schedule.
     *
     * @param firstDate First payment date.
     * @param lastDate  Last payment date.
     * @param DAYS_LEFT Interval time between two dates in days. It concerns the recurring payments.
     * @return Recurring payments schedule.
     */
    public static List<List<LocalDate>> createSchedule(LocalDate firstDate, LocalDate lastDate, final int DAYS_LEFT)
    {
        int multiplier = 0;

        List<List<LocalDate>> schedule = new ArrayList<>(new ArrayList<>());

        if (firstDate.compareTo(lastDate) == 0)
        {
            LocalDate lFirstDate = firstDate.plusDays(multiplier + 1).plusDays(multiplier * DAYS_LEFT);
            LocalDate lLastDate = lFirstDate.plusDays(DAYS_LEFT);
            LocalDate[] ranges = {lFirstDate, lLastDate};
            schedule.add(Arrays.asList(ranges));
        }
        else if (firstDate.compareTo(lastDate) < 0)
        {
            while (true)
            {
                LocalDate lFirstDate = firstDate.plusDays(multiplier + 1).plusDays(multiplier * DAYS_LEFT);
                LocalDate lLastDate = lFirstDate.plusDays(DAYS_LEFT);
                LocalDate[] ranges = {lFirstDate, lLastDate};
                schedule.add(Arrays.asList(ranges));

                if (lLastDate.compareTo(lastDate) >= 0 && lFirstDate.compareTo(lastDate) <= 0)
                {
                    break;
                }

                ++multiplier;
            }
        }

        return schedule;
    }

    /**
     * It checks if any date is in period.
     *
     * @param firstDate First payment date.
     * @param lastDate  Last payment date.
     * @param dates     Date list.
     * @return True if date is in period, otherwise false.
     */
    public static boolean isAnyDateInPeriod(LocalDate firstDate, LocalDate lastDate, List<LocalDate> dates)
    {
        for (LocalDate d : dates)
        {
            if (lastDate.compareTo(d) >= 0 && firstDate.compareTo(d) <= 0)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * It checks if date is unique.
     *
     * @param schedule  Recurring payments schedule.
     * @param dates     Date list.
     * @param checkDate Date to check.
     * @return True if date is unique, otherwise false.
     */
    public static boolean isDateUnique(List<List<LocalDate>> schedule, List<LocalDate> dates, LocalDate checkDate)
    {
        for (LocalDate d : dates)
        {
            for (List<LocalDate> s : schedule)
            {
                if (s.get(1).compareTo(d) >= 0 && s.get(0).compareTo(d) <= 0 &&
                    s.get(1).compareTo(checkDate) >= 0 && s.get(0).compareTo(checkDate) <= 0)
                {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * It creates forecasting.
     *
     * @param logInUser  Logged in user.
     * @param allUsers   All user list.
     * @param expenses   Expense list.
     * @param chosenDate The date the forecast is made to.
     * @return Forecast as list.
     */
    public static List<SBForecasting> createForecasting(SBUser logInUser,
                                                        List<SBUser> allUsers,
                                                        List<SBExpense> expenses,
                                                        SBDate chosenDate)
    {
        List<SBForecasting> forecastingList = new ArrayList<>();

        if (expenses.isEmpty())
        {
            return forecastingList;
        }

        if (chosenDate.getDate() == null)
        {
            chosenDate.setDate(Date.valueOf(LocalDate.now()));
        }

        List<SBUser> users = new ArrayList<>();

        if (logInUser.getIsReadOnly())
        {
            users = allUsers;
        }
        else
        {
            users.add(logInUser);
        }

        expenses = expenses.stream()
                           .filter(p -> p.getKindOfOperation().getName().equalsIgnoreCase("CYKLICZNA"))
                           .collect(Collectors.toList());

        for (SBUser user : users)
        {
            List<SBExpense> expenseUserList = expenses.stream()
                                                      .filter(p -> p.getUser().getUsername().equals(user.getUsername()))
                                                      .collect(Collectors.toList());

            List<String> expenseNamesList = expenseUserList.stream()
                                                           .map(SBExpense::getName)
                                                           .distinct()
                                                           .collect(Collectors.toList());

            for (String s : expenseNamesList)
            {
                List<SBExpense> tmpExpenseUserSubList = expenseUserList.stream()
                                                                       .filter(p -> p.getName().equals(s))
                                                                       .sorted(Comparator.comparing(
                                                                               SBExpense::getDateOfPurchase))
                                                                       .collect(Collectors.toList());

                SBExpense firstExpense = tmpExpenseUserSubList.get(0);
                SBForecasting firstRecord = new SBForecasting(firstExpense.getId().toString(),
                                                              firstExpense.getName(),
                                                              user,
                                                              firstExpense.getExpenseCategory(),
                                                              firstExpense.getPrice(),
                                                              firstExpense.getDateOfPurchase().toLocalDate()
                                                                          .minusDays(firstExpense.getDaysLeft()),
                                                              firstExpense.getDateOfPurchase().toLocalDate());

                if (firstExpense.getDateOfPurchase().compareTo(chosenDate.getDate()) <= 0)
                {
                    forecastingList.add(firstRecord);
                }

                Double priceAvg = tmpExpenseUserSubList.stream().mapToDouble(SBExpense::getPrice).sum() /
                                  tmpExpenseUserSubList.size();

                List<LocalDate> tmpDateList = tmpExpenseUserSubList.stream()
                                                                   .map(m -> m.getDateOfPurchase().toLocalDate())
                                                                   .collect(Collectors.toList());

                final int DAYS_LEFT = tmpExpenseUserSubList.get(0).getDaysLeft();
                LocalDate firstDate = tmpDateList.get(0);
                LocalDate lastDate = chosenDate.getDate().toLocalDate();

                List<List<LocalDate>> schedule = createSchedule(firstDate, lastDate, DAYS_LEFT);

                if (!schedule.isEmpty() &&
                    schedule.get(0).get(0).compareTo(chosenDate.getDate().toLocalDate()) <= 0)
                {
                    for (List<LocalDate> period : schedule)
                    {
                        SBForecasting forecasting;
                        SBExpense expense;

                        if (isAnyDateInPeriod(period.get(0), period.get(1), tmpDateList))
                        {
                            expense = tmpExpenseUserSubList
                                    .stream()
                                    .filter(f -> period.get(1).compareTo(f.getDateOfPurchase().toLocalDate()) >= 0 &&
                                                 period.get(0).compareTo(f.getDateOfPurchase().toLocalDate()) <= 0)
                                    .findFirst()
                                    .get();

                            forecasting = new SBForecasting(expense.getId().toString(),
                                                            s,
                                                            user,
                                                            expense.getExpenseCategory(),
                                                            SBCustomUtils.round(expense.getPrice()),
                                                            period.get(0),
                                                            period.get(1));

                            forecastingList.add(forecasting);
                        }
                        else
                        {
                            forecasting = new SBForecasting("P",
                                                            s,
                                                            user,
                                                            tmpExpenseUserSubList.get(0).getExpenseCategory(),
                                                            SBCustomUtils.round(priceAvg),
                                                            period.get(0),
                                                            period.get(1));

                            forecastingList.add(forecasting);
                        }
                    }
                }
            }
        }

        forecastingList.sort(Comparator.comparing(SBForecasting::getFromDate));

        return forecastingList;
    }

    /**
     * It returns expense messages about the recurring payments.
     *
     * @param logInUser   Logged in user.
     * @param allUsers    All user list.
     * @param allExpenses All expense list.
     * @return Expense messages as list.
     */
    public static List<SBMessage> expenseMessages(SBUser logInUser, List<SBUser> allUsers, List<SBExpense> allExpenses)
    {
        List<SBMessage> messages = new ArrayList<>();
        List<SBUser> validUsers = new ArrayList<>();
        List<SBExpense> expenseList;

        if (logInUser.getIsReadOnly())
        {
            validUsers = allUsers;
        }
        else
        {
            validUsers.add(logInUser);
        }

        for (SBUser user : validUsers)
        {
            expenseList = allExpenses
                    .stream()
                    .filter(p -> p.getKindOfOperation().getName().equalsIgnoreCase("CYKLICZNA") &&
                                 p.getUser().getUsername().equals(user.getUsername()))
                    .collect(Collectors.toList());

            List<String> expenseNamesList = expenseList.stream()
                                                       .map(SBExpense::getName)
                                                       .distinct()
                                                       .collect(Collectors.toList());

            for (String name : expenseNamesList)
            {
                List<SBExpense> tmpExpenseList = expenseList.stream()
                                                            .filter(p -> p.getName().equalsIgnoreCase(name))
                                                            .collect(Collectors.toList());

                List<Long> tmpIdsList = tmpExpenseList.stream().map(SBExpense::getId).collect(Collectors.toList());

                tmpExpenseList.sort(Comparator.comparing(SBExpense::getDateOfPurchase));

                List<LocalDate> dates = tmpExpenseList.stream().map(d -> d.getDateOfPurchase().toLocalDate())
                                                      .collect(Collectors.toList());

                final int DAYS_LEFT = tmpExpenseList.get(0).getDaysLeft();
                LocalDate firstDate = dates.get(0);
                LocalDate lastDate = LocalDate.now();
                List<List<LocalDate>> schedule = SBCustomUtils.createSchedule(firstDate, lastDate, DAYS_LEFT);

                for (List<LocalDate> period : schedule)
                {
                    if (!isAnyDateInPeriod(period.get(0), period.get(1), dates))
                    {
                        SBMessage message = new SBMessage(tmpIdsList, name, user, period.get(0), period.get(1),
                                                          "BRAK WPŁATY");

                        messages.add(message);
                    }
                }
            }
        }

        return messages;
    }

    /**
     * It returns loan messages if someone doesn't pay loan.
     *
     * @param logInUser Logged in user.
     * @param allUsers  All user list.
     * @param allLoans  All loan list.
     * @return Loan messages as list.
     */
    public static List<SBMessage> loanMessages(SBUser logInUser, List<SBUser> allUsers, List<SBLoan> allLoans)
    {
        List<SBMessage> messages = new ArrayList<>();

        List<SBUser> users = new ArrayList<>();

        if (logInUser.getIsReadOnly())
        {
            users = allUsers;
        }
        else
        {
            users.add(logInUser);
        }

        for (SBUser user : users)
        {
            List<SBLoan> loanList = allLoans.stream()
                                            .filter(p -> p.getUser().getUsername()
                                                          .equalsIgnoreCase(user.getUsername()) &&
                                                         p.getIsActive())
                                            .collect(Collectors.toList());

            for (SBLoan l : loanList)
            {
                LocalDate tmpBeginDate = l.getBeginDate().toLocalDate();
                LocalDate tmpCurrentDate = LocalDate.now();
                Period age = Period.between(tmpBeginDate, tmpCurrentDate);
                int diff = age.getMonths();

                if (diff >= l.getPaidUpMonths())
                {
                    LocalDate tmpLastDate = tmpBeginDate.plusMonths(l.getPaidUpMonths());

                    for (int i = 0; i < Math.abs(diff - l.getPaidUpMonths()); ++i)
                    {
                        LocalDate fromDate = tmpLastDate.plusMonths(i);
                        LocalDate toDate = tmpLastDate.plusMonths(i + 1);

                        List<Long> tmpIdsList = new ArrayList<Long>()
                        {{
                            add(l.getId());
                        }};

                        SBMessage message = new SBMessage(tmpIdsList, l.getKindOfLoan().getName(), user, fromDate,
                                                          toDate, "BRAK WPŁATY");

                        messages.add(message);
                    }
                }
            }
        }

        return messages;
    }

    /**
     * It returns tax messages if someone doesn't pay tax.
     *
     * @param logInUser Logged in user.
     * @param allUsers  All user list.
     * @param allTaxes  All taxes list.
     * @return Tax messages as list.
     */
    public static List<SBMessage> taxMessages(SBUser logInUser, List<SBUser> allUsers, List<SBTax> allTaxes)
    {
        final LocalDate DEADLINE_TAX_DEPOSIT = LocalDate.of(LocalDate.now().getYear(), 4, 30);
        List<SBMessage> messages = new ArrayList<>();
        List<SBUser> users = new ArrayList<>();

        if (logInUser.getIsReadOnly())
        {
            users = allUsers;
        }
        else
        {
            users.add(logInUser);
        }

        for (SBUser user : users)
        {
            List<SBTax> taxList = allTaxes.stream()
                                          .filter(f -> f.getUser().getUsername().equals(user.getUsername()) &&
                                                       !f.getIsPaid())
                                          .collect(Collectors.toList());

            for (SBTax t : taxList)
            {
                if ((t.getYear().equals(LocalDate.now().getYear() - 1)
                     && LocalDate.now().isBefore(DEADLINE_TAX_DEPOSIT))
                    || t.getYear() < LocalDate.now().getYear())
                {
                    List<Long> tmpIdsList = new ArrayList<Long>()
                    {{
                        add(t.getId());
                    }};

                    SBMessage message = new SBMessage(tmpIdsList, t.getYear().toString(), user, null, null,
                                                      "BRAK ZAREJESTROWANEGO ROZLICZENIA");

                    messages.add(message);
                }
            }
        }

        return messages;
    }
}