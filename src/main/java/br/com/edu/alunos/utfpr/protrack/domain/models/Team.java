package br.com.edu.alunos.utfpr.protrack.domain.models;

import br.com.edu.alunos.utfpr.protrack.domain.enums.TeamEndEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToMany
    @JoinTable(
            name = "team_employee",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    @JsonBackReference
    private List<Employee> employees;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamEndEnum teamEndEnum;

    @OneToMany(mappedBy = "team")
    private List<Project> projects;
}

