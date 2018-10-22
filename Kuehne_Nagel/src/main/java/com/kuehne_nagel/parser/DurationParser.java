package com.kuehne_nagel.parser;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationParser implements Function<String, Long> {

    private static final Pattern TIME_PATTERN = Pattern.compile("^(\\d{1,2})[.:](\\d{2}).(\\d{2})$");

    @Override
    public Long apply(String input) {
        Matcher matcher = TIME_PATTERN.matcher(input);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Not a valid duration representation mm.ss.SS expected");
        }
        long minutes = Integer.valueOf(matcher.group(1));
        if (minutes >= 60)
            throw new IllegalArgumentException("Invalid minutes");
        int seconds = Integer.valueOf(matcher.group(2));
        if (seconds >= 60)
            throw new IllegalArgumentException("Invalid seconds");
        int millis = Integer.valueOf(matcher.group(3));
        return TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(seconds) + millis;
    }
}
