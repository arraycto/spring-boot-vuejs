package net.wuxianjie.springbootvuejs.controller;

import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class BackendController {

  @RequestMapping(path = "/hello")
  public String sayHello() {
    return "Hello Spring Boot & Vue.js";
  }

  @RequestMapping(path = "/user/{lastName}/{firstName}", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public long addNewUser(@PathVariable("lastName") String lastName,
      @PathVariable("firstName") String firstName) {
    return 123456789;
  }

  @GetMapping(path = "/user/{id}")
  public Object getUserById(@PathVariable("id") long id) {

    return new HashMap<String, Object>() {{
      put("firstName", "仙杰");
      put("lastName", "吴");
      put("id", "25");
    }};
  }

  @RequestMapping(path = "/secured", method = RequestMethod.GET)
  public @ResponseBody
  String getSecured() {
    return "返回来至设保护的资源";
  }
}
