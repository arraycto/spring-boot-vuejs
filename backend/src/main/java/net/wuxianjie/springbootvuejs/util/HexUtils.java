package net.wuxianjie.springbootvuejs.util;

/**
 * 十六进制工具类
 *
 * @author 吴仙杰
 */
public class HexUtils {

  /**
   * 把字节数组转换为十六进制字符串字符串
   *
   * @param bytes             字节数组
   * @param isOutputUpperCase 若为 {@code true}，则最终输出十六进制大写字符串；否则输出十六进制小写字符串
   * @return 十六进制字符串
   */
  public static String toHex(byte[] bytes, boolean isOutputUpperCase) {
    StringBuilder builder = new StringBuilder();
    for (byte b : bytes) {
      String hex = Integer.toHexString(b & 0xFF);
      if (hex.length() == 1) {
        builder.append("0");
      }
      builder.append(isOutputUpperCase ? hex.toUpperCase() : hex);
    }
    return builder.toString();
  }

  /**
   * 把十六进制字符串转换为字节数组
   *
   * @param hex 十六进制字符串
   * @return byte 数组
   */
  public static byte[] parseHex(String hex) {
    int len = hex.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
    }
    return data;
  }
}
