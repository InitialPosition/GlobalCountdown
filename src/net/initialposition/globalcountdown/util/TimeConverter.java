package net.initialposition.globalcountdown.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class TimeConverter {

    public static Date convertTimestampToDate(long timestamp) {
        return new Date(timestamp);
    }

    public static long convertDateToTimestamp(Date date) {
        Timestamp ts = new Timestamp(date.getTime());
        return ts.getTime() / 1000;     // divide by 1000 since java timestamps work in ms
    }

    public static long calculateRemainingTimeFromDate(Date finalDate) {
        Date currentDate = Calendar.getInstance().getTime();

        return (finalDate.getTime() - currentDate.getTime()) / 1000;
    }

    public static Date getCurrentDatePlusThirtyDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        return calendar.getTime();
    }
}
