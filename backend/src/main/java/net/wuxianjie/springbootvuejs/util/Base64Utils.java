package net.wuxianjie.springbootvuejs.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Base64;
import net.wuxianjie.springbootvuejs.exception.Base64Exception;
import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;

/**
 * Base64 编码/解码工具类
 *
 * <pre>
 * +----------------------------------------------------------------+
 * |                                                                |
 * |                  Table 1: The Base 64 Alphabet                 |
 * |                                                                |
 * | Value Encoding  Value Encoding  Value Encoding  Value Encoding |
 * |     0 A            17 R            34 i            51 z        |
 * |     1 B            18 S            35 j            52 0        |
 * |     2 C            19 T            36 k            53 1        |
 * |     3 D            20 U            37 l            54 2        |
 * |     4 E            21 V            38 m            55 3        |
 * |     5 F            22 W            39 n            56 4        |
 * |     6 G            23 X            40 o            57 5        |
 * |     7 H            24 Y            41 p            58 6        |
 * |     8 I            25 Z            42 q            59 7        |
 * |     9 J            26 a            43 r            60 8        |
 * |    10 K            27 b            44 s            61 9        |
 * |    11 L            28 c            45 t            62 +        |
 * |    12 M            29 d            46 u            63 /        |
 * |    13 N            30 e            47 ^                        |
 * |    14 O            31 f            48 w         (pad) =        |
 * |    15 P            32 g            49 x                        |
 * |    16 Q            33 h            50 y                        |
 * |                                                                |
 * +----------------------------------------------------------------+
 * </pre>
 *
 * <p>其中字母表的第 65 个字符（{@code =}）是填充字符，它用于将 Base64 编码的文本填充到整数大小</p>
 *
 * <p>Base64 的编码算法是接收字节（{@code 1 byte = 8 bit}）输入流。然后从左到右，
 * 将这些字节流拆分为 24-bit（3 个字节）组，每组被视为由 4 个 6-bit 组拼接而成。
 * 之后以每个 6-bit 组作为上述字母表的索引，分别映射到 64 个可打印字符，
 * 最终形成 Base64 编码后的字符数组</p>
 *
 * 若需要编码的数据的末尾少于 24 bit 时，则添加 0 bit（在右侧）以形成整数个 6-bit 组。
 * 然后，输出 1 个或 2 个 {@code =} 填充字符。这里有两种情况：
 * <ul>
 * <li>剩余 1 个字节（8 bit）：向该字节的末尾添加 4 个 0 bit（{@code 0000}）以形成 2 个 6-bit 组。
 * 然后以每个 6-bit 组作为索引得到可打印的字符数组。
 * 最后在这 2 个字符之后，再添加 2 个 {@code =}（{@code ==}）作为填充字符</li>
 * <li>剩余 2 个字节（16 bit）：向第二个字节的末尾添加 2 个 0 bit（{@code 00}）以形式 3 个 6-bit 组。
 * 然后以每个 6-bit 组作为索引得到可打印的字符数组。
 * 最后在这 3 个字符之后，再添加 1 个 {@code =}（{@code =}）作为填充字符</li>
 * </ul>
 *
 * @author 吴仙杰
 */
public class Base64Utils {

  private static final String DOUBLE_EQUALS = "==";

  private static final String SINGLE_EQUAL = "=";

  /**
   * 匹配 Basic Base64 的正则
   */
  private static final String BASIC_BASE64_REGEX = "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$";

  /**
   * 匹配 URL and Filename Safe Base64 变体的正则
   */
  private static final String URL_BASE64_REGEX = "^(?:[A-Za-z0-9-_]{4})*(?:[A-Za-z0-9-_]{2}==|[A-Za-z0-9-_]{3}=)?$";

  /**
   * 判断字符串是否符合 Basic Base64 编码
   *
   * @param src 需要判断的字符串
   * @return 当且仅当字符串符合 Basic Base64 编码时，才返回 {@code true}
   */
  public static boolean isBase64String(String src) {
    return src.matches(BASIC_BASE64_REGEX);
  }

