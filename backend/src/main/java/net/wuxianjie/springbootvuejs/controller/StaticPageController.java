package net.wuxianjie.springbootvuejs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 静态 HTML 页面映射控制器
 *
 * <p>为了处理 Vue.js 多页面</p>
 *
 * @author 吴仙杰
 */
@Controller
public class StaticPageController {

  /**
   * 返回 {@code login.html} 静态文件
   *
   * @return {@code login.html} 静态文件
   */
  @GetMapping("/login")
  public String loginPage() {
    return "login";
  }
}
