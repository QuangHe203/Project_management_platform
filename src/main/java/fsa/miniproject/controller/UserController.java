package fsa.miniproject.controller;

import fsa.miniproject.dto.MemberUserDto;
import fsa.miniproject.dto.RegisterUserDto;
import fsa.miniproject.dto.DetailUserDto;
import fsa.miniproject.dto.TeamUserDto;
import fsa.miniproject.entity.RoleEnum;
import fsa.miniproject.entity.Task;
import fsa.miniproject.service.TaskService;
import fsa.miniproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private TaskService taskService;

    @GetMapping("/login")
    public String showLoginForm(Model model,
                               @RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               @RequestParam(value = "expired", required = false) String expired) {

        if (error != null) {
            model.addAttribute("error", "Email hoặc mật khẩu không đúng!");
        }

        if (logout != null) {
            model.addAttribute("message", "Bạn đã đăng xuất thành công!");
        }

        if (expired != null) {
            model.addAttribute("message", "Phiên làm việc đã hết hạn, vui lòng đăng nhập lại!");
        }
        
        model.addAttribute("user", new RegisterUserDto());
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        System.out.println("Session ID in dashboard: " + session.getId());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication in dashboard: " + auth);
        
        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
            return "redirect:/dashboard_manager";
        } else if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/dashboard_admin";
        } else {
            return "redirect:/dashboard_member";
        }
    }

    @GetMapping("/dashboard_manager")
    public String dashboardManager(Model model, HttpSession session) {
        System.out.println("Session ID in dashboard_manager: " + session.getId());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication in dashboard_manager: " + auth);

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login?expired=true";
        }

        try {
            if (taskService == null) {
                System.out.println("TaskService is null!");
                model.addAttribute("error", "TaskService không được khởi tạo");
                return "error";
            }
            if (userService == null) {
                System.out.println("UserService is null!");
                model.addAttribute("error", "UserService không được khởi tạo");
                return "error";
            }
            String username = auth.getName();
            Optional<DetailUserDto> currentUser = userService.findDetailByEmail(username);
            if (!currentUser.isPresent()) {
                model.addAttribute("error", "Không tìm thấy thông tin người dùng");
                return "error";
            }
            Integer teamId = currentUser.get().getTeamId() != null ? currentUser.get().getTeamId() : null;
            if (teamId == null) {
                model.addAttribute("error", "Bạn chưa thuộc nhóm nào");
                model.addAttribute("tasks", new ArrayList<>());
                model.addAttribute("usersInSameTeam", new ArrayList<>());
                model.addAttribute("allUsersMembers", new ArrayList<>());
                model.addAttribute("task", new Task());
                model.addAttribute("username", username);
                return "dashboard_manager";
            }
            // Lấy danh sách task của team hiện tại
            List<Task> tasks = taskService.getTasksByTeamId(teamId);
            // Lấy danh sách user của team hiện tại
            List<TeamUserDto> usersInSameTeam = userService.findUsersByTeamId(teamId);
            for (TeamUserDto user : usersInSameTeam) {
                System.out.println("User in team: " + user.getName() + ", Email: " + user.getEmail());
            }
            // Lấy danh sách user có vai trò là member
            List<MemberUserDto> allUsersMembers = userService.findUsersByRole(RoleEnum.ROLE_MEMBER);

            model.addAttribute("tasks", tasks != null ? tasks : new ArrayList<>());
            model.addAttribute("usersInSameTeam", usersInSameTeam != null ? usersInSameTeam : new ArrayList<>());
            model.addAttribute("allUsersMembers", allUsersMembers != null ? allUsersMembers : new ArrayList<>());
            model.addAttribute("task", new Task());
            model.addAttribute("username", username);
        } catch (Exception e) {
            System.out.println("Error in dashboard_manager: " + e.getMessage());
            model.addAttribute("error", "Không thể tải dữ liệu: " + e.getMessage());
            model.addAttribute("tasks", new ArrayList<>());
            model.addAttribute("usersInSameTeam", new ArrayList<>());
            model.addAttribute("allUsersMembers", new ArrayList<>());
            model.addAttribute("task", new Task());
            model.addAttribute("username", "");
            return "dashboard_manager";
        }

        return "dashboard_manager";
    }


    @GetMapping("/dashboard_admin")
    public String dashboardAdmin(Model model, HttpSession session) {
        System.out.println("Session ID in dashboard_admin: " + session.getId());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication in dashboard_admin: " + auth);
        
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login?expired=true";
        }
        
        model.addAttribute("username", auth.getName());
        return "dashboard_admin";
    }

    @GetMapping("/dashboard_member")
    public String dashboardMember(Model model, HttpSession session) {
        System.out.println("Session ID in dashboard_member: " + session.getId());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication in dashboard_member: " + auth);
        
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login?expired=true";
        }
        
        model.addAttribute("username", auth.getName());
        return "dashboard_member";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("user") RegisterUserDto userDto,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            userService.registerUser(userDto);
            model.addAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "login";
        } catch (Exception ex) {
            model.addAttribute("error", "Đã xảy ra lỗi: " + ex.getMessage());
            return "login";
        }
    }
}
