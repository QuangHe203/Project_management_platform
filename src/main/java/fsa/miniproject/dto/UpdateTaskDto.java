package fsa.miniproject.dto;

import fsa.miniproject.enums.RoleEnum;
import fsa.miniproject.enums.TaskStatusEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateTaskDto {
    private Integer taskId;
    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer assignee;

    // Constructor mặc định (cần có để Spring khởi tạo)
    public UpdateTaskDto() {}

    // Constructor đầy đủ nếu cần
    public UpdateTaskDto(Integer taskId, String title, String content,
                         LocalDate startDate, LocalDate endDate, Integer assignee) {
        this.taskId = taskId;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assignee = assignee;
    }
}