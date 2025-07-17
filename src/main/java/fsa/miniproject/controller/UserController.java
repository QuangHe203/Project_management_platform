package fsa.miniproject.controller;

import fsa.miniproject.dto.RegisterUserDto;
import fsa.miniproject.entity.RoleEnum;
import fsa.miniproject.entity.Task;
import fsa.miniproject.entity.User;
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
            // Kiểm tra service có null không
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
            
            List<Task> tasks = taskService.getAllTasks();
            System.out.println("Tasks loaded: " + (tasks != null ? tasks.size() : "null"));
            
            List<User> members = userService.findUserByRole(RoleEnum.ROLE_MEMBER);
            System.out.println("Members loaded: " + (members != null ? members.size() : "null"));
            
            model.addAttribute("tasks", tasks != null ? tasks : new ArrayList<>());
            model.addAttribute("task", new Task());
            model.addAttribute("users", members != null ? members : new ArrayList<>());
            model.addAttribute("username", auth.getName());
            
        } catch (Exception e) {
            System.out.println("Error in dashboard_manager: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Không thể tải dữ liệu: " + e.getMessage());
            return "error";
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
