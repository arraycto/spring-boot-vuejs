package net.wuxianjie.springbootvuejs.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Base64;
import net.wuxianjie.springbootvuejs.exception.Base64Exception;
import net.wuxianjie.springbootvuejs.rest.RestCodeEnum;

/**
 * Base64 相关操作工具类
 *
 * @author 吴仙杰
 */
public class Base64Utils {

  public static final String BASE64_REGEXP = "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$";

  /**
   * 根据 base64 文件字符串对比文件大小
   *
   * @param base64String base64 字符串
   * @param maximumKb    文件最大的大小（单位：KB）
   * @return 若超过了最大文件限制，则返回 {@code true}；否则返回 {@code false}
   */
  public static boolean isExceed(String base64String, int maximumKb) {
    final String doubleEquals = "==";
    final String singleEqual = "=";

    // 包括填充字符的 bytes
    double resultBytes = MathUtils.divide(base64String.length(), 4, 2) * 3;
    // 忽略填充字符，获取实际字节数
    if (base64String.endsWith(doubleEquals)) {
      resultBytes -= 2;
    } else if (base64String.endsWith(singleEqual)) {
      resultBytes -= 1;
    }

    return resultBytes / 1000 > maximumKb;
  }

  /**
   * 将 base64 字符串解码为文件
   *
   * @param base64String base64 字符串
   * @param file         目标文件对象
   * @throws Base64Exception 当解码失败时抛出
   */
  public static void decode(String base64String, File file) {
    byte[] data = Base64.getDecoder().decode(base64String);
    try (OutputStream out = new FileOutputStream(file)) {
      out.write(data);
    } catch (IOException e) {
      throw new Base64Exception(String.format("将 base64 字符串【%s】解码为文件【%s】失败：%s", base64String, file.getAbsolutePath(), e.getMessage()), e, RestCodeEnum.ERROR);
    }
  }

  /**
   * 将文件编码为 base64 字符串
   *
   * @param file 需要编码为 base64 字符串的文件
   * @return base64 字符串
   * @throws Base64Exception 当编码失败时抛出
   */
  public static String encode(File file) {
    byte[] bytes;
    try {
      bytes = Base64.getEncoder().encode(Files.readAllBytes(file.toPath()));
    } catch (IOException e) {
      throw new Base64Exception(String.format("将文件【%s】编码为 base64 字符串失败：%s", file.getAbsolutePath(), e.getMessage()), e, RestCodeEnum.ERROR);
    }
    return new String(bytes);
  }
}
