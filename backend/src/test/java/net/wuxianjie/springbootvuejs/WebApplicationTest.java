package net.wuxianjie.springbootvuejs;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 不启动服务器进行测试
 *
 * <p>在没有服务器的情况下，启动了完整的 Spring 应用程序上下文</p>
 *
 * <p>代码调用的方式与处理实际 HTTP 请求的方式完全相同，但是不需要启动服务器</p>
 *
 * <p>{@code @AutoConfigureMockMvc} - 表明可注入 Spring 的 {@code MockMvc}</p>
 *
 * @author 吴仙杰
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:/sbv-conf/application.properties")
@Disabled
public class WebApplicationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testShouldReturnNeedAccessTokenMessage() throws Exception {
    mockMvc.perform(get("/")).andDo(print()).andExpect(status().is4xxClientError()).andExpect(content().string(containsString("【access_token】缺失")));
  }
}
