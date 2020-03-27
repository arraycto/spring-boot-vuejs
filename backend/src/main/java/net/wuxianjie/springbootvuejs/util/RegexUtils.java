package net.wuxianjie.springbootvuejs.util;

import com.google.common.base.Strings;

/**
 * 正则工具类
 *
 * @author 吴仙杰
 */
public class RegexUtils {

  /**
   * 匹配 32 位 UUID（不含 {@code -}）
   */
  public static final String UUID_REGEX = "([a-fA-F0-9]{8}[a-fA-F0-9]{4}[a-fA-F0-9]{4}[a-fA-F0-9]{4}[a-fA-F0-9]{12})";

  /**
   * 匹配 36 位 UUID（包含 {@code -}）
   */
  public static final String UUID2_REGEX = "([a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12})";

  /**
   * 匹配手机号码
   */
  public static final String CELLPHONE_REGEX = "^(1[0-9]{10})$";

  /**
   * 匹配包含区号的电话号码
   */
  public static final String TELEPHONE_REGEX = "^0\\d{2,3}-?\\d{7,8}$";

  /**
   * 匹配邮政编码
   */
  public static final String POSTCODE_REGEX = "^[1-9]\\d{5}$";

  /**
   * 匹配密码复杂度
   *
   * <p>规则：字符串长度至少 6 位最多 36 位，且至少包含一个数字、字母和特殊字符（{@code $ @ _ ! % * # ? &}）</p>
   */
  public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@_!%*#?&])[A-Za-z\\d$@_!%*#?&]{6,36}$";

  /**
   * 匹配用户名
   *
   * <p>规则：字符串长度至少 2 位最多 15 位，且只能包含中文、数字、字母和下划线（{@code _}）</p>
   */
  public static final String USERNAME_REGEX = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]{2,15}$";

  /**
   * 匹配编码命名
   *
   * <p>规则：字符串长度至少 2 位最多 15 位，以字母或下划线开头，只能包含数字、字母、下划线（{@code _}）</p>
   */
  public static final String CODENAME_REGEX = "^[A-Za-z_]\\w{1,14}$";

  /**
   * 匹配邮箱
   */
  public static final String EMAIL_REGEX = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";

  /**
   * 匹配 1 个或多个正整数
   */
  public static final String POSITIVE_INTEGER_REGEX = "^\\d+$";

  /**
   * 匹配身份证号码
   *
   * <p>身份证 15 位编码规则：{@code dddddd yymmdd xx p}</p>
   *
   * <ul>
   *  <li>{@code dddddd}：6 位地区编码</li>
   *  <li>{@code yymmdd}：出生年（两位年）月日，如：910215</li>
   *  <li>{@code xx}：顺序编码，系统产生，无法确定</li>
   *  <li>{@@code p}：性别，奇数为男，偶数为女</li>
   * </ul>
   *
   * <p>身份证 18 位编码规则：{@code dddddd yyyymmdd xxx y}</p>
   *
   * <ul>
   *  <li>{@code dddddd}：6 位地区编码</li>
   *  <li>{@code yyyymmdd}：出生年（四位年）月日，如：19910215</li>
   *  <li>{@code xxx}：顺序编码，系统产生，无法确定，奇数为男，偶数为女</li>
   *  <li>{@code y}：校验码，该位数值可通过前 17 位计算获得</li>
   *  <li>前 17 位号码加权因子为 {@code Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ]}</li>
   *  <li>验证位 {@code Y = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ]}</li>
   *  <li>如果验证码恰好是 10，为了保证身份证是十八位，那么第十八位将用 X 来代替 校验位计算公式：{@code Y_P = mod( ∑(Ai×Wi),11 )}</li>
   *  <li>{@code i} 为身份证号码 {@code 1...17} 位；{@code Y_P} 为校验码 {@code Y} 所在校验码数组位置</li>
   * </ul>
   */
  public static final String ID_CARD_REGEX = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([012]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([012]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";

  /**
   * 匹配 URL
   */
  public static final String URL_REGEX = "^(http(s?):\\/\\/)?(www\\.)?[a-zA-Z0-9.\\-_]+(:[0-9]+)?(\\/[a-zA-Z0-9_\\-\\s.\\/?%#&=]*)?$";

  /**
   * 匹配 IP 地址
   */
  public static final String IP_REGEX = "^(\\d{1,3}?)\\.(\\d{1,3}?)\\.(\\d{1,3}?)\\.(\\d{1,3}?)$";

  /**
   * 判断字符串与正则是否匹配
   *
   * @param value 需要判断的字符串
   * @param regex 正则字符串
   * @return 当且仅当匹配成功时，才返回 {@code true}
   */
  public static boolean isMatch(String value, String regex) {
    return Strings.nullToEmpty(value).trim().matches(regex);
  }
}
