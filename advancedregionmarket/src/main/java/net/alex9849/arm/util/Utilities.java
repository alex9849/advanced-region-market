package net.alex9849.arm.util;

import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.Messages;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class Utilities {

    public static long stringToTime(String stringtime) throws IllegalArgumentException {
        long time = 0;
        if(stringtime.matches("[\\d]+d")){
            time = Long.parseLong(stringtime.split("d")[0]);
            time = time * 1000*60*60*24;
        } else if(stringtime.matches("[\\d]+h")){
            time = Long.parseLong(stringtime.split("h")[0]);
            time = time * 1000*60*60;
        } else if(stringtime.matches("[\\d]+m")){
            time = Long.parseLong(stringtime.split("m")[0]);
            time = time * 1000*60;
        } else if(stringtime.matches("[\\d]+s")){
            time = Long.parseLong(stringtime.split("s")[0]);
            time = time * 1000;
        } else if (stringtime.matches("[\\d]+")) {
            time = Long.parseLong(stringtime);
        } else {
            throw new IllegalArgumentException();
        }
        return time;
    }

    public static String getCountdown(Boolean shortedCountdown, long endTimeInMs, boolean showZeroIfDateInThePast, String ifDateInPastReplacement){
        GregorianCalendar actualtime = new GregorianCalendar();
        GregorianCalendar payedTill = new GregorianCalendar();
        payedTill.setTimeInMillis(endTimeInMs);

        long remainingMilliSeconds = payedTill.getTimeInMillis() - actualtime.getTimeInMillis();

        String sec;
        String min;
        String hour;
        String days;
        if(shortedCountdown) {
            sec = " " + Messages.TIME_SECONDS_SHORT;
            min = " " + Messages.TIME_MINUTES_SHORT;
            hour = " " + Messages.TIME_HOURS_SHORT;
            days = " " + Messages.TIME_DAYS_SHORT;
        } else {
            sec = Messages.TIME_SECONDS;
            min = Messages.TIME_MINUTES;
            hour = Messages.TIME_HOURS;
            days = Messages.TIME_DAYS;
        }

        if(remainingMilliSeconds < 0 && !showZeroIfDateInThePast){
            return ifDateInPastReplacement;
        }

        long remainingDays = TimeUnit.DAYS.convert(remainingMilliSeconds, TimeUnit.MILLISECONDS);
        remainingMilliSeconds = remainingMilliSeconds - (remainingDays * 1000 * 60 * 60 *24);

        long remainingHours = TimeUnit.HOURS.convert(remainingMilliSeconds, TimeUnit.MILLISECONDS);
        remainingMilliSeconds = remainingMilliSeconds - (remainingHours * 1000 * 60 * 60);

        long remainingMinutes = TimeUnit.MINUTES.convert(remainingMilliSeconds, TimeUnit.MILLISECONDS);
        remainingMilliSeconds = remainingMilliSeconds - (remainingMinutes * 1000 * 60);

        long remainingSeconds = TimeUnit.SECONDS.convert(remainingMilliSeconds, TimeUnit.MILLISECONDS);


        String timetoString = "";
        if(remainingDays != 0) {
            timetoString = timetoString + remainingDays + days;
            if(shortedCountdown){
                return timetoString;
            }
        }
        if(remainingHours != 0) {
            timetoString = timetoString + remainingHours + hour;
            if(shortedCountdown){
                return timetoString;
            }
        }
        if(remainingMinutes != 0) {
            timetoString = timetoString + remainingMinutes + min;
            if(shortedCountdown){
                return timetoString;
            }
        }
        if(remainingSeconds != 0) {
            timetoString = timetoString + remainingSeconds + sec;
            if(shortedCountdown){
                return timetoString;
            }
        }
        if(remainingSeconds == 0 && remainingMinutes == 0 && remainingHours == 0 && remainingDays == 0){
            timetoString = "0" + sec;
        }

        return timetoString;
    }

    public static String getDate(long dateInMs, boolean showDateIfDateInThePast, String ifDateInPastReplacement) {
        GregorianCalendar actualtime = new GregorianCalendar();
        GregorianCalendar payedTill = new GregorianCalendar();
        payedTill.setTimeInMillis(dateInMs);

        if ((payedTill.getTimeInMillis() - actualtime.getTimeInMillis()) < 0 && !showDateIfDateInThePast) {
            return ifDateInPastReplacement;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(ArmSettings.getDateTimeformat());

        return sdf.format(payedTill.getTime());
    }

    public static String timeInMsToString(long endTime, boolean showTimeIfDateInThePast, String ifDateInPastReplacement) {
        String timetoString = ArmSettings.getRemainingTimeTimeformat();
        timetoString = timetoString.replace("%countdown%", Utilities.getCountdown(ArmSettings.isUseShortCountdown(), endTime, showTimeIfDateInThePast, ifDateInPastReplacement));
        timetoString = timetoString.replace("%date%", Utilities.getDate(endTime, showTimeIfDateInThePast, ifDateInPastReplacement));
        return timetoString;
    }

}
