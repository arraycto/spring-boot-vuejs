package net.wuxianjie.springbootvuejs.util;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;
import net.wuxianjie.springbootvuejs.exception.HttpException;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * HTTP 请求工具类
 *
 * @author 吴仙杰
 */
public class HttpUtils {

  public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  private static final OkHttpClient httpClient = new OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .writeTimeout(15, TimeUnit.SECONDS)
    .readTimeout(15, TimeUnit.SECONDS)
    .build();

  /**
   * 发送 GET 请求
   *
   * @param url    服务器 URL
   * @param params 请求参数
   * @return 服务器响应结果字
   * @throws HttpException 当请求失败时
   */
  public static String get(String url, Map<String, String> params) {
    HttpUrl httpUrl = HttpUrl.parse(url);
    if (httpUrl == null) {
      throw new HttpException(String.format("URL【%s】不合法", url), RestCodeEnum.ERROR_SERVER);
    }

    HttpUrl.Builder httpBuilder = httpUrl.newBuilder();
    if (params != null) {
      for (Map.Entry<String, String> param : params.entrySet()) {
        httpBuilder.addQueryParameter(param.getKey(), param.getValue());
      }
    }

    Request request = new Request.Builder()
      .url(httpBuilder.build())
      .build();

    return execute(request, null);
  }

  /**
   * 发送 POST Form 请求
   *
   * @param url 服务器 URL
   * @param params 向服务器提交的参数
   * @return 服务器响应结果
   * @throws HttpException 当请求失败时
   */
  public static String postForm(String url, Map<String, String> params) {
    FormBody.Builder builder = new FormBody.Builder();
    if (params != null) {
      for (Map.Entry<String, String> entry : params.entrySet()) {
        builder.add(entry.getKey(), entry.getValue());
      }
    }

    Request request = new Request.Builder()
      .url(url)
      .post(builder.build())
      .build();

    return execute(request, JsonUtils.toJson(params, true));
  }

  /**
   * 发送 POST JSON 请求
   *
   * @param url 服务器 URL
   * @param json 向服务器提交的参数，JSON 字符串
   * @return 服务器响应结果
   * @throws HttpException 当请求失败时
   */
  public static String postJson(String url, String json) {
    RequestBody body = RequestBody.create(JSON, json);

    Request request = new Request.Builder()
      .url(url)
      .post(body)
      .build();

    return execute(request, json);
  }

  /**
   * 执行 HTTP 请求
   *
   * @param request HTTP 请求对象
   * @return 服务器响应结果
   * @throws HttpException 当请求失败时
   */
  private static String execute(Request request, String param) {
    try (Response response = httpClient.newCall(request).execute()) {

      ResponseBody responseBody = response.body();
      if (responseBody == null)
        throw new HttpException(
          String.format("HTTP【%s %s】参数【%s】请求失败：%s",
            request.method(),
            request.url().toString(),
            param,
            "响应体为 null"),
          RestCodeEnum.ERROR_SERVER);
      String result = responseBody.string();

      if (!response.isSuccessful())
        throw new HttpException(
          String.format("HTTP【%s %s】MIME【%s】参数【%s】请求失败：%s",
            request.method(),
            request.url().toString(),
            request.body() == null ? null : request.body().contentType(),
            param,
            result),
          RestCodeEnum.ERROR_INVOKE);

      return result;

    } catch (IOException e) {
      throw new HttpException(
        String.format("HTTP【%s %s】MIME【%s】参数【%s】请求失败：%s",
          request.method(),
          request.url().toString(),
          request.body() == null ? null : request.body().contentType(),
          param,
          e.getMessage()),
        e,
        RestCodeEnum.ERROR_INVOKE);
    }
  }
}
