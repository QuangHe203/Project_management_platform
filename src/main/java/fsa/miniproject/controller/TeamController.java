package fsa.miniproject.controller;

import fsa.miniproject.dto.TeamUserDto;
import fsa.miniproject.service.CustomUserDetails;
import fsa.miniproject.service.TeamService;
import fsa.miniproject.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/teams")
@PreAuthorize("hasRole('ROLE_MANAGER')")
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;

    public TeamController(TeamService teamService, UserService userService) {
        this.teamService = teamService;
        this.userService = userService;
    }

    @PostMapping("/members/add")
    public String addMemberToTeam(@RequestParam(value = "userId") Integer userId, Model model) {
        CustomUserDetails loggedInUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Integer teamId = loggedInUser.getTeamId();
        if (teamId == null) {
            model.addAttribute("error", "Người dùng không thuộc nhóm nào.");
            return "dashboard_manager";
        }
        teamService.addMember(teamId, userId);
        return "redirect:/dashboard_manager";
    }

    @PostMapping("/members/remove")
    public String removeMemberFromTeam(@RequestParam(value = "userId") Integer userId, Model model) {
        CustomUserDetails loggedInUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Integer teamId = loggedInUser.getTeamId();
        if (teamId == null) {
            model.addAttribute("error", "Người dùng không thuộc nhóm nào.");
            return "dashboard_manager";
        }
        teamService.removeMember(teamId, userId);
        return "redirect:/dashboard_manager";
    }

    @GetMapping("/members")
    public String getTeamMembers(Model model) {
        try {
            CustomUserDetails loggedInUser = (CustomUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            Integer teamId = loggedInUser.getTeamId();
            if (teamId == null) {
                model.addAttribute("error", "Người dùng không thuộc nhóm nào.");
                return "dashboard_manager";
            }
            List<TeamUserDto> teamMembers = userService.findUsersByTeamId(teamId);
            model.addAttribute("users", teamMembers);
            return "dashboard_manager";
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi trên server: " + e.getMessage());
            return "dashboard_manager";
        }
    }
}