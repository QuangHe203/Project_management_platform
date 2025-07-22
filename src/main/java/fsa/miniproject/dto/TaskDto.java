package fsa.miniproject.dto;

import fsa.miniproject.entity.TaskStatusEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDto {
    private Integer taskId;
    private String title;
    private String content;
    private TaskStatusEnum status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String assigneeName;
    private Integer assigneeId;
//    private String projectName;
//    private Integer projectId;

    public TaskDto() {}

    public TaskDto(Integer taskId, String title, String content, TaskStatusEnum status,
                   LocalDate startDate, LocalDate endDate,
                   Integer assigneeId, String assigneeName) {
        this.taskId = taskId;
        this.title = title;
        this.content = content;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assigneeId = assigneeId;
        this.assigneeName = assigneeName;
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

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

}

