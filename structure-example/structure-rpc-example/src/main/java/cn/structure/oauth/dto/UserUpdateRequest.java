package cn.structure.oauth.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String email;
    private String phone;
    private Integer status;
}
