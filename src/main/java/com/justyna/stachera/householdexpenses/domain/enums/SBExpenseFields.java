package com.justyna.stachera.householdexpenses.domain.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 * <p>
 * Enum which contains values needed to sort 'sbexpense' records and viewing notifications on drop list.
 */
public enum SBExpenseFields
{
    id_asc("ID", "rosnąco"),
    id_desc("ID", "malejąco"),
    name_asc("NAZWA", "rosnąco"),
    name_desc("NAZWA", "malejąco"),
    user_asc("UŻYTKOWNIK", "rosnąco"),
    user_desc("UŻYTKOWNIK", "malejąco"),
    expenseCategory_asc("RODZAJ KATEGORII", "rosnąco"),
    expenseCategory_desc("RODZAJ KATEGORII", "malejąco"),
    formOfPayment_asc("FORMA ZAPŁATY", "rosnąco"),
    formOfPayment_desc("FORMA ZAPŁATY", "malejąco"),
    kindOfOperation_asc("RODZAJ OPERACJI", "rosnąco"),
    kindOfOperation_desc("RODZAJ OPERACJI", "malejąco"),
    daysLeft_asc("OKRES", "rosnąco"),
    daysLeft_desc("OKRES", "malejąco"),
    price_asc("CENA", "rosnąco"),
    price_desc("CENA", "malejąco"),
    dateOfPurchase_asc("DATA ZAKUPU", "rosnąco"),
    dateOfPurchase_desc("DATA ZAKUPU", "malejąco");
    
    private String value;
    private String order;
    
    /**
     * Argument constructor.
     *
     * @param value Field name in Polish.
     * @param order Order name in Polish.
     */
    SBExpenseFields(String value, String order)
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
        return Arrays.asList(SBExpenseFields.values())
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
        
        for (SBExpenseFields s : SBExpenseFields.values())
        {
            if (s.getOrder() != null)
            {
                fieldNames.add(s.getValue() + " - " + s.getOrder());
            }
        }
        
        return fieldNames;
    }
}
