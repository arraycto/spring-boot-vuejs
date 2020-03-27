package net.wuxianjie.springbootvuejs.util;

import java.security.SecureRandom;
import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;
import net.wuxianjie.springbootvuejs.exception.ServerException;

/**
 * 唯一标识符生成器
 *
 * @author 吴仙杰
 */
public class IdentifierGenerator {

  private static final int NUMBER_SYMBOLS = 1;

  private static final int NUMBER_LETTER_SYMBOLS = 2;


  /**
   * 整数库
   */
  private char[] numberSymbols;

  /**
   * 符号库，可自定义扩充
   *
   * <p>当前为 {@code 数字 + 小写英文字符 + 大写英文字符}</p>
   */
  private char[] numberLetterSymbols;

  /**
   * 一个经过加密的强随机数生成器
   *
   * <p>因为创建 {@link SecureRandom} 对象的代价昂贵，所以保存此对象以复用</p>
   */
  private final SecureRandom random = new SecureRandom();

  /**
   * 将构造方法私有化，使外部无法直接实例化
   */
  private IdentifierGenerator() {
    // 构造数字库
    char minNumberChar = '0';
    char maxNumberChar = '9';
    StringBuilder numberBuilder = getBuilder(minNumberChar, maxNumberChar);
    numberSymbols = numberBuilder.toString().toCharArray();

    // 构造小写英文字符库
    char minLowercaseChar = 'a';
    char maxLowercaseChar = 'z';
    StringBuilder lowercaseBuilder = getBuilder(minLowercaseChar, maxLowercaseChar);

    // 构造大写英文字符库
    char minUppercaseChar = 'A';
    char maxUppercaseChar = 'Z';
    StringBuilder uppercaseBuilder = getBuilder(minUppercaseChar, maxUppercaseChar);

    // 拼接大小写英文字符库
    numberLetterSymbols = numberBuilder.append(lowercaseBuilder).append(uppercaseBuilder).toString().toCharArray();
  }

  /**
   * 单例模式：静态内部类法，懒加载，并且能保证线程安全
   *
   * <p>将单例（singleton）放到一个静态内部类中，可避免类加载时就创建实例，从而实现懒加载（在需要使用时才创建）</p>
   *
   * <p>由于静态内部类只会被加载一次，所以这种写法也是线程安全的</p>
   */
  private static class SingletonHolder {

    private static final IdentifierGenerator INSTANCE;

    static {
      // 利用静态代码块来完成初始化
      INSTANCE = new IdentifierGenerator();
    }
  }

  /**
   * 获取 {@link IdentifierGenerator} 的单例对象
   *
   * @return {@link IdentifierGenerator} 对象
   */
  public static IdentifierGenerator getInstance() {
    return IdentifierGenerator.SingletonHolder.INSTANCE;
  }

  /**
   * 生成自定义长度的数字标识符字符串
   *
   * @param length 字符长度
   * @return 数字标识符字符串
   * @throws IllegalArgumentException 如果 {@code length} 小于 1
   */
  public String generateNumber(int length) {
    return generateFactory(length, NUMBER_SYMBOLS);
  }

  /**
   * 生成自定义长度的数字、大小写字母标识符字符串
   *
   * @param length 字符长度
   * @return 数字、大小写字母标识符字符串
   * @throws ServerException 当 {@code length} 小于 1 时
   */
  public String generateNumberLetter(int length) {
    return generateFactory(length, NUMBER_LETTER_SYMBOLS);
  }

  /**
   * 获取字符库的可变字符序列构造器
   *
   * @param min 最小字符（包含）
   * @param max 最大字符（包含）
   * @return 字符库的可变字符序列构造器
   */
  private StringBuilder getBuilder(char min, char max) {
    StringBuilder builder = new StringBuilder();
    for (char ch = min; ch <= max; ch++) {
      builder.append(ch);
    }
    return builder;
  }

  private String generateFactory(int length, int type) {
    if (length < 1)
      throw new ServerException("字符串长度不能小于 1", RestCodeEnum.ERROR_SERVER);
    char[] buffer = new char[length];
    for (int i = 0, l = buffer.length; i < l; ++i) {
      if (type == NUMBER_SYMBOLS)
        buffer[i] = numberSymbols[random.nextInt(numberSymbols.length)];
      else
        buffer[i] = numberLetterSymbols[random.nextInt(numberLetterSymbols.length)];
    }
    return new String(buffer);
  }
}
