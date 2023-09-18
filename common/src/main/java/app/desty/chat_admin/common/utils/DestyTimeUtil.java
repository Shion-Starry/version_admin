package app.desty.chat_admin.common.utils;

import java.util.Calendar;
import java.util.Date;

import kotlin.Pair;

public class DestyTimeUtil {

    /**
     * 获取对应年月日的开始和结束时间
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 开始和结束时间戳
     */
    public static Pair<Long, Long> getDayStartAndEndMillis(int year, int month, int day) {
        return new Pair<>(getStartCalendar(year, month, day).getTimeInMillis(),
                          getEndCalendar(year, month, day).getTimeInMillis());
    }

    /**
     * 获取对应年月日的开始时间
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 开始时间戳
     */
    public static Calendar getStartCalendar(int year, int month, int day) {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, month - 1);
        start.set(Calendar.DAY_OF_MONTH, day);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return start;
    }

    /**
     * 获取对应年月日的结束时间
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 结束时间戳
     */
    public static Calendar getEndCalendar(int year, int month, int day) {
        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, year);
        end.set(Calendar.MONTH, month - 1);
        end.set(Calendar.DAY_OF_MONTH, day);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);
        end.add(Calendar.DAY_OF_YEAR, 1);
        end.add(Calendar.MILLISECOND, -1);
        return end;
    }

    /**
     * 获取对应年月日的开始和结束时间
     *
     * @param date 时间
     * @return 开始和结束时间戳
     */
    public static Pair<Long, Long> getDayStartAndEndMillis(Date date) {
        return new Pair<>(getStartCalendar(date).getTimeInMillis(),
                          getEndCalendar(date).getTimeInMillis());
    }

    /**
     * 获取对应年月日的开始时间
     *
     * @param date 时间
     * @return 开始时间戳
     */
    public static Calendar getStartCalendar(Date date) {
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return start;
    }

    /**
     * 获取对应年月日的结束时间
     *
     * @param date 时间
     * @return 结束时间戳
     */
    public static Calendar getEndCalendar(Date date) {
        Calendar end = Calendar.getInstance();
        end.setTime(date);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);
        end.add(Calendar.DAY_OF_YEAR, 1);
        end.add(Calendar.MILLISECOND, -1);
        return end;
    }

    /**
     * 获取时间戳对应天的开始和结束时间戳
     *
     * @param milliseconds 目标天的任意时间戳
     * @return 开始和结束时间戳
     */
    public static Pair<Long, Long> getDayStartAndEndMillis(long milliseconds) {
        return new Pair<>(getStartCalendar(milliseconds).getTimeInMillis(),
                          getEndCalendar(milliseconds).getTimeInMillis());
    }

    /**
     * 获取时间戳对应天的开始时间戳
     *
     * @param milliseconds 目标天的任意时间戳
     * @return 开始时间戳
     */
    public static Calendar getStartCalendar(long milliseconds) {
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(milliseconds);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return start;
    }

    /**
     * 获取时间戳对应天的结束时间戳
     *
     * @param milliseconds 目标天的任意时间戳
     * @return 结束时间戳
     */
    public static Calendar getEndCalendar(long milliseconds) {
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(milliseconds);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);
        end.add(Calendar.DAY_OF_YEAR, 1);
        end.add(Calendar.MILLISECOND, -1);
        return end;
    }


}
