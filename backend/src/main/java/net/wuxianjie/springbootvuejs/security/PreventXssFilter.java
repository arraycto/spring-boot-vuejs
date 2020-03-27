package net.wuxianjie.springbootvuejs.security;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 防跨站脚本攻击（XSS），定义危险字符过滤器
 *
 * @author 吴仙杰
 */
@Component
@Order
public class PreventXssFilter implements Filter {

  /**
   * 忽略 {@code script} 标记之间的任何内容
   */
  private static final Pattern SCRIPT_TAG_PATTERN = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);

  /**
   * 忽略 {@code src='...'} 中的任何内容
   */
  private static final Pattern SCRIPT_SRC_SINGLE_QUOTE_PATTERN = Pattern.compile("src[\r\n]*=[\r\n]*'(.*?)'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

  /**
   * 忽略 {@code src="..."} 中的任何内容
   */
  private static final Pattern SCRIPT_SRC_DOUBLE_QUOTE_PATTERN = Pattern.compile("src[\r\n]*=[\r\n]*\"(.*?)\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

  /**
   * 忽略任何单独的 {@code </script>} 标签
   */
  private static final Pattern SCRIPT_LONESOME_CLOSED_TAG_PATTERN = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);

  /**
   * 忽略任何单独的 {@code <script ...>} 标签
   */
  private static final Pattern SCRIPT_LONESOME_TAG_PATTERN = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

  /**
   * 忽略 {@code eval(...)} 表达式
   */
  private static final Pattern SCRIPT_EVAL_PATTERN = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

  /**
   * 忽略 {@code expression(...)} 表达式
   */
  private static final Pattern SCRIPT_EXPRESSION_PATTERN = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

  /**
   * 忽略 {@code javascript:...} 表达式
   */
  private static final Pattern SCRIPT_JAVASCRIPT_PATTERN = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);

  /**
   * 忽略 {@code vbscript:...} 表达式
   */
  private static final Pattern SCRIPT_VBSCRIPT_PATTERN = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);

  /**
   * 忽略 {@code onload=} 表达式
   */
  private static final Pattern SCRIPT_ONLOAD_PATTERN = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

  @Override
  public void init(FilterConfig filterConfig) {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    // 包装原始 HTTP 请求
    XssRequestWrapper wrapper = new XssRequestWrapper((HttpServletRequest) servletRequest);
    // 进入责任链的下一条
    filterChain.doFilter(wrapper, servletResponse);
  }

  @Override
  public void destroy() {

  }

  /**
   * 改写 HTTP 请求，重写原 HTTP 获取参数名和参数值的方法
   */
  static class XssRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 处理过后的请求参数
     */
    private Map<String, String[]> sanitizedQueryString;

    XssRequestWrapper(HttpServletRequest request) {
      super(request);
    }

    @Override
    public String getParameter(String name) {
      // 获取经新方法处理后的值
      String[] values = getParameterMap().get(name);
      // 若处理后的值不为空，则返回处理后的值
      if (values != null && values.length > 0) {
        return values[0];
      }

      // 若处理后的值为空，则调用来原请求的方法返回
      return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
      // 若未处理过当前请求，则进行相应的参数处理
      if (sanitizedQueryString == null) {
        // 处理后的参数结果集
        Map<String, String[]> result = new HashMap<>();
        // 从原请求中获取所有参数
        Map<String, String[]> originalQueryString = super.getParameterMap();
        // 若请求参数不为空，则进行个性化参数处理
        if (originalQueryString != null) {

          for (String key : originalQueryString.keySet()) {
            // 获取原来的值
            String[] rawValArray = originalQueryString.get(key);
            // 处理后的值
            String[] sanitizedValArray = new String[rawValArray.length];

            for (int i = 0; i < rawValArray.length; i++) {
              // 过滤值中敏感字符
              sanitizedValArray[i] = stripXSS(rawValArray[i]);
            }

            // 过滤参数名中的敏感字符，并加入结果集中
            result.put(stripXSS(key), sanitizedValArray);
          }
        }
        sanitizedQueryString = result;
      }
      return sanitizedQueryString;
    }

    @Override
    public String[] getParameterValues(String name) {
      return getParameterMap().get(name);
    }

    @Override
    public Enumeration<String> getParameterNames() {
      return Collections.enumeration(getParameterMap().keySet());
    }

    /**
     * 删除字符串中可能存在的恶意字符
     *
     * @param val 原始字符串
     * @return 删除恶意字符后的字符串
     */
    private String stripXSS(String val) {
      String cleanVal = null;
      if (val != null) {
        cleanVal = Normalizer.normalize(val, Normalizer.Form.NFD);
        // 忽略 `null` 字符
        cleanVal = cleanVal.replaceAll("\0", "");
        cleanVal = SCRIPT_TAG_PATTERN.matcher(cleanVal).replaceAll("");
        cleanVal = SCRIPT_SRC_SINGLE_QUOTE_PATTERN.matcher(cleanVal).replaceAll("");
        cleanVal = SCRIPT_SRC_DOUBLE_QUOTE_PATTERN.matcher(cleanVal).replaceAll("");
        cleanVal = SCRIPT_LONESOME_CLOSED_TAG_PATTERN.matcher(cleanVal).replaceAll("");
        cleanVal = SCRIPT_LONESOME_TAG_PATTERN.matcher(cleanVal).replaceAll("");
        cleanVal = SCRIPT_EVAL_PATTERN.matcher(cleanVal).replaceAll("");
        cleanVal = SCRIPT_EXPRESSION_PATTERN.matcher(cleanVal).replaceAll("");
        cleanVal = SCRIPT_JAVASCRIPT_PATTERN.matcher(cleanVal).replaceAll("");
        cleanVal = SCRIPT_VBSCRIPT_PATTERN.matcher(cleanVal).replaceAll("");
        cleanVal = SCRIPT_ONLOAD_PATTERN.matcher(cleanVal).replaceAll("");
      }
      return cleanVal;
    }
  }
}
