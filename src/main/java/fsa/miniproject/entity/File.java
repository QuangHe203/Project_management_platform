package fsa.miniproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "files")
@AllArgsConstructor
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Integer fileId;

    @Column(name = "file_name", columnDefinition = "NVARCHAR(255)")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;
    private Integer size;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
