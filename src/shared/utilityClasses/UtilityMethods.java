package shared.utilityClasses;

import java.sql.Time;
import java.time.LocalDate;
import java.sql.Date;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class UtilityMethods {

    public static String formatDate(Date date) {
        LocalDate localDate = date.toLocalDate();
        return localDate.getMonth().name() + " " + localDate.getDayOfMonth() +  ", " + localDate.getYear() +  " " + localDate.getDayOfWeek().name().toUpperCase();
    }

    public static String formatTime(Time time) {
        LocalTime localTime = time.toLocalTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        return localTime.format(formatter);
    }

    public static boolean haveNullOrEmpty(String... strings){
        for (String str : strings) {
            if (str == null || str.trim().isEmpty()){
                return true;
            }
        }
        return false;
    }


}
