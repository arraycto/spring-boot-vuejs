package net.wuxianjie.springbootvuejs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;
import net.wuxianjie.springbootvuejs.exception.DateException;

/**
 * 日期工具类
 *
 * <p>尽量使用 JDK8 {@code java.time} 包中的日期时间对象</p>
 *
 * @author 吴仙杰
 */
public class DateUtils {

  public static final String YYYY_MM_DD = "yyyy-MM-dd";

  public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

  /**
   * 从日期字符串中解析出合法的日期对象
   *
   * <p><b>注意</b>：该方法会忽略日期字符的时间部分，得到的日期对象只有默认的时间 {@code 00:00:00}。
   * 若要也要解析字符串的时间部分，则请使用 {@link #parseLegalDateTime(String)} 方法</p>
   *
   * @param dateStr 需要解析的日期字符串
   * @return 当且仅当该日期字符串中的日期部分为合法日期时，才返回 {@link Date} 对象，否则返回 {@code null}
   */
  public static Date parseLegalDate(String dateStr) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD);
    dateFormat.setLenient(false);
    try {
      return dateFormat.parse(dateStr);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 从日期字符串中解析出合法的日期对象
   *
   * <p>若只需要解析字符串的日期部分，则请使用 {@link #parseLegalDate(String)} 方法</p>
   *
   * @param dateStr 需要解析的日期字符串
   * @return 当且仅当该日期字符串为合法日期时，才返回 {@link Date} 对象，否则返回 {@code null}
   */
  public static Date parseLegalDateTime(String dateStr) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
    dateFormat.setLenient(false);
    try {
      return dateFormat.parse(dateStr.trim());
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 根据指定的日期格式，将日期字符串解析为日期对象
   *
   * @param dateStr 需要解析的日期字符串
   * @param pattern 日期字符串的日期格式，例如：{@code yyyy-MM-dd HH:mm:ss}）
   * @return 当且仅当该日期字符串可推断为日期时，才返回 {@link Date} 对象，否则抛出 {@link ParseException} 异常
   * @throws DateException 当日期字符串不可推断为日期时
   */
  public static Date parse(String dateStr, String pattern) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
    try {
      return dateFormat.parse(dateStr);
    } catch (ParseException e) {
      throw new DateException(String.format("日期字符串【%s】解析失败：%s", dateStr, e.getMessage()), RestCodeEnum.ERROR_SERVER);
    }
  }

  /**
   * 根据指定的日期格式，将日期对象格式化为字符串
   *
   * @param date    日期对象
   * @param pattern 输出字符串的日期格式，例如：{@code yyyy-MM-dd HH:mm:ss}
   * @return 日期字符串
   */
  public static String format(Date date, String pattern) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
    return dateFormat.format(date);
  }

  /**
   * 获取系统当前时间字符串
   *
   * @param pattern 自定义输出的日期格式，例如：{@code yyyy-MM-dd HH:mm:ss}
   * @return 日期字符串
   */
  public static String getCurrentDateString(String pattern) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
    return dateFormat.format(System.currentTimeMillis());
  }

  /**
   * 重新格式化日期字符串
   *
   * @param dateStr    原日期字符串
   * @param oldPattern 原日期字符串的格式，例如：{@code yyyy-MM-dd HH:mm:ss}
   * @param newPattern 新日期字符串的格式，例如：{@code yyyy-MM-dd HH:mm:ss}
   * @return 当且仅当该日期字符串可推断为日期时，才返回新日期格式的字符串，否则抛出 {@link ParseException} 异常
   * @throws DateException 当日期字符串不可推断为日期时
   */
  public static String reformat(String dateStr, String oldPattern, String newPattern) {
    SimpleDateFormat oldFormat = new SimpleDateFormat(oldPattern);
    SimpleDateFormat newFormat = new SimpleDateFormat(newPattern);
    try {
      return newFormat.format(oldFormat.parse(dateStr));
    } catch (ParseException e) {
      throw new DateException(String.format("日期字符串【%s】解析失败：%s", dateStr, e.getMessage()), RestCodeEnum.ERROR_SERVER);
    }
  }

  /**
   * 获取当前日期和时间
   *
   * @return 当前日期和时间
   */
  public static Date getCurrentDate() {
    return Date.from(Instant.now());
  }

  /**
   * 获取当前多少分钟后的日期和时间
   *
   * @param minutes 多少分钟后，可正可负
   * @return 当前多少分钟后的日期和时间
   */
  public static Date getAfterMinutesDate(int minutes) {
    Instant instant = Instant.now();
    Duration duration = Duration.ofMinutes(minutes);
    Instant instantMinutesLater = instant.plus(duration);
    return Date.from(instantMinutesLater);
  }

  /**
   * 获取指定月份的第一天的最小日期时间
   *
   * @param date 日期对象
   * @return 指定月份的第一天的最小日期时间
   */
  public static Date getFirstDateLeastTimeOfMonth(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
    cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
    cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
    cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
    cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
    return cal.getTime();
  }

  /**
   * 获取指定月份的最后一天的最大日期时间
   *
   * @param date 日期对象
   * @return 指定月份的最后一天的最大日期时间
   */
  public static Date getLastDateMaxTimeOfMonth(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
    cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
    cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
    cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
    return cal.getTime();
  }

  /**
   * 获取两个日期的差值
   *
   * @param minuend    作为被减数的日期
   * @param subtrahend 作为减数的日期
   * @param timeUnit   日期间隔换算单位
   * @return {@code minuend - subtrahend} 的差值
   */
  public static long subtract(Date minuend, Date subtrahend, TimeUnit timeUnit) {
    long value = minuend.getTime() - subtrahend.getTime();
    return timeUnit.convert(value, TimeUnit.MILLISECONDS);
  }

  /**
   * 获取两个日期的月份差
   *
   * @param minuend    作为被减数的日期
   * @param subtrahend 作为减数的日期
   * @return 两个日期的月份差
   */
  public static int subtractMonths(Date minuend, Date subtrahend) {
    Calendar startCalendar = new GregorianCalendar();
    startCalendar.setTime(minuend);
    Calendar endCalendar = new GregorianCalendar();
    endCalendar.setTime(subtrahend);
    int years = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
    return years * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
  }

  /**
   * 根据日期跨度获取两个日期之间的所有间隔日期
   *
   * @param startDate    开始日期
   * @param endDate      结束日期
   * @param calendarType 日期跨度类型，例如：{@code Calendar.DAY_OF_MONTH}
   * @param step         步长，至少 {@code >=1}
   * @return 两个日期之间的所有间隔日期
   */
  public static List<Date> getIntervals(Date startDate, Date endDate, int calendarType, int step) {
    List<Date> dates = new ArrayList<>();
    if (step < 1) {
      return dates;
    }

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(startDate);

    while (calendar.getTime().compareTo(endDate) <= 0) {
      dates.add(calendar.getTime());
      calendar.add(calendarType, step);
    }

    return dates;
  }

  /**
   * 获取指定日期的开始时间
   *
   * @param date 日期对象
   * @return 开始时间
   */
  public static Date getStartDateTime(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  /**
   * 获取指定日期的结束时间
   *
   * @param date 日期对象
   * @return 结束时间
   */
  public static Date getEndDateTime(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 999);
    return cal.getTime();
  }
}
