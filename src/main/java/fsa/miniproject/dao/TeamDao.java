package fsa.miniproject.dao;

import fsa.miniproject.entity.Team;
import fsa.miniproject.entity.User;

import java.util.Optional;

public interface TeamDao {
    Optional<Team> findById(Integer teamId);
    boolean save(Team team);
}