  /**
   * 判断字符串是否符合 URL and Filename Safe Base64 变体编码
   *
   * @param src 需要判断的字符串
   * @return 当且仅当字符串符合 URL and Filename Safe Base64 变体时，才返回 {@code true}
   */
  public static boolean isURLBase64String(String src) {
    return src.matches(URL_BASE64_REGEX);
  }

  /**
   * 编码 Basic Base64 字符串
   *
   * <ul>
   * <li>使用 Base64 字母表（Table 1: The Base 64 Alphabet）进行编码和解码</li>
   * <li>编码器不添加任何的行分隔符</li>
   * <li>解码器会拒绝对包含除 Base64 字母表之外字符的数据进行解码</li>
   * </ul>
   *
   * @param src 需要 base64 编码的字节数组
   * @return 经 base64 编码后的字符串
   */
  public static String encodeToString(byte[] src) {
    return Base64.getEncoder().encodeToString(src);
  }

  /**
   * 解码 Basic Base64 字符串
   *
   * <ul>
   * <li>使用 Base64 字母表（Table 1: The Base 64 Alphabet）进行编码和解码</li>
   * <li>编码器不添加任何的行分隔符</li>
   * <li>解码器会拒绝对包含除 Base64 字母表之外字符的数据进行解码</li>
   * </ul>
   *
   * @param src 需要 base64 解码的字符串
   * @return 经 base64 解码后的字节数组
   */
  public static byte[] decode(String src) {
    return Base64.getDecoder().decode(src);
  }

  /**
   * 编码 MIME (Multipurpose Internet Mail Extensions) Base64 变体的字符串
   *
   * <ul>
   * <li>使用 Base64 字母表（Table 1: The Base 64 Alphabet）进行编码和解码</li>
   * <li>编码器输出每行不超过 76 个字符，
   * 且在除了最后一行之外的每行结尾处添加行分隔符（{@code \r\n}，即回车符后紧跟着换行符）。
   * 注意：最后一行不添加行分隔符</li>
   * <li>解码器在解码时会忽略所有行分隔符和 Base64 字母表之外字符</li>
   * </ul>
   *
   * @param src 需要 Base64 编码的字节数组
   * @return 经 MIME Base64 变体编码后的字符串
   */
  public static String mimeEncodeToString(byte[] src) {
    return Base64.getMimeEncoder().encodeToString(src);
  }

  /**
   * 解码 MIME (Multipurpose Internet Mail Extensions) Base64 变体的字符串
   *
   * <ul>
   * <li>使用 Base64 字母表（Table 1: The Base 64 Alphabet）进行编码和解码</li>
   * <li>编码器输出每行不超过 76 个字符，
   * 且在除了最后一行之外的每行结尾处添加行分隔符（{@code \r\n}，即回车符后紧跟着换行符）。
   * 注意：最后一行不添加行分隔符</li>
   * <li>解码器在解码时会忽略所有行分隔符和 Base64 字母表之外字符</li>
   * </ul>
   *
   * @param src 需要 Base64 解码的字符串
   * @return 经 MIME Base64 变体解码后的字符串
   */
  public static byte[] mimeDecode(String src) {
    return Base64.getMimeDecoder().decode(src);
  }

  /**
   * 编码 URL and Filename Safe Base64 变体的字符串
   *
   * <ul>
   * <li>使用 Base64 字母表（将 Table 1: The Base 64 Alphabet 中的 {@code +} 替换为 {@code -}，
   * {@code /} 替换为 {@code _}）进行编码和解码</li>
   * <li>编码器不添加任何的行分隔符</li>
   * <li>解码器会拒绝对包含除 Base64 字母表之外字符的数据进行解码</li>
   * </ul>
   * <p>
   * 该变体非常适用于作为 HTTP GET 请求参数（URL 编码器会将 {@code +} 变成 {@code %2B}，{@code /} 变成 {@code %2F}；
   * 反之，也需要 URL 解码器进行反运算）和作为文件名（Unix 和 Windows 文件名都不能包含 {@code /}）的场景。
   *
   * @param src 需要 Base64 编码的字节数组
   * @return 经 URL and Filename Safe Base64 变体编码后的字符串
   */
  public static String urlEncodeToString(byte[] src) {
    return Base64.getMimeEncoder().encodeToString(src);
  }

