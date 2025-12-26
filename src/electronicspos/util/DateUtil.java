package electronicspos.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    // Returns today's date in "dd-MM-yyyy" format
    public static String today() {
        return format(new Date(), "dd-MM-yyyy");
    }

    // Returns the date formatted according to the given pattern
    public static String format(Date date, String pattern) {
        if (date == null || pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Date and pattern must not be null");
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    // Example usage
    public static void main(String[] args) {
        System.out.println("Today: " + today());
        System.out.println("Custom format: " + format(new Date(), "yyyy/MM/dd HH:mm:ss"));
    }
}