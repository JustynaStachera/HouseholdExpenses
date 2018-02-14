package com.justyna.stachera.householdexpenses.domain.helpers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 *
 * POJO class which wraps {@link SBField} list.
 * {@link lombok.Lombok} library provides set/get methods and argument/non-argument constructors.
 * {@link Data} annotation causes problems. It contains badly overloaded toString, equals and hashCode methods, so it
 * couldn't be used.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SBList
{
    private List<SBField> list;
}
