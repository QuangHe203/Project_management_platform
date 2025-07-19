package fsa.miniproject.controller;

import fsa.miniproject.entity.RoleEnum;
import fsa.miniproject.entity.Task;
import fsa.miniproject.entity.TaskStatusEnum;
import fsa.miniproject.entity.User;
import fsa.miniproject.service.TaskService;
import fsa.miniproject.service.UserService;
import jakarta.transaction.Transactional;
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

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        System.out.println("=== InitBinder được gọi ===");
        binder.registerCustomEditor(User.class, "assignee", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                System.out.println("=== Binding assignee với ID: " + text + " ===");
                Integer userId = Integer.valueOf(text);
                User user = userService.findById(userId).orElse(null);
                setValue(user);
            }
        });
    }


    @PostMapping("/addTask")
    @Transactional
    public String addTask(@Valid @ModelAttribute("task") Task task,
                          BindingResult result,
                          Model model) {
        System.out.println("=== AddTask called ===");
        System.out.println("Task: " + task);
        System.out.println("Has errors: " + result.hasErrors());

        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            model.addAttribute("membersInSameTeam", userService.findUserByRole(RoleEnum.ROLE_MEMBER));
            return "dashboard_manager";
        }

        try {
            taskService.addTask(task);
            System.out.println("Task added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding task: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Không thể thêm tác vụ: " + e.getMessage());
            model.addAttribute("membersInSameTeam", userService.findUserByRole(RoleEnum.ROLE_MEMBER));
            return "dashboard_manager";
        }

        return "redirect:/dashboard_manager";
    }


    @PostMapping("/updateTask")
    public String updateTask(@Valid @ModelAttribute("task") Task task,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findUserByRole(RoleEnum.ROLE_MEMBER));
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
        List<Task> tasks = taskService.getTasksByStatus(status);
        model.addAttribute("tasks", tasks);
        model.addAttribute("users", userService.findUserByRole(RoleEnum.ROLE_MEMBER));
        model.addAttribute("task", new Task());
        model.addAttribute("todoTasks", taskService.getTasksByStatus("TODO"));
        model.addAttribute("inProgressTasks", taskService.getTasksByStatus("IN_PROGRESS"));
        model.addAttribute("doneTasks", taskService.getTasksByStatus("DONE"));
        return "dashboard_manager";
    }
}