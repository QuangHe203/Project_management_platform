package fsa.miniproject.dto;

import lombok.Data;

@Data
public class UserDto {
    private Integer accountId;
    private String name;
    private String email;

    public UserDto(Integer accountId, String name, String email) {
        this.accountId = accountId;
        this.name = name;
        this.email = email;
    }

    // getters & setters
}
