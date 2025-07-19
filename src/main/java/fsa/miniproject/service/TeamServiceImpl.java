package fsa.miniproject.service;

import fsa.miniproject.dao.TeamDao;
import fsa.miniproject.dao.UserDao;
import fsa.miniproject.dto.UserDto;
import fsa.miniproject.entity.Team;
import fsa.miniproject.entity.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamDao teamDao;
    private final UserDao userDao;

    public TeamServiceImpl(TeamDao teamDao, UserDao userDao) {
        this.teamDao = teamDao;
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public boolean addMember(Integer teamId, Integer userId) {
        // Lấy team từ database
        Team team = teamDao.findById(teamId).orElseThrow(() ->
                new IllegalArgumentException("Team không tồn tại với ID: " + teamId));

        // Lấy user từ database
        User user = userDao.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User không tồn tại với ID: " + userId));

        // Gán team cho user và lưu user
        user.setTeam(team);
        userDao.save(user);
        return true;
    }


    @Override
    @Transactional
    public boolean removeMember(Integer teamId, Integer userId) {
        Team team = teamDao.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Team not found"));
        User user = userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!team.getMembers().remove(user)) {
            throw new IllegalStateException("User is not a part of this team");
        }

        user.setTeam(null);
        teamDao.save(team); // Cập nhật ở team
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getMembers(Integer teamId) {
        Team team = teamDao.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Team not found"));

        List<User> members = team.getMembers();

        return members.stream()
                .map(member -> new UserDto(member.getAccountId(), member.getName(), member.getEmail()))
                .collect(Collectors.toList());
    }
}
