package org.simpleflatmapper.converter.joda.impl;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormatter;
import org.simpleflatmapper.converter.Converter;


public class CharacterSequenceToJodaInstantConverter implements Converter<CharSequence, Instant> {
    private final DateTimeFormatter dateTimeFormatter;

    public CharacterSequenceToJodaInstantConverter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @Override
    public Instant convert(CharSequence in) throws Exception {
        if (in == null || in.length() == 0) return null;
        return dateTimeFormatter.parseDateTime(String.valueOf(in)).toInstant();
    }
}
