package com.justyna.stachera.householdexpenses.utils;

import com.justyna.stachera.householdexpenses.domain.main.SBExpense;
import com.justyna.stachera.householdexpenses.domain.main.SBLoan;
import com.justyna.stachera.householdexpenses.domain.main.SBTax;
import com.justyna.stachera.householdexpenses.domain.main.SBUser;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 *
 * Class which contains statistics utils to generate the charts.
 */
public class SBStatisticsUtils
{
    private static Map<String, Double> expenseData = new TreeMap<>();
    private static Map<String, Double> expenseCategoryData = new TreeMap<>();
    private static Map<String, Double> loanData = new TreeMap<>();
    private static Map<String, Double> taxAmountData = new TreeMap<>();
    private static Map<String, Double> taxAmountPayableData = new TreeMap<>();

    /**
     * It creates expense map.
     *
     * @param users User list.
     * @param expenses Expense list.
     * @return Generated map with user expenses data.
     */
    public static Map<String, Double> createExpenseMap(List<SBUser> users, List<SBExpense> expenses)
    {
        users.sort(Comparator.comparing(SBUser::getUsername));

        for (SBUser user : users)
        {
            String key = user.getPerson().getName() + " " + user.getPerson().getSurname();
            Double value = SBCustomUtils.round(expenses.stream()
                                                       .filter(p -> p.getUser().getUsername().equals(user.getUsername()))
                                                       .mapToDouble(SBExpense::getPrice).sum());

            expenseData.put(key, value);
        }

        return expenseData;
    }

