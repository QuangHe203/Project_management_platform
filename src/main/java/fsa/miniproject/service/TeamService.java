package fsa.miniproject.service;

import fsa.miniproject.dto.UserDto;

import java.util.List;

public interface TeamService {
    boolean addMember(Integer teamId, Integer userId);
    boolean removeMember(Integer teamId, Integer userId);
    List<UserDto> getMembers(Integer teamId);
}
