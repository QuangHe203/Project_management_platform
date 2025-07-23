package fsa.miniproject.entity;

import fsa.miniproject.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer accountId;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
    private Set<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
