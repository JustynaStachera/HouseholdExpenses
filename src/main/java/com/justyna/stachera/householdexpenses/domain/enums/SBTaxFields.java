package com.justyna.stachera.householdexpenses.domain.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 *
 * Enum which contains values needed to sort 'sbtax' records and viewing notifications on drop list.
 */
public enum SBTaxFields
{
    id_asc("ID", "rosnąco"),
    id_desc("ID", "malejąco"),
    user_asc("UŻYTKOWNIK", "rosnąco"),
    user_desc("UŻYTKOWNIK", "malejąco"),
    year_asc("ROK", "rosnąco"),
    year_desc("ROK", "malejąco"),
    perCent_asc("PROCENT", "rosnąco"),
    perCent_desc("PROCENT", "malejąco"),
    amountPayable_asc("PODATEK", "rosnąco"),
    amountPayable_desc("PODATEK", "malejąco"),
    taxRefund_asc("ZWROT PODATKU", "rosnąco"),
    taxRefund_desc("ZWROT PODATKU", "malejąco"),
    isPaid_asc("CZY OPŁACONE", "rosnąco"),
    isPaid_desc("CZY OPŁACONE", "malejąco");

    private String value;
    private String order;

    /**
     * Argument constructor.
     *
     * @param value Field name in Polish.
     * @param order Order name in Polish.
     */
    SBTaxFields(String value, String order)
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
        return this.value;
    }

    /**
     * It returns order name in Polish.
     *
     * @return Order name in Polish.
     */
    public String getOrder() { return this.order; }

    /**
     * It returns proper ENUM.
     *
     * @param value Value name in Polish.
     * @param order Order name in Polish.
     * @return One of enums.
     */
    public static String getEnum(String value, String order)
    {
        return Arrays.asList(SBTaxFields.values())
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

        for (SBTaxFields s : SBTaxFields.values())
        {
            fieldNames.add(s.getValue() + " - " + s.getOrder());
        }

        return fieldNames;
    }
}