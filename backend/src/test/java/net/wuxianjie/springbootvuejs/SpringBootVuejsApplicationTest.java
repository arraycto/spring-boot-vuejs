package net.wuxianjie.springbootvuejs;

import static org.assertj.core.api.Assertions.assertThat;

import net.wuxianjie.springbootvuejs.controller.AccessTokenController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>{@code @SpringBootTest} 注解告诉 Spring Boot 去寻找一个主配置类（例如，一个带有 {@code @SpringBootApplication} 的类），然后使用该类来启动 Spring 应用程序上下文</p>
 *
 * <p>Spring 测试支持的一个很好的特性是在测试之间缓存应用程序上下文。
 * 如果在一个测试用例中有多个方法，或者多个测试用例具有相同的配置，那么它们只会产生一次启动应用程序的成本。
 * 可以使用 {@code @DirtiesContext} 注释来控制缓存</p>
 *
 * @author 吴仙杰
 */
@SpringBootTest
public class SpringBootVuejsApplicationTest {

  /**
   * Spring 解释 {@code @Autowired} 注解，在测试方法运行之前注入控制器
   */
  @Autowired
  private AccessTokenController controller;

  @Test
  public void testShouldLoadContextIsNotNull() {
    // 使用 `AssertJ`（提供了 `assertThat()` 等方法）来表示测试断言
    assertThat(controller).isNotNull();
  }
}
