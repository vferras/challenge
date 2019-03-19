package com.n26.helper;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class TimeHelper {

    public static final int MAX_MILLISECONDS = 60000;

    public long getCurrentMilliseconds() {
        return OffsetDateTime.now(ZoneOffset.UTC).toInstant().toEpochMilli();
    }
}
