package fsa.miniproject.dto;

import fsa.miniproject.entity.RoleEnum;

public class MemberUserDto extends TeamUserDto{
    public MemberUserDto(Integer accountId, String name, String email, RoleEnum role) {
        super(accountId, name, email, role);
    }
}
