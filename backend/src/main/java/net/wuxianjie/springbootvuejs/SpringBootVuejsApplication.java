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
 * <p>{@code @SpringBootApplication} 等同于组合使用以下四个注解的默认配置：</p>
 *
 * <ul>
 *   <li>{@code @Configuration} - 将该类标记为程序上下文中 bean 定义的源</li>
 *   <li>{@code @EnableAutoConfiguration} - 告诉 Spring Boot 根据类路径、其他 bean 和各种属性配置来加入 bean</li>
 *   <li>{@code @EnableWebMvc} - 将程序标记为 web 应用，并激活关键行为。当 Spring Boot 在类路径中发现 {@code spring-webmvc} 时，会自动添加该注解</li>
 *   <li>{@code @ComponentScan} - 告诉 Spring Boot 在当前包及子包中查找其他控制器（component）、配置（configuration）和服务（service）</li>
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
