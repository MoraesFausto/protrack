package br.com.edu.alunos.utfpr.protrack.application.services;

import org.springframework.stereotype.Service;

import br.com.edu.alunos.utfpr.protrack.application.services.generic.GenericServiceImpl;
import br.com.edu.alunos.utfpr.protrack.domain.models.PlatformUser;
import br.com.edu.alunos.utfpr.protrack.infraestructure.repositories.PlatformUserRepository;

@Service
public class PlatformUserService extends GenericServiceImpl<PlatformUser, Long> {
    public PlatformUserService(final PlatformUserRepository repository) {
        super(repository);
    }
}
