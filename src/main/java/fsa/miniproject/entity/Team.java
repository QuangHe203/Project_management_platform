package fsa.miniproject.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "teams")
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Integer teamId;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<User> members;

}