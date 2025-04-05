package br.com.edu.alunos.utfpr.protrack.domain.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.edu.alunos.utfpr.protrack.domain.enums.RoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum roleEnum;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @ManyToMany(mappedBy = "employees")
    private List<Team> teams;

}

