package br.com.edu.alunos.utfpr.protrack.infrastructure.repositories;


import java.util.Optional;

import br.com.edu.alunos.utfpr.protrack.domain.models.PlatformUser;
import br.com.edu.alunos.utfpr.protrack.infrastructure.repositories.generic.GenericRepository;

public interface PlatformUserRepository extends GenericRepository<PlatformUser, Long> {
}
