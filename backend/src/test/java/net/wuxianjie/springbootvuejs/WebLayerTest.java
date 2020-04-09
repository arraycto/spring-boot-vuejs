package net.wuxianjie.springbootvuejs;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import net.wuxianjie.springbootvuejs.service.AccessTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 不启动服务器进行测试
 *
 * <p>{@code @WebMvcTest} - 将测试范围缩小到 web 层</p>
 *
 * <p>{@code @WebMvcTest(AccessTokenController.class)} - 仅实例化一个 {@code AccessTokenController.class} 控制器</p>
 *
 * <p>这个测试断言与前一种情况（{@link WebApplicationTest}）相同，但在这个测试中，Spring Boot 只实例化 web 层，而不是整个上下文</p>
 *
 * @author 吴仙杰
 */
@WebMvcTest
@TestPropertySource(locations = "classpath:/sbv-conf/application.properties")
public class WebLayerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AccessTokenService service;

  @Test
  public void testShouldReturnNeedAccessTokenMessage() throws Exception {
    mockMvc.perform(get("/api")).andDo(print()).andExpect(status().is4xxClientError()).andExpect(content().string(containsString("【access_token】缺失")));
  }
}
