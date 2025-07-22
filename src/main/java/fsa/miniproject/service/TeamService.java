package fsa.miniproject.service;

public interface TeamService {
    boolean addMember(Integer teamId, Integer userId);
    boolean removeMember(Integer teamId, Integer userId);
}
