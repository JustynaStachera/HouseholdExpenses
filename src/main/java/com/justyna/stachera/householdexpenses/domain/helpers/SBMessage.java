package com.justyna.stachera.householdexpenses.domain.helpers;

import com.justyna.stachera.householdexpenses.domain.main.SBUser;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Justyna Stachera.
 * User: jstachera
 * Date: 02.01.2018
 *
 * POJO class which represents single message.
 * {@link Lombok} library provides set/get methods and argument/non-argument constructors.
 * {@link Data} annotation causes problems. It contains badly overloaded toString, equals and hashCode methods, so it
 * couldn't be used.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SBMessage
{
    private List<Long> ids;

    private String name;

    private SBUser user;

    private LocalDate from;

    private LocalDate to;

    private String message;

    @Override
    public String toString()
    {
        return "SBMessage{" +
               "ids=" + ids + '\'' +
               "user=" + user.getPerson().getName() + " " + user.getPerson().getSurname() + '\'' +
               ", name='" + name + '\'' +
               ", from=" + from +
               ", to=" + to +
               ", message='" + message + '\'' +
               '}';
    }
}
