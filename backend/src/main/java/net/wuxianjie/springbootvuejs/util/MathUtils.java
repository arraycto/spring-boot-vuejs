package net.wuxianjie.springbootvuejs.util;

import java.math.BigDecimal;

/**
 * 有关数学运算的工具类
 *
 * @author 吴仙杰
 */
public class MathUtils {

  /**
   * 加法
   *
   * @param n1 加数
   * @param n2 加数
   * @return {@code n1 + n2} 的结果
   */
  public static double add(double n1, double n2) {
    return BigDecimal.valueOf(n1).add(BigDecimal.valueOf(n2)).doubleValue();
  }

  /**
   * 减法
   *
   * @param minuend    被减数
   * @param subtrahend 减数
   * @return {@code minuend - subtrahend} 的结果
   */
  public static double subtract(double minuend, double subtrahend) {
    return BigDecimal.valueOf(minuend).subtract(BigDecimal.valueOf(subtrahend)).doubleValue();
  }

  /**
   * 乘法
   *
   * @param n1 乘数
   * @param n2 乘数
   * @return {@code n1 x n2} 的结果
   */
  public static double multiply(double n1, double n2) {
    return BigDecimal.valueOf(n1).multiply(BigDecimal.valueOf(n2)).doubleValue();
  }

  /**
   * 除法
   *
   * @param dividend 被除数
   * @param divisor  除数
   * @param scale    精确小数点后几位
   * @return {@code dividend / divisor} 的结果
   */
  public static double divide(double dividend, double divisor, int scale) {
    return BigDecimal.valueOf(dividend)
        .divide(BigDecimal.valueOf(divisor), scale, BigDecimal.ROUND_HALF_UP)
        .doubleValue();
  }

  /**
   * 四舍五入并保留小数点后指定的位数
   *
   * @param decimal 小数
   * @param scale   保留小数点后的几位数字
   * @return 处理后的小数
   */
  public static double round(double decimal, int scale) {
    return Double.parseDouble(String.format("%." + scale + "f", decimal));
  }
}
