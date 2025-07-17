package fsa.miniproject.controller;

import fsa.miniproject.entity.RoleEnum;
import fsa.miniproject.entity.Task;
import fsa.miniproject.entity.TaskStatusEnum;
import fsa.miniproject.service.TaskService;
import fsa.miniproject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping("/addTask")
    public String addTask(@Valid @ModelAttribute("task") Task task,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findUserByRole(RoleEnum.ROLE_MEMBER));
            return "dashboard_manager";
        }
        taskService.addTask(task);
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