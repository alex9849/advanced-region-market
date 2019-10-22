package net.alex9849.arm.util;

import net.alex9849.arm.Messages;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class TimeUtil {
    /**
     * Generates a String that shows a countdown that will end at the given endTime
     * @param endTimeInMs The enddate in ms
     * @param writeOut if true the method will write out the timeunits.
     *                 For example 'Minutes' instead of 'm'
     * @param returnOnlyHighestUnit if true the value will be cut
     *                              after the first value that is not 0.
     *                              This would return a string like '1d'
     *                              instead of '1d10h5m6s'
     * @param showReplacementIfDateInThePast if the countdown is expired the method will return
     *                                       the string given in ifDateInPastReplacement
     * @param ifDateInPastReplacement The value that will be returned if endTimeInMs is in the past
     * @return a string with the countdown
     */
    public static String getCountdown(long endTimeInMs, boolean writeOut, Boolean returnOnlyHighestUnit, boolean showReplacementIfDateInThePast, String ifDateInPastReplacement) {
        GregorianCalendar actualtime = new GregorianCalendar();
        GregorianCalendar payedTill = new GregorianCalendar();
        payedTill.setTimeInMillis(endTimeInMs);

        long remainingMilliSeconds = payedTill.getTimeInMillis() - actualtime.getTimeInMillis();

        if (remainingMilliSeconds < 0 && showReplacementIfDateInThePast) {
            return ifDateInPastReplacement;
        }

        return timeInMsToString(remainingMilliSeconds, writeOut, returnOnlyHighestUnit);
    }

    /**
     * Converts a timevalue to a string. For example:
     * timeInMs = 62000 would be 1 Minute and 2 Seconds
     * @param timeInMs the timevalue in ms should be positive.
     *                 If not the method will return '0 Seconds'
     * @param writeOut if true the method will write out the timeunits.
     *                 For example 'Minutes' instead of 'm'
     * @param returnOnlyHighestUnit if true the value will be cut
     *                              after the first value that is not 0.
     *                              This would return a string like '1d'
     *                              instead of '1d10h5m6s'
     * @return a String with given date or '0' if timeInMs is negative
     */
    public static String timeInMsToString(long timeInMs, boolean writeOut, boolean returnOnlyHighestUnit) {
        long time = timeInMs;

        if(timeInMs < 0) {
            return 0 + getTimeUnit(0, writeOut, Messages.TIME_SECONDS_SHORT, Messages.TIME_SECONDS_SINGULAR, Messages.TIME_SECONDS_PLURAL);
        }

        long remainingDays = TimeUnit.DAYS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingDays * 1000 * 60 * 60 * 24);

        long remainingHours = TimeUnit.HOURS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingHours * 1000 * 60 * 60);

        long remainingMinutes = TimeUnit.MINUTES.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingMinutes * 1000 * 60);

        long remainingSeconds = TimeUnit.SECONDS.convert(time, TimeUnit.MILLISECONDS);

        StringBuilder timetoString = new StringBuilder(30);
        if (remainingDays != 0) {
            timetoString.append(remainingDays);
            timetoString.append(getTimeUnit(remainingDays, writeOut, Messages.TIME_DAYS_SHORT, Messages.TIME_DAYS_SINGULAR, Messages.TIME_DAYS_PLURAL));
            if(returnOnlyHighestUnit) return timetoString.toString();
        }
        if (remainingHours != 0) {
            if(remainingDays != 0) {
                if(writeOut) {
                    if(remainingMinutes == 0) {
                        timetoString.append(Messages.TIME_UNIT_SPLITTER);
                    } else {
                        timetoString.append(", ");
                    }
                } else {
                    timetoString.append(Messages.TIME_UNIT_SPLITTER_SHORT);
                }
            }
            timetoString.append(remainingHours);
            timetoString.append(getTimeUnit(remainingHours, writeOut, Messages.TIME_HOURS_SHORT, Messages.TIME_HOURS_SINGULAR, Messages.TIME_HOURS_PLURAL));
            if(returnOnlyHighestUnit) return timetoString.toString();
        }
        if (remainingMinutes != 0) {
            if(remainingDays != 0 || remainingHours != 0) {
                if(writeOut) {
                    if(remainingSeconds == 0) {
                        timetoString.append(Messages.TIME_UNIT_SPLITTER);
                    } else {
                        timetoString.append(", ");
                    }
                } else {
                    timetoString.append(Messages.TIME_UNIT_SPLITTER_SHORT);
                }
            }
            timetoString.append(remainingMinutes);
            timetoString.append(getTimeUnit(remainingMinutes, writeOut, Messages.TIME_MINUTES_SHORT, Messages.TIME_MINUTES_SINGULAR, Messages.TIME_MINUTES_PLURAL));
            if(returnOnlyHighestUnit) return timetoString.toString();
        }
        if (remainingSeconds != 0 || (remainingSeconds == 0 && remainingMinutes == 0 && remainingHours == 0 && remainingDays == 0)) {
            if(remainingDays != 0 || remainingHours != 0 ||remainingMinutes != 0) {
                if(writeOut) {
                    timetoString.append(Messages.TIME_UNIT_SPLITTER);
                } else {
                    timetoString.append(Messages.TIME_UNIT_SPLITTER_SHORT);
                }
            }
            timetoString.append(remainingSeconds);
            timetoString.append(getTimeUnit(remainingSeconds, writeOut, Messages.TIME_SECONDS_SHORT, Messages.TIME_SECONDS_SINGULAR, Messages.TIME_SECONDS_PLURAL));
            if(returnOnlyHighestUnit) return timetoString.toString();
        }

        return timetoString.toString();
    }

    private static String getTimeUnit(long amount, boolean writeOut, String shortUnit, String writtenOutUnit, String writtenOutUnitPlural) {
        if(!writeOut) {
            return shortUnit;
        }
        if(amount > 1) {
            return " " + writtenOutUnitPlural;
        }
        return " " + writtenOutUnit;
    }

    public static String getDate(long dateInMs, boolean showReplacementIfDateInThePast, String ifDateInPastReplacement, String datePattern) {
        GregorianCalendar actualtime = new GregorianCalendar();
        GregorianCalendar payedTill = new GregorianCalendar();
        payedTill.setTimeInMillis(dateInMs);

        if ((payedTill.getTimeInMillis() - actualtime.getTimeInMillis()) < 0 && showReplacementIfDateInThePast) {
            return ifDateInPastReplacement;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);

        return sdf.format(payedTill.getTime());
    }
}
