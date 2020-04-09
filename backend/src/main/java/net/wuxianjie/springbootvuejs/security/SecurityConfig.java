package net.wuxianjie.springbootvuejs.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring Security 配置
 *
 * <p>Spring Security 要求角色名必须大写，且必须以前缀 {@code ROLE_} 开头</p>
 *
 * <p>而在使用时，可忽略前缀 {@code ROLE_}，比如：{@code @PreAuthorize("hasRole('ADMIN')")}</p>
 *
 * @author 吴仙杰
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .authorizeRequests()
      .antMatchers(SecurityConstants.PERMIT_ALL_PATH).permitAll()
      .anyRequest().authenticated()
      .and()
      .csrf().disable()
      .addFilterBefore(new SecurityContextFilter(), JwtAuthenticationFilter.class)
      .addFilter(new JwtAuthenticationFilter());
  }
}
