package net.wuxianjie.springbootvuejs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Spring Boot 程序主入口
 *
 * <p>{@code @SpringBootApplication} 等同于组合使用以下三个注解的默认配置：</p>
 *
 * <ul>
 *   <li>{@code @Configuration} - 允许在上下文中注册额外的 bean 或导入其他配置类</li>
 *   <li>{@code @EnableAutoConfiguration} - 启用 Spring Boot 的自动配置机制，搜索该注解类下的所有包</li>
 *   <li>{@code @ComponentScan} - 在应用程序所在位置的包上启用 {@code @Component} 扫描</li>
 * </ul>
 *
 * <p>4、Spring Boot WAR：Tomcat 需要应用程序符合 Servlet API 3.0 规范，
 * 实现 {@code SpringBootServletInitializer} 接口来初始化 Tomcat 所需的 Servlet 上下文</p>
 *
 * @author 吴仙杰
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
@MapperScan("net.wuxianjie.springbootvuejs.mapper")
public class SpringBootVuejsApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(SpringBootVuejsApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringBootVuejsApplication.class, args);
  }
}