  /**
   * 解码 URL and Filename Safe Base64 变体的字符串
   *
   * <ul>
   * <li>使用 Base64 字母表（将 Table 1: The Base 64 Alphabet 中的 {@code +} 替换为 {@code -}，
   * {@code /} 替换为 {@code _}）进行编码和解码</li>
   * <li>编码器不添加任何的行分隔符</li>
   * <li>解码器会拒绝对包含除 Base64 字母表之外字符的数据进行解码</li>
   * </ul>
   * <p>
   * 该变体非常适用于作为 HTTP GET 请求参数（URL 编码器会将 {@code +} 变成 {@code %2B}，{@code /} 变成 {@code %2F}；
   * 反之，也需要 URL 解码器进行反运算）和作为文件名（Unix 和 Windows 文件名都不能包含 {@code /}）的场景。
   *
   * @param src 需要 Base64 解码的字符串
   * @return 经 URL and Filename Safe Base64 变体解码后的字符串
   */
  public static byte[] urlDecode(String src) {
    return Base64.getUrlDecoder().decode(src);
  }

  /**
   * 判断 Basic Base64 字符串所代表的数据是否超过了指定的数据大小
   *
   * @param src   需要进行数据大小比较的 Base64 字符串
   * @param maxKB 数据的最大值（单位：KB）
   * @return 当且仅当 Base64 字符串所代表的数据超过了指定大小时，才返回 {@code true}
   * @see <a href="https://stackoverflow.com/questions/34109053/what-file-size-is-data-if-its-450kb-base64-encoded">What file size is data if it's 450KB base64 encoded?</a>
   */
  public static boolean isExceed(String src, int maxKB) {
    // 包括填充字符的字节长度
    double byteLength = MathUtils.divide(src.length(), 4, 2) * 3;

    // 忽略填充字符，获取实际字节数
    if (src.endsWith(DOUBLE_EQUALS)) {
      byteLength -= 2;
    } else if (src.endsWith(SINGLE_EQUAL)) {
      byteLength -= 1;
    }

    return byteLength / 1000 > maxKB;
  }

  /**
   * 对字符串进行 Basic Base64 解码，并输出文件
   *
   * @param src  需要解码的文件的 Base64 字符串
   * @param file 解码后输出的目标文件
   * @throws Base64Exception 当解码失败时抛出
   */
  public static void decodeToFile(String src, File file) {
    byte[] data;
    if (isBase64String(src)) {
      data = decode(src);
    } else if (isURLBase64String(src)) {
      data = urlDecode(src);
    } else {
      data = mimeDecode(src);
    }

    try (OutputStream os = new FileOutputStream(file)) {
      os.write(data);
    } catch (IOException e) {
      throw new Base64Exception(String.format("将 base64 字符串【%s】解码为文件【%s】失败：%s", src, file.getAbsolutePath(), e.getMessage()), e, RestCodeEnum.ERROR_SERVER);
    }
  }

  /**
   * 将文件进行 Basic Base64 编码，并输出编码后的字符串
   *
   * @param file 需要进行 Basic Base64 编码的文件的
   * @return 对文件进行 Basic Base64 编码后得到的字符串
   * @throws Base64Exception 当编码失败时抛出
   */
  public static String encodeToString(File file) {
    try {
      return encodeToString(Files.readAllBytes(file.toPath()));
    } catch (IOException e) {
      throw new Base64Exception(String.format("将文件【%s】编码为 base64 字符串失败：%s", file.getAbsolutePath(), e.getMessage()), e, RestCodeEnum.ERROR_SERVER);
    }
  }
}
