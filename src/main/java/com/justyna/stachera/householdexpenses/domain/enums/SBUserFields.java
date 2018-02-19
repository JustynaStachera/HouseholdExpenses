package com.justyna.stachera.householdexpenses.domain.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 * <p>
 * Enum which contains values needed to sort 'sbuser' records and viewing notifications on drop list.
 */
public enum SBUserFields
{
    id_asc("ID", "rosnąco"),
    id_desc("ID", "malejąco"),
    username_asc("LOGIN", "rosnąco"),
    username_desc("LOGIN", "malejąco"),
    is_admin_asc("ADMIN", "rosnąco"),
    is_admin_desc("ADMIN", "malejąco"),
    is_read_asc("ODCZYT", "rosnąco"),
    is_read_desc("ODCZYT", "malejąco"),
    is_add_asc("DODAWANIE", "rosnąco"),
    is_add_desc("DODAWANIE", "malejąco"),
    is_modify_asc("MODYFIKACJA", "rosnąco"),
    is_modify_desc("MODYFIKACJA", "malejąco"),
    name_asc("IMIĘ", "rosnąco"),
    name_desc("IMIĘ", "malejąco"),
    surname_asc("NAZWISKO", "rosnąco"),
    surname_desc("NAZWISKO", "malejąco"),
    age_asc("WIEK", "rosnąco"),
    age_desc("WIEK", "malejąco"),
    PESEL_asc("PESEL", "rosnąco"),
    PESEL_desc("PESEL", "malejąco");

    private String value;
    private String order;

    /**
     * Argument constructor.
     *
     * @param value Field name in Polish.
     * @param order Order name in Polish.
     */
    SBUserFields(String value, String order)
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
        return Arrays.asList(SBUserFields.values())
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

        for (SBUserFields s : SBUserFields.values())
        {
            fieldNames.add(s.getValue() + " - " + s.getOrder());
        }

        return fieldNames;
    }
}