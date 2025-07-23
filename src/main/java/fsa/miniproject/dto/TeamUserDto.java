package fsa.miniproject.dto;

import fsa.miniproject.enums.RoleEnum;
import lombok.Data;

@Data
public class TeamUserDto {
    private Integer accountId;
    private String name;
    private String email;
    private RoleEnum role;

    public TeamUserDto(Integer accountId, String name, String email, RoleEnum role) {
        this.accountId = accountId;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
