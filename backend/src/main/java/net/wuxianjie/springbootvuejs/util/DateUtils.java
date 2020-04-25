package net.wuxianjie.springbootvuejs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
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
 * <p>尽量使用 JDK 8 {@code java.time} 包中的日期时间对象</p>
 *
 * @author 吴仙杰
 */
public class DateUtils {

  public static final String YYYY_MM_DD = "yyyy-MM-dd";

  public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

  /**
   * {@link Date} 转换为 {@link LocalDateTime}
   *
   * @param date Java 8 前日期对象
   * @return {@code LocalDateTime} 实例对象
   */
  public static LocalDateTime dateToLocalDateTime(Date date) {

    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

  /**
   * {@link Calendar} 转换为 {@link LocalDateTime}
   *
   * @param cal Java 8 前日期对象
   * @return {@code LocalDateTime} 实例对象
   */
  public static LocalDateTime calendarToLocalDateTime(Calendar cal) {

    return LocalDateTime.ofInstant(cal.toInstant(), ZoneId.systemDefault());
  }

  /**
   * 从 {@code 1970-01-01T00:00:00Z} 的纪元开始，以毫秒为单位转换为 {@link LocalDateTime}
   *
   * @param epochMilli Java 8 前日期对象
   * @return {@code LocalDateTime} 实例对象
   */
  public static LocalDateTime dateToLocalDateTime(long epochMilli) {

    return Instant.ofEpochMilli(epochMilli).atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  /**
   * {@link LocalDateTime} 转换为 {@link Date}
   *
   * @param ldt Java 8 日期时间对象
   * @return {@code Date} 实例对象
   */
  public static Date localDateTimeToDate(LocalDateTime ldt) {

    return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
  }

  /**
   * 获取指定日期的当天开始日期
   *
   * @param date 日期对象
   * @return 开始时间
   */
  public static Date getStartOfDay(Date date) {

    LocalDateTime ldt = dateToLocalDateTime(date);

    LocalDate localDate = ldt.toLocalDate();

    LocalDateTime startTime = LocalDateTime.of(localDate, LocalTime.MIN);

    return localDateTimeToDate(startTime);
  }

  /**
   * 获取指定日期的当天结束日期
   *
   * @param date 日期对象
   * @return 结束时间
   */
  public static Date getEndOfDay(Date date) {

    LocalDateTime ldt = dateToLocalDateTime(date);

    LocalDate localDate = ldt.toLocalDate();

    LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);

    return localDateTimeToDate(endTime);
  }

  /**
   * 获取指定日期的当月开始日期
   *
   * @param date 日期对象
   * @return 开始时间
   */
  public static Date getStartOfMonth(Date date) {

    LocalDateTime ldt = dateToLocalDateTime(date);

    LocalDate localDate = ldt.toLocalDate();
    localDate = localDate.with(ChronoField.DAY_OF_MONTH, 1);

    LocalDateTime startTime = LocalDateTime.of(localDate, LocalTime.MIN);

    return localDateTimeToDate(startTime);
  }

  /**
   * 获取指定日期的当月结束日期
   *
   * @param date 日期对象
   * @return 结束时间
   */
  public static Date getEndOfMonth(Date date) {

    LocalDateTime ldt = dateToLocalDateTime(date);

    LocalDate localDate = ldt.toLocalDate();
    localDate = localDate.with(ChronoField.DAY_OF_MONTH, localDate.lengthOfMonth());

    LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);

    return localDateTimeToDate(endTime);
  }

  /**
   * 获取指定日期的当年开始日期
   *
   * @param date 日期对象
   * @return 开始时间
   */
  public static Date getStartOfYear(Date date) {

    LocalDateTime ldt = dateToLocalDateTime(date);

    LocalDate localDate = ldt.toLocalDate();
    localDate = localDate.with(ChronoField.MONTH_OF_YEAR, 1);
    localDate = localDate.with(ChronoField.DAY_OF_MONTH, 1);

    LocalDateTime startTime = LocalDateTime.of(localDate, LocalTime.MIN);

    return localDateTimeToDate(startTime);
  }

  /**
   * 获取指定日期的当年结束日期
   *
   * @param date 日期对象
   * @return 结束时间
   */
  public static Date getEndOfYear(Date date) {

    LocalDateTime ldt = dateToLocalDateTime(date);

    LocalDate localDate = ldt.toLocalDate();
    localDate = localDate.with(ChronoField.MONTH_OF_YEAR, 12);
    localDate = localDate.with(ChronoField.DAY_OF_MONTH, 31);

    LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);

    return localDateTimeToDate(endTime);
  }

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
   * 获取当前多少秒后的日期和时间
   *
   * @param seconds 多少秒后，可正可负
   * @return 当前多少秒后的日期和时间
   */
  public static Date getAfterSecondsDate(int seconds) {
    Instant instant = Instant.now();
    Duration duration = Duration.ofSeconds(seconds);
    Instant instantMinutesLater = instant.plus(duration);
    return Date.from(instantMinutesLater);
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
   * 获取当前多少天后的日期和时间
   *
   * @param days 多少天后，可正可负
   * @return 当前多少天后的日期和时间
   */
  public static Date getAfterDaysDate(int days) {
    Instant instant = Instant.now();
    Duration duration = Duration.ofDays(days);
    Instant instantMinutesLater = instant.plus(duration);
    return Date.from(instantMinutesLater);
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
}
