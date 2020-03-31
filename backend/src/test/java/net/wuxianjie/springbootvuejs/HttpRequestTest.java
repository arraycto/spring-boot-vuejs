package net.wuxianjie.springbootvuejs;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

/**
 * 启动服务器进行测试
 *
 * <p>{@code webEnvironment=RANDOM_PORT} - 使用一个随机端口（这对于避免测试环境中的冲突非常有用）启动服务器</p>
 *
 * @author 吴仙杰
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

  /**
   * 使用 {@code @LocalServerPort} 注入 {@code webEnvironment=RANDOM_PORT} 中的随机端口
   */
  @LocalServerPort
  private int port;

  /**
   * Spring Boot 已经自动提供了一个 {@link TestRestTemplate}，故只需 {@code @Autwire} 注入即可
   */
  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void testShouldReturnNeedAccessTokenMessage() {
    assertThat(restTemplate.getForObject("http://localhost:" + port + "/", String.class)).contains("【access_token】缺失");
  }
}
