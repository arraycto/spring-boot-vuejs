package net.wuxianjie.springbootvuejs.dto;

import lombok.Data;

/**
 * Spring Security 中的当事人对象
 *
 * @author 吴仙杰
 */
@Data
public class PrincipalDto {

  private int userId;

  private String userName;
}
