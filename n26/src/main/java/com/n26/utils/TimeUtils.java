package com.n26.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtils {

    public long durationBetween(LocalDateTime from, LocalDateTime toExclusive) {
        return Duration.between(from, toExclusive).toMillis();
    }

    public LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

    public long durationToNow(LocalDateTime from) {
        return durationBetween(from, now());
    }

    public long durationFromNow(LocalDateTime from) {
        return durationBetween(now(),from);
    }
}
