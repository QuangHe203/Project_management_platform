package fsa.miniproject.dto;

import fsa.miniproject.enums.RoleEnum;
import lombok.Data;

@Data
public class DetailUserDto {
    private Integer accountId;
    private RoleEnum role;
    private String name;
    private String email;
    private String password;
    private Integer teamId;

    public DetailUserDto(Integer accountId, RoleEnum role, String name, String email, String password, Integer teamId) {
        this.accountId = accountId;
        this.role = role;
        this.name = name;
        this.email = email;
        this.password = password;
        this.teamId = teamId;
    }
}
