package net.wuxianjie.springbootvuejs.util;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;
import net.wuxianjie.springbootvuejs.exception.IllegalArgumentException;

/**
 * 字符串工具类
 *
 * @author 吴仙杰
 */
public class StringUtils {

  /**
   * 生成去掉 {@code -} 后的 32 位 UUID
   *
   * @return 32 位 UUID
   */
  public static String generateUUId() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  /**
   * 生成完整的 36 位 UUID
   *
   * @return 36 位 UUID
   */
  public static String generateUUID2() {
    return UUID.randomUUID().toString();
  }

  /**
   * 自动截取指定最长长度的字符，并以英文省略号（{@code ...}）结尾。
   *
   * @param src       原字符串
   * @param maxLength 最大长度，至少要大于 3
   * @return 若原字符串长度大于指定的最大长度，则返回长度为 {@code maxLength - 1}，并以 {@code ...} 结尾的字符串，否则返回原字符串
   * @throws IllegalArgumentException 当指定的最大长度小于等于 3 时
   */
  public static String ellipsis(String src, int maxLength) {
    if (maxLength <= 3)
      throw new IllegalArgumentException("原字符串【%s】必须要包含至少 3 个以上的字符", RestCodeEnum.ERROR_SERVER);
    return src.length() > maxLength ? src.substring(0, maxLength - 3) + "..." : src;
  }

  /**
   * 将 Unicode 字符串转换为 UTF-8 字符串
   *
   * @param unicodeStr Unicode 字符串
   * @return UTF-8 字符串
   */
  public static String unicodeToUTF8(String unicodeStr) {
    Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
    Matcher matcher = pattern.matcher(unicodeStr);
    char ch;
    while (matcher.find()) {
      ch = (char) Integer.parseInt(matcher.group(2), 16);
      unicodeStr = unicodeStr.replace(matcher.group(1), ch + "");
    }
    return unicodeStr;
  }

  /**
   * 将 UTF-8 字符串转换为 Unicode 字符串
   *
   * @param str UTF-8 字符串
   * @return Unicode 字符串
   */
  public static String utf8ToUnicode(String str) {
    char[] originCharArray = str.toCharArray();

    StringBuilder builder = new StringBuilder();
    for (int i = 0, l = str.length(); i < l; ++i) {
      Character.UnicodeBlock ub = Character.UnicodeBlock.of(originCharArray[i]);
      if (ub == Character.UnicodeBlock.BASIC_LATIN) {
        // 英文及数字等
        builder.append(originCharArray[i]);
      } else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
        // 全角半角字符
        int j = (int) originCharArray[i] - 65248;
        builder.append((char) j);
      } else {
        // 汉字
        short chinese = (short) originCharArray[i];
        String hexStr = Integer.toHexString(chinese);
        String unicode = "\\u" + hexStr;
        builder.append(unicode.toLowerCase());
      }
    }
    return builder.toString();
  }
}
