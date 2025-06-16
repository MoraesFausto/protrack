package br.com.edu.alunos.utfpr.protrack.infrastructure.repositories;

import br.com.edu.alunos.utfpr.protrack.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
