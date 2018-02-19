package com.justyna.stachera.householdexpenses.domain.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 * <p>
 * Enum which contains values needed to sort 'sbloan' records and viewing notifications on drop list.
 */
public enum SBLoanFields
{
    id_asc("ID", "rosnąco"),
    id_desc("ID", "malejąco"),
    name_asc("NAZWA", "rosnąco"),
    name_desc("NAZWA", "malejąco"),
    user_asc("UŻYTKOWNIK", "rosnąco"),
    user_desc("UŻYTKOWNIK", "malejąco"),
    initialAmount_asc("KWOTA POCZĄTKOWA", "rosnąco"),
    initialAmount_desc("KWOTA POCZĄTKOWA", "malejąco"),
    payoffSum_asc("SUMA SPŁAT", "rosnąco"),
    payoffSum_desc("SUMA SPŁAT", "malejąco"),
    interest_asc("ODSETKI", "rosnąco"),
    interest_desc("ODSETKI", "malejąco"),
    monthPayment_asc("OPŁATA MIESIĘCZNA", "rosnąco"),
    monthPayment_desc("OPŁATA MIESIĘCZNA", "malejąco"),
    beginDate_asc("DATA ROZPOCZĘCIA", "rosnąco"),
    beginDate_desc("DATA ROZPOCZĘCIA", "malejąco"),
    endDate_asc("DATA ZAKOŃCZENIA", "rosnąco"),
    endDate_desc("DATA ZAKOŃCZENIA", "malejąco"),
    paidUpMonths_asc("OPŁACONE MIESIĄCE", "rosnąco"),
    paidUpMonths_desc("OPŁACONE MIESIĄCE", "malejąco"),
    isActive_asc("CZY AKTYWNE?", "rosnąco"),
    isActive_desc("CZY AKTYWNE?", "malejąco");

    private String value;
    private String order;

    /**
     * Argument constructor.
     *
     * @param value Field name in Polish.
     * @param order Order name in Polish.
     */
    SBLoanFields(String value, String order)
    {
        this.value = value;
        this.order = order;
    }

    /**
     * It returns field name in Polish.
     *
     * @return Field name in Polish.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * It returns order name in Polish.
     *
     * @return Order name in Polish.
     */
    public String getOrder() { return order; }

    /**
     * It returns proper ENUM.
     *
     * @param value Value name in Polish.
     * @param order Order name in Polish.
     * @return One of enums.
     */
    public static String getEnum(String value, String order)
    {
        return Arrays.asList(SBLoanFields.values())
                     .stream()
                     .filter(v -> v.getValue().equalsIgnoreCase(value))
                     .filter(o -> o.getOrder().equalsIgnoreCase(order))
                     .findAny()
                     .get()
                     .toString();
    }

    /**
     * It returns ENUM list.
     *
     * @return List of ENUMS.
     */
    public static List<String> getList()
    {
        List<String> fieldNames = new ArrayList<>();

        for (SBLoanFields s : SBLoanFields.values())
        {
            if (s.getOrder() != null)
            {
                fieldNames.add(s.getValue() + " - " + s.getOrder());
            }
        }

        return fieldNames;
    }
}
