package shared.utilityClasses;

import shared.referenceClasses.Genre;

import java.sql.Time;
import java.time.LocalDate;
import java.sql.Date;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;
import java.util.regex.Pattern;

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

    public static int computeDiscount(int price) {
        return (int) (price - (price * .20));
    }

    public static String generateRandomID() {
        return UUID.randomUUID().toString().substring(0, 18);
    }

    public static Date getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return Date.valueOf(currentDate.format(formatter));
    }

    public static Time getCurrentTime(){
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return Time.valueOf(currentTime.format(formatter));
    }

    public static boolean isEmailValid(String email) {
        String regex = "\\b[A-Za-z0-9._%+-]+@gmail\\.com\\b";
        return Pattern.matches(regex, email);
    }

    public static String[] populateGenres(LinkedList<Genre> genres){
        String[] contents = new String[genres.size()];
        int index = 0;
        for (Genre genre : genres){
            contents[index++] = genre.getGenreName();
        }
        return contents;
    }







}
