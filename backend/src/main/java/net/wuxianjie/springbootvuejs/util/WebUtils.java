package net.wuxianjie.springbootvuejs.util;

import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;
import net.wuxianjie.springbootvuejs.exception.ServerException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Web 相关工具类
 *
 * @author 吴仙杰
 */
public class WebUtils {

  /**
   * 获取 {@code HttpServletRequest} 对象
   *
   * @return {@code HttpServletRequest} 对象
   * @throws ServerException 当 {@code RequestAttributes} 为 {@code null} 时
   */
  public static HttpServletRequest getHttpServletRequest() {
    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (servletRequestAttributes == null)
      throw new ServerException("当前线程的 RequestAttributes 对象为 null", RestCodeEnum.ERROR_SERVER);
    return servletRequestAttributes.getRequest();
  }

  /**
   * 获取 {@code HttpServletResponse} 对象
   *
   * @return {@code HttpServletResponse} 对象
   * @throws ServerException 当 {@code RequestAttributes} 为 {@code null} 时
   */
  public static HttpServletResponse getHttpServletResponse() {
    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (servletRequestAttributes == null)
      throw new ServerException("当前线程的 RequestAttributes 对象为 null", RestCodeEnum.ERROR_SERVER);
    return servletRequestAttributes.getResponse();
  }

  /**
   * 获取网站的基准路径
   *
   * @return 网站基准路径（{@code 协议://服务器地址:端口/Web应用的虚拟路径}）
   */
  public static String getBaseUrl() {
    HttpServletRequest request = getHttpServletRequest();
    return request.getScheme()
        + "://" + request.getServerName()
        + ":" + request.getServerPort()
        + request.getContextPath();
  }

  /**
   * 获取网站的绝对根路径
   *
   * @return 绝对根路径，结尾必定包含文件分隔符
   */
  public static String getWebRootPath() {
    ServletContext context = getHttpServletRequest().getServletContext();
    String rootPath = context.getRealPath(File.separator);
    if (!rootPath.endsWith(File.separator))
      rootPath += File.separator;
    return rootPath;
  }

  /**
   * 获取请求 URL（包含查询参数）
   *
   * @return 请求 URL（包含查询参数）
   */
  private static String getCurrentUrl(HttpServletRequest request) {
    // 获取请求 URL（包含协议、服务器名称、端口号和服务器路径，但不包含查询字符串参数）
    StringBuffer urlBuilder = request.getRequestURL();
    // 获取在请求 URL 路径后的查询字符串参数
    String queryStr = request.getQueryString();
    if (Strings.isNullOrEmpty(queryStr)) {
      return urlBuilder.toString();
    } else {
      return urlBuilder.append("?").append(queryStr).toString();
    }
  }

  /**
   * 获取当前请求的真实来源 IP
   *
   * @return 当前请求的真实来源 IP（可能为空）
   */
  public static String getCurrentRequestIp() {
    HttpServletRequest request = getHttpServletRequest();
    String ip = request.getHeader("X-Forwarded-For");
    if (ip != null && ip.indexOf(",") > 0) {
      // 对于通过多个代理的情况，第一个 IP 为客户端真实 IP，多个 IP 按照 `,` 分割
      ip = ip.substring(0, ip.indexOf(","));
    }
    if (Strings.isNullOrEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (Strings.isNullOrEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (Strings.isNullOrEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (Strings.isNullOrEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (Strings.isNullOrEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("X-Real-IP");
    }
    if (Strings.isNullOrEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
      InetAddress inet;
      try { // 根据网卡取本机配置的 IP
        inet = InetAddress.getLocalHost();
      } catch (UnknownHostException e) {
        return "";
      }
      ip = inet.getHostAddress();
    }
    return ip;
  }

  /**
   * 获取当前机器的 IP
   *
   * @return 当前机器的 IP
   * @throws ServerException 当 IP 获取失败时
   */
  public static String getCurrentMachineIp() {
    try (final DatagramSocket socket = new DatagramSocket()) {
      socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
      return socket.getLocalAddress().getHostAddress();
    } catch (SocketException | UnknownHostException e) {
      throw new ServerException(String.format("获取当前服务器所在 IP 失败：%s", e.getMessage()), RestCodeEnum.ERROR_SERVER);
    }
  }

  /**
   * Ping HTTP URL
   *
   * @param url     需要 ping 的 URL
   * @param timeout 连接超时和响应读取超时的超时（单位：毫秒）。注意，总超时实际上是给定超时的两倍
   * @return 当且仅当给定的 URL 在接收 GET 请求时响应码为 {@code [200..300)} 时，才返回 {@code true}
   */
  public static boolean pingUrl(String url, int timeout) {
    // 将 HTTPS 请求替换为 HTTP，否则可能会在无效的 SSL 证书上引发异常
    url = url.replaceFirst("^https", "http");
    try {
      HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
      connection.setConnectTimeout(timeout);
      connection.setReadTimeout(timeout);
      connection.setRequestMethod("GET");
      int responseCode = connection.getResponseCode();
      return (200 <= responseCode && responseCode < 300);
    } catch (IOException exception) {
      return false;
    }
  }
}
