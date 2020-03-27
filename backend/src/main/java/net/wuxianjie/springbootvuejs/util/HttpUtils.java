package net.wuxianjie.springbootvuejs.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import net.wuxianjie.springbootvuejs.exception.HttpException;
import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 有关 HTTP 处理的工具类
 *
 * @author 吴仙杰
 */
public class HttpUtils {

  /**
   * 超时时间（单位：毫秒）
   */
  private static final int TIMEOUT_MS = 15000;

  /**
   * 连接池最大连接数
   */
  private static final int MAX_CONNECTIONS = 50;

  /**
   * 创建信息所有证书的信任管理器
   */
  private static final X509TrustManager X_509_TRUST_MANAGER = new X509TrustManager() {

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return null;
    }
  };

  /**
   * GET 请求
   *
   * @param url             请求地址
   * @param params          请求参数
   * @param headers         请求头
   * @param responseCharset 响应编码
   * @return 响应结果字符串
   * @throws HttpException 当请求失败时
   */
  public static String get(String url, Map<String, String> params, Map<String, String> headers, String responseCharset) {
    // 拼接请求参数至 URL 中
    URIBuilder uriBuilder;
    try {
      uriBuilder = new URIBuilder(url);
      if (params != null && !params.isEmpty()) {
        for (Map.Entry<String, String> item : params.entrySet()) {
          uriBuilder.setParameter(item.getKey(), item.getValue());
        }
      }
    } catch (URISyntaxException e) {
      throw new HttpException(String.format("构建请求【%s】失败：%s", url, e.getMessage()), e, RestCodeEnum.ERROR_SERVER);
    }

    // 1、创建请求方法的实例，并指定请求 URL
    HttpGet httpGet;
    try {
      httpGet = new HttpGet(uriBuilder.build());
    } catch (URISyntaxException e) {
      throw new HttpException(String.format("构建请求【%s】失败：%s", uriBuilder.toString(), e.getMessage()), e, RestCodeEnum.ERROR_SERVER);
    }

    return execute(headers, httpGet, responseCharset);
  }

  /**
   * POST 请求（表单提交）
   *
   * <p>{@code Content-Type: application/x-www-form-urlencoded}</p>
   *
   * @param url             请求地址
   * @param params          请求参数
   * @param requestCharset  请求编码
   * @param headers         请求头
   * @param responseCharset 响应编码
   * @return 响应结果字符串
   * @throws HttpException 当请求失败时
   */
  public static String postForm(String url, Map<String, Object> params, String requestCharset, Map<String, String> headers, String responseCharset) {
    // 1、 创建请求方法的实例，并指定请求 URL
    HttpPost httpPost = new HttpPost(url);

    // 设置请求参数
    if (params != null && !params.isEmpty()) {
      List<NameValuePair> paramList = new ArrayList<>();

      for (Map.Entry<String, Object> item : params.entrySet()) {
        paramList.add(new BasicNameValuePair(item.getKey(), String.valueOf(item.getValue())));
      }

      try {
        httpPost.setEntity(new UrlEncodedFormEntity(paramList, requestCharset));
      } catch (UnsupportedEncodingException e) {
        throw new HttpException(String.format("构建请求【%s】失败：%s", httpPost.getURI(), e.getMessage()), e, RestCodeEnum.ERROR_SERVER);
      }
    }

    return execute(headers, httpPost, responseCharset);
  }

  /**
   * POST 请求（JSON 提交）
   *
   * <p>{@code Content-Type: application/json}</p>
   *
   * @param url             请求地址
   * @param json            请求参数
   * @param headers         请求头
   * @param responseCharset 响应编码
   * @return 响应结果字符串
   * @throws HttpException 当请求失败时
   */
  public static String postJson(String url, String json, Map<String, String> headers, String responseCharset) {
    // 1、 创建请求方法的实例，并指定请求 URL
    HttpPost httpPost = new HttpPost(url);

    // 设置请求参数
    StringEntity entity;
    try {
      entity = new StringEntity(json);
    } catch (UnsupportedEncodingException e) {
      throw new HttpException(String.format("构建请求【%s】失败：%s", httpPost.getURI(), e.getMessage()), e, RestCodeEnum.ERROR_SERVER);
    }
    httpPost.setEntity(entity);

    return execute(headers, httpPost, responseCharset);
  }

  /**
   * 执行请求
   *
   * @param headers         请求头
   * @param httpMethod      请求方法
   * @param responseCharset 响应编码
   * @return 从服务器返回的结果字符串
   * @throws HttpException 当请求失败时
   */
  private static String execute(Map<String, String> headers, HttpUriRequest httpMethod, String responseCharset) {
    String result = null;

    // 设置请求头
    if (headers != null && !headers.isEmpty()) {
      for (Map.Entry<String, String> item : headers.entrySet()) {
        httpMethod.setHeader(item.getKey(), item.getValue());
      }
    }

    // 使用 java7 的 `try-resource-with` 语法，
    // 该语法能够自动关闭 `try()` 中实现了 `Closeable` 或 `AutoCloseable` 接口的对象，而无须在 `finally` 语句中手动关闭资源
    try (
        // 2、创建 `HttpClient` 对象
        CloseableHttpClient httpClient = getHttpClient();

        // 3、执行请求
        CloseableHttpResponse response = httpClient.execute(httpMethod)
    ) {

      // 4、获取服务器的响应内容
      HttpEntity entity = response.getEntity();

      if (entity != null) {
        result = EntityUtils.toString(entity, responseCharset);
        // 关闭 `HttpEntity` 的流
        EntityUtils.consume(entity);
      }

      return result;
    } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
      throw new HttpException(String.format("请求【%s】失败：%s", httpMethod.getURI(), e.getMessage()), e, RestCodeEnum.ERROR_SERVER);
    }
    // 5、释放连接
  }

  /**
   * 获取兼容 http 和 https 请求的 {@code httpClient}
   *
   * @return HTTP 请求对象
   * @throws NoSuchAlgorithmException 当 TLS 协议获取失败时
   * @throws KeyManagementException   当初始化 SSL 上下文失败时
   */
  private static CloseableHttpClient getHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
    SSLContext sslContext = SSLContext.getInstance("TLS");
    // 初始化 SSL 上下文
    sslContext.init(null, new TrustManager[]{X_509_TRUST_MANAGER}, null);
    // SSL 套接字连接工厂，`NoopHostnameVerifier` 为信任所有服务器
    SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
    // 注册 http 套接字工厂和 https 套接字工厂
    Registry<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create()
        .register("http", PlainConnectionSocketFactory.INSTANCE)
        .register("https", sslConnectionSocketFactory)
        .build();

    // 设置超时时间
    RequestConfig requestConfig = RequestConfig.custom()
        // `http.connection.timeout` 与远程主机建立连接的时间，即建立 TCP 连接的超时时间
        // 连接上服务器（握手成功）的时间，超出该时间抛出 `connect timeout`
        .setConnectTimeout(TIMEOUT_MS)
        // `http.socket.timeout` 建立连接后，等待数据的时间；两个数据包之间的最长不活动时间
        // 服务器返回数据（response）的时间，超过该时间抛出 `read timeout`
        .setSocketTimeout(TIMEOUT_MS)
        // `http.connection-manager.timeout` 等待连接管理器/池连接的时间
        // 从连接池中获取连接的超时时间，超过该时间未拿到可用连接，时会抛出 `org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool`
        .setConnectionRequestTimeout(TIMEOUT_MS)
        .build();

    // 连接池管理器
    PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registryBuilder);
    // 连接池最大连接数
    poolingHttpClientConnectionManager.setMaxTotal(MAX_CONNECTIONS);
    // 每个路由最大连接数
    poolingHttpClientConnectionManager.setDefaultMaxPerRoute(MAX_CONNECTIONS);

    // 创建 `HttpClient` 对象并返回
    return HttpClients.custom()
        // 保证 `httpClient` 实例能发 https 请求
        .setSSLSocketFactory(sslConnectionSocketFactory)
        .setDefaultRequestConfig(requestConfig)
        .setConnectionManager(poolingHttpClientConnectionManager)
        .build();
  }
}
