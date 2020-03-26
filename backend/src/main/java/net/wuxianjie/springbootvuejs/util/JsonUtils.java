package net.wuxianjie.springbootvuejs.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import net.wuxianjie.springbootvuejs.exception.JsonException;
import net.wuxianjie.springbootvuejs.rest.RestCodeEnum;

/**
 * JSON 序列化与反序列化工具类
 *
 * @author 吴仙杰
 */
@SuppressWarnings("unused")
public class JsonUtils {

  private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

  /**
   * JSON 序列化
   *
   * @param value          需要序列化的 Java 值
   * @param isDateReadable 是否需要日期可读性转换，可读性日期格式为 {@code yyyy-MM-dd HH:mm:ss}
   * @return JSON 字符串
   * @throws JsonException 当 JSON 序列化失败时抛出
   */
  public static String toJson(Object value, boolean isDateReadable) {
    ObjectMapper mapper = new ObjectMapper();
    if (isDateReadable) {
      DateFormat df = new SimpleDateFormat(JsonUtils.DATE_TIME_PATTERN);
      mapper.setDateFormat(df);
    }
    try {
      return mapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new JsonException(String.format("JSON 序列化【%s】失败：%s", value, e.getMessage()), e, RestCodeEnum.ERROR);
    }
  }

  /**
   * JSON 反序列化
   *
   * @param json           需要 JSON 反序列化的字符串
   * @param valueType      JSON 反序列化后所得到对象的类型
   * @param isDateReadable 是否需要日期可读性转换，可读性日期格式为 {@code yyyy-MM-dd HH:mm:ss}
   * @return 指定类型的对象
   * @throws JsonException 当 JSON 反序列化失败时抛出
   */
  public static <T> T parseJson(String json, Class<T> valueType, boolean isDateReadable) {
    ObjectMapper mapper = JsonUtils.getObjectMapper(json, valueType, isDateReadable);
    try {
      return mapper.readValue(json, valueType);
    } catch (IOException e) {
      throw new JsonException(String.format("JSON 反序列化【%s】失败：%s", json, e.getMessage()), e, RestCodeEnum.ERROR);
    }
  }

  /**
   * JSON 反序列化，支持完整泛型类型信息
   *
   * <p>例如，实例化对泛型类型 {@code List<Integer>} 的引用：</p>
   *
   * <p>{@code TypeReference ref = new TypeReference<List<Integer>>() { };}</p>
   *
   * @param json           需要 JSON 反序列化的字符串
   * @param ref            JSON 反序列化后所得到对象的类型，支持完整泛型类型信息
   * @param isDateReadable 是否需要日期可读性转换，可读性日期格式为 {@code yyyy-MM-dd HH:mm:ss}
   * @return 指定类型的对象
   * @throws JsonException 当 JSON 反序列化失败时抛出
   */
  public static <T> T parseJson(String json, TypeReference<T> ref, boolean isDateReadable) {
    ObjectMapper mapper = JsonUtils.getObjectMapper(json, ref, isDateReadable);
    try {
      return mapper.readValue(json, ref);
    } catch (IOException e) {
      throw new JsonException(String.format("JSON 反序列化【%s】失败：%s", json, e.getMessage()), e, RestCodeEnum.ERROR);
    }
  }

  private static ObjectMapper getObjectMapper(String json, Object type, boolean isDateReadable) {
    ObjectMapper mapper = new ObjectMapper();
    if (isDateReadable) {
      DateFormat df = new SimpleDateFormat(JsonUtils.DATE_TIME_PATTERN);
      mapper.setDateFormat(df);
    }
    return mapper;
  }
}
