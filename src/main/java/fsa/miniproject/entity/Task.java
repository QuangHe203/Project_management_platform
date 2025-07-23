package fsa.miniproject.entity;

import fsa.miniproject.enums.TaskStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskId;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String title;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String content;

    @Enumerated(EnumType.STRING)
    private TaskStatusEnum status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

//    @ManyToOne
//    @JoinColumn(name = "project_id")
//    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")  // Tên cột của khóa ngoại đến bảng "users"
    private User assignee;

//    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
//    private Set<Comment> comments;
//
//    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
//    private Set<File> files;
}