    /**
     * It returns expense users list.
     *
     * @return Expense users list.
     */
    public static List<String> getExpenseUsers()
    {
        return expenseData.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * It returns expense prices sum.
     *
     * @return Expense prices sum.
     */
    public static List<Double> getExpensePricesSum()
    {
        return expenseData.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    /**
     * It returns expense title.
     *
     * @return Expense title.
     */
    public static String getExpenseTitle()
    {
        return "WYDATKI: STATYSTYKA CEN WG UŻYTKOWNIKÓW";
    }

    /**
     * It returns expense label.
     *
     * @return Expense label.
     */
    public static String getExpenseLabel()
    {
        return "Suma cen [zł]";
    }

    /**
     * It creates expense category map.
     *
     * @param expenses Expense list.
     * @return Generated map with expense categories data.
     */
    public static Map<String, Double> createExpenseCategoryMap(List<SBExpense> expenses)
    {
        List<String> expenseCategories = expenses.stream()
                                                 .map(e -> e.getExpenseCategory().getName())
                                                 .distinct()
                                                 .collect(Collectors.toList());

        for (String expenseCategory : expenseCategories)
        {
            String key = expenseCategory;
            Double value = SBCustomUtils.round(expenses.stream()
                                                       .filter(e -> e.getExpenseCategory().getName().equals(key))
                                                       .mapToDouble(SBExpense::getPrice).sum());

            expenseCategoryData.put(key, value);
        }

        return expenseCategoryData;
    }

    /**
     * It returns expense category list.
     *
     * @return Expense category list.
     */
    public static List<String> getExpenseCategories()
    {
        return expenseCategoryData.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * It returns expense category prices sum.
     *
     * @return Expense category prices sum.
     */
    public static List<Double> getExpenseCategoryPricesSum()
    {
        return expenseCategoryData.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    /**
     * It returns expense category title.
     *
     * @return Expense category title.
     */
    public static String getExpenseCategoryTitle()
    {
        return "WYDATKI: STATYSTYKA CEN WEDŁUG KATEGORII WYDATKÓW";
    }

    /**
     * It returns expense category label.
     *
     * @return Expense category label.
     */
    public static String getExpenseCategoryLabel()
    {
        return "Suma cen [zł]";
    }

    /**
     * It creates loan map.
     *
     * @param users User list.
     * @param loans Loan list.
     * @return Generated map with user loans data.
     */
    public static Map<String, Double> createLoanMap(List<SBUser> users, List<SBLoan> loans)
    {
        users.sort(Comparator.comparing(SBUser::getUsername));

        for (SBUser user : users)
        {
            String key = user.getPerson().getName() + " " + user.getPerson().getSurname();
            Double value = SBCustomUtils.round(loans.stream()
                                                    .filter(p -> p.getUser().getUsername().equals(user.getUsername()))
                                                    .mapToDouble(SBLoan::getPayoffSum).sum());

            loanData.put(key, value);
        }

        return loanData;
    }

    /**
     * It returns loan users list.
     *
     * @return Loan users list.
     */
    public static List<String> getLoanUsers()
    {
        return loanData.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * It returns loan payoff sum.
     *
     * @return Loan payoff sum.
     */
    public static List<Double> getLoanPayoffSum()
    {
        return loanData.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    /**
     * It returns loan title.
     *
     * @return Loan title.
     */
    public static String getLoanTitle()
    {
        return "DEBET: STATYSTYKA SUMY SPŁAT WEDŁUG UŻYTKOWNIKÓW";
    }

    /**
     * It returns loan label.
     *
     * @return Loan label.
     */
    public static String getLoanLabel()
    {
        return "Suma spłat [zł]";
    }

    /**
     * It creates tax amount map.
     *
     * @param users User list.
     * @param taxes Tax list.
     * @return Generated map with user taxes data.
     */
    public static Map<String, Double> createTaxAmountMap(List<SBUser> users, List<SBTax> taxes)
    {
        users.sort(Comparator.comparing(SBUser::getUsername));

        for (SBUser user : users)
        {
            String key = user.getPerson().getName() + " " + user.getPerson().getSurname();
            Double value = SBCustomUtils.round(taxes.stream()
                                                    .filter(p -> p.getUser().getUsername().equals(user.getUsername()))
                                                    .mapToDouble(SBTax::getAmount).sum());

            taxAmountData.put(key, value);
        }

        return taxAmountData;
    }

    /**
     * It returns tax amount users list.
     *
     * @return Tax amount users list.
     */
    public static List<String> getTaxAmountUsers()
    {
        return taxAmountData.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * It returns tax amount sum.
     *
     * @return Tax amount sum.
     */
    public static List<Double> getTaxAmountSum()
    {
        return taxAmountData.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    /**
     * It returns tax amount title.
     *
     * @return Tax amount title.
     */
    public static String getTaxAmountTitle()
    {
        return "PODATKI: STATYSTYKA ZAROBKÓW WEDŁUG UŻYTKOWNIKÓW";
    }

    /**
     * It returns tax amount label.
     *
     * @return Tax amount label.
     */
    public static String getTaxAmountLabel()
    {
        return "Suma zarobków [zł]";
    }

    /**
     * It creates tax amount payable map.
     *
     * @param users User list.
     * @param taxes Tax list.
     * @return Generated map with user tax amount payable data.
     */
    public static Map<String, Double> createTaxAmountPayableMap(List<SBUser> users, List<SBTax> taxes)
    {
        users.sort(Comparator.comparing(SBUser::getUsername));

        for (SBUser user : users)
        {
            String key = user.getPerson().getName() + " " + user.getPerson().getSurname();
            Double value = SBCustomUtils.round(taxes.stream()
                                                    .filter(p -> p.getUser().getUsername().equals(user.getUsername()))
                                                    .mapToDouble(SBTax::getAmountPayable).sum());

            taxAmountPayableData.put(key, value);
        }

        return taxAmountPayableData;
    }

    /**
     * It returns tax amount payable users.
     *
     * @return Tax amount payable users.
     */
    public static List<String> getTaxAmountPayableUsers()
    {
        return taxAmountPayableData.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * It returns tax amount payable sum.
     *
     * @return Tax amount payable sum.
     */
    public static List<Double> getTaxAmountPayableSum()
    {
        return taxAmountPayableData.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    /**
     * It returns tax amount payable title.
     *
     * @return Tax amount payable title.
     */
    public static String getTaxAmountPayableTitle()
    {
        return "PODATKI: STATYSTYKA PODATKÓW WEDŁUG UŻYTKOWNIKÓW";
    }

    /**
     * It return tax amount payable label.
     *
     * @return Tax amount payable label.
     */
    public static String getTaxAmountPayableLabel()
    {
        return "Suma podatków [zł]";
    }
}
