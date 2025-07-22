package fsa.miniproject.controller;

import fsa.miniproject.entity.RoleEnum;
import fsa.miniproject.dto.TaskDto;
import fsa.miniproject.entity.Task;
import fsa.miniproject.entity.TaskStatusEnum;
import fsa.miniproject.entity.User;
import fsa.miniproject.service.TaskService;
import fsa.miniproject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.util.List;

@Controller
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        System.out.println("=== InitBinder được gọi ===");
//        binder.registerCustomEditor(User.class, "assignee", new PropertyEditorSupport() {
//            @Override
//            public void setAsText(String text) {
//                System.out.println("=== Binding assignee với ID: " + text + " ===");
//                Integer userId = Integer.valueOf(text);
//                User user = userService.findById(userId).orElse(null);
//                setValue(user);
//            }
//        });
//    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        System.out.println("=== InitBinder được gọi ===");
        binder.registerCustomEditor(User.class, "assignee", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                System.out.println("=== Binding assignee với ID: " + text + " ===");
                if (text == null || text.trim().isEmpty()) {
                    setValue(null); // Gán null nếu người dùng không chọn assignee
                    return;
                }

                try {
                    Integer userId = Integer.valueOf(text);
                    User user = userService.findById(userId).orElse(null);
                    setValue(user);
                } catch (NumberFormatException e) {
                    System.err.println("Không thể parse userId từ text: " + text);
                    setValue(null); // Hoặc throw nếu muốn fail hard
                }
            }
        });
    }

    @PostMapping("/addTask")
    public String addTask(@Valid @ModelAttribute("task") Task task,
                          BindingResult result,
                          Model model) {
        System.out.println("=== AddTask called ===");
        System.out.println("Task title: " + task.getTitle());
        System.out.println("Assignee: " + (task.getAssignee() != null ? task.getAssignee().getName() : "null"));
        System.out.println("Status: " + task.getStatus());
        System.out.println("Has errors: " + result.hasErrors());

        // ⚠️ Nếu không có giá trị status từ form (ví dụ bị thiếu input hidden), gán mặc định
        if (task.getStatus() == null) {
            task.setStatus(TaskStatusEnum.BACKLOG);
        }

        if (result.hasErrors()) {
            model.addAttribute("membersInSameTeam", userService.findUsersByRole(RoleEnum.ROLE_MEMBER));
            model.addAttribute("task", task); // Giữ lại dữ liệu đã nhập
            return "dashboard_manager";
        }

        try {
            taskService.addTask(task);
        } catch (Exception e) {
            model.addAttribute("error", "Không thể thêm tác vụ: " + e.getMessage());
            model.addAttribute("membersInSameTeam", userService.findUsersByRole(RoleEnum.ROLE_MEMBER));
            model.addAttribute("task", task); // Giữ lại dữ liệu đã nhập
            return "dashboard_manager";
        }

        return "redirect:/dashboard_manager";
    }


    @PostMapping("/updateTask")
    public String updateTask(@Valid @ModelAttribute("task") Task task,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findUsersByRole(RoleEnum.ROLE_MEMBER));
            model.addAttribute("task", task); // Giữ lại dữ liệu đã nhập
            return "dashboard_manager";
        }
        taskService.updateTask(task);
        return "redirect:/dashboard_manager";
    }

    @PostMapping("/updateTaskStatus")
    public String updateTaskStatus(@RequestParam Integer taskId, @RequestParam TaskStatusEnum status) {
        boolean success = taskService.updateTaskStatus(taskId, status);
        return "redirect:/dashboard_manager" + (success ? "" : "?error=TaskNotFound");
    }

    @GetMapping("/tasks/byStatus")
    public String getTasksByStatus(@RequestParam String status, Model model) {
        // 👉 Dùng DTO để tránh lazy/concurrent modification
        List<TaskDto> tasks = taskService.getTasksByStatusWithDetails(status);

        model.addAttribute("tasks", tasks);
        model.addAttribute("users", userService.findUsersByRole(RoleEnum.ROLE_MEMBER));
        model.addAttribute("task", new TaskDto());

        model.addAttribute("todoTasks", taskService.getTasksByStatusWithDetails("TODO"));
        model.addAttribute("inProgressTasks", taskService.getTasksByStatusWithDetails("IN_PROGRESS"));
        model.addAttribute("doneTasks", taskService.getTasksByStatusWithDetails("DONE"));

        return "dashboard_manager";
    }
}
