package org.isolution.xml.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javax.xml.datatype.XMLGregorianCalendar;

public class XMLGregorianCalendarMatchers extends TypeSafeMatcher<XMLGregorianCalendar>{

    private XMLGregorianCalendar expected;

    public XMLGregorianCalendarMatchers(XMLGregorianCalendar expected) {
        this.expected = expected;
    }

    @Override
    public boolean matchesSafely(XMLGregorianCalendar actual) {
        return actual.getDay() == expected.getDay() &&
                actual.getMonth() == expected.getMonth() &&
                actual.getYear() == expected.getYear() &&
                actual.getTimezone() == expected.getTimezone();
    }

    public void describeTo(Description description) {
        description.appendText(" doesnt match [" + expected.toString() + "]");
    }

    @Factory
    public static Matcher<XMLGregorianCalendar> matchesIgnoringTime(XMLGregorianCalendar expected) {
        return new XMLGregorianCalendarMatchers(expected);
    }
}
