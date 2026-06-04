package cn.structure.oauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class UserInfo {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private Integer status;
    private Long createTime;
    private Long updateTime;
}
