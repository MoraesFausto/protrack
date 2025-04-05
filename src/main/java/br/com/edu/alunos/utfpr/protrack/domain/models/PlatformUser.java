package br.com.edu.alunos.utfpr.protrack.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.edu.alunos.utfpr.protrack.domain.enums.AccessLevelEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class PlatformUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessLevelEnum accessLevel;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    private String password;

}
