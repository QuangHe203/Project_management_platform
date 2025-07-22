package fsa.miniproject.dto;

import fsa.miniproject.entity.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserDto {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password have 8 character at least")
    private String password;

    private RoleEnum role; // mặc định là thành viên

    // getters & setters
}
