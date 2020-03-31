package net.wuxianjie.springbootvuejs.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import net.wuxianjie.springbootvuejs.dto.AccessTokenDto;
import net.wuxianjie.springbootvuejs.service.AccessTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccessTokenController.class)
class AccessTokenControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AccessTokenService service;

  @Test
  public void testAccessTokenShouldReturnAccessToken() throws Exception {
    when(service.getAccessToken("fake_jason", "fake_123")).thenReturn(new AccessTokenDto("access_token", 1000));

    mockMvc.perform(post("/api/token").param("user_name", "fake_jason").param("password", "fake_123").accept("application/json; charset=UTF-8")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("expires_in")));
  }
}
