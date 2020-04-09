package net.wuxianjie.springbootvuejs.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class HttpUtilsTest {

  @Test
  public void testGet() {
    Map<String, String> params = new HashMap<String, String>() {{
      put("name", "Jason Wu");
      put("age", "25");
    }};

    String response = HttpUtils.get("http://httpbin.org/get", params);
    System.out.println(response);
    assertTrue(true);
  }

  @Test
  public void testPostForm() {
    Map<String, String> params = new HashMap<>();
    params.put("user_name", "jason");
    params.put("password", "123");

    String response = HttpUtils.postForm("http://httpbin.org/post", params);
    System.out.println(response);
    assertTrue(true);
  }

  @Test
  public void testPostJson() {
    Map<String, Object> params = new HashMap<>();
    params.put("name", "Jason Wu");
    params.put("age", 25);
    String json = JsonUtils.toJson(params, true);

    String response = HttpUtils.postJson("http://httpbin.org/post", json);
    System.out.println(response);
    assertTrue(true);
  }
}
