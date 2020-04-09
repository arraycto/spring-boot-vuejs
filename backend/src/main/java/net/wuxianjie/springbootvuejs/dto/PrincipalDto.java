package net.wuxianjie.springbootvuejs.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * Spring Security 中的当事人对象
 *
 * @author 吴仙杰
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PrincipalDto {

  private int userId;

  private String userName;
}
