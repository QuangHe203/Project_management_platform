package fsa.miniproject.dto;

import fsa.miniproject.enums.RoleEnum;
import fsa.miniproject.enums.TaskStatusEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DetailTaskDto {
    private Integer taskId;
    private String title;
    private String content;
    private TaskStatusEnum status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer assigneeId;
    private String assigneeName;
    private RoleEnum assigneeRole;
//    private String projectName;
//    private Integer projectId;

    public DetailTaskDto() {}

    public DetailTaskDto(Integer taskId, String title, String content, TaskStatusEnum status,
                         LocalDate startDate, LocalDate endDate,
                         Integer assigneeId, String assigneeName, RoleEnum assigneeRole) {
        this.taskId = taskId;
        this.title = title;
        this.content = content;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assigneeId = assigneeId;
        this.assigneeName = assigneeName;
        this.assigneeRole = assigneeRole;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TaskStatusEnum status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

    public void setAssignee(Integer integer) {

    }
}

