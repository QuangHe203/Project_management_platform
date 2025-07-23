package fsa.miniproject.controller;

import fsa.miniproject.dto.DetailUserDto;
import fsa.miniproject.dto.UpdateTaskDto;
import fsa.miniproject.enums.RoleEnum;
import fsa.miniproject.dto.DetailTaskDto;
import fsa.miniproject.entity.Task;
import fsa.miniproject.enums.TaskStatusEnum;
import fsa.miniproject.entity.User;
import fsa.miniproject.service.TaskService;
import fsa.miniproject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Optional;

@Controller
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(User.class, "assignee", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                Integer userId = Integer.valueOf(text);
                User user = userService.findById(userId).orElse(null);
                setValue(user);
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

    @PostMapping("/updateTask/{taskId}")
    public String updateTask(@PathVariable("taskId") Integer taskId,
                             @Valid @ModelAttribute("task") UpdateTaskDto task,
                             BindingResult result,
                             Model model) {
        try {
            taskService.updateFromDto(task);
        } catch (Exception e) {
            model.addAttribute("membersInSameTeam", userService.findUsersByRole(RoleEnum.ROLE_MEMBER));
            model.addAttribute("task", task);
            model.addAttribute("error", "Không thể sửa tác vụ: " + e.getMessage());
            return "dashboard_manager";
        }
        return "redirect:/dashboard_manager";
    }

    @GetMapping("/getTask/{taskId}")
    @ResponseBody
    public DetailTaskDto getTaskById(@PathVariable("taskId") Integer taskId) {
        Optional<DetailTaskDto> taskDto = taskService.getById(taskId);
        if (taskDto.isPresent()) {
            return taskDto.get();
        } else {
            throw new IllegalArgumentException("Task not found with ID: " + taskId); // Nếu không tìm thấy task
        }
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

    @GetMapping("/tasks/byStatus")
    public String getTasksByStatus(@RequestParam String status, Model model) {
        List<DetailTaskDto> tasks = taskService.getTasksByStatusWithDetails(status);

        model.addAttribute("tasks", tasks);
        model.addAttribute("users", userService.findUsersByRole(RoleEnum.ROLE_MEMBER));
        model.addAttribute("task", new DetailTaskDto());

        model.addAttribute("todoTasks", taskService.getTasksByStatusWithDetails("TODO"));
        model.addAttribute("inProgressTasks", taskService.getTasksByStatusWithDetails("IN_PROGRESS"));
        model.addAttribute("doneTasks", taskService.getTasksByStatusWithDetails("DONE"));

        return "dashboard_manager";
    }

    @PostMapping("/deleteTask")
    public String deleteTask(@RequestParam("taskId") Integer taskId, RedirectAttributes redirectAttributes) {
        boolean deleted = taskService.deleteTask(taskId);
        if (deleted) {
            redirectAttributes.addFlashAttribute("success", "Xoá tác vụ thành công.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Tác vụ không tồn tại hoặc đã bị xoá.");
        }
        return "redirect:/dashboard_manager"; // hoặc trang danh sách task của bạn
    }
}
