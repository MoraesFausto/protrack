package br.com.edu.alunos.utfpr.protrack.application.services;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.edu.alunos.utfpr.protrack.application.services.generic.GenericServiceImpl;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.platformusers.CreateUserDTO;
import br.com.edu.alunos.utfpr.protrack.domain.enums.AccessLevelEnum;
import br.com.edu.alunos.utfpr.protrack.domain.models.PlatformUser;
import br.com.edu.alunos.utfpr.protrack.infrastructure.repositories.PlatformUserRepository;

@Service
public class PlatformUserService extends GenericServiceImpl<PlatformUser, Long> {
    private final PasswordEncoder passwordEncoder;

    public PlatformUserService(final PlatformUserRepository repository) {
        super(repository);
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public PlatformUser create(final CreateUserDTO createUserDTO) {
        final PlatformUser platformUser = new PlatformUser();
        platformUser.setUsername(createUserDTO.username());
        platformUser.setPassword(this.passwordEncoder.encode(createUserDTO.password()));
        platformUser.setAccessLevel(AccessLevelEnum.SIMPLE);
        return this.save(platformUser);
    }

    public Optional<PlatformUser> findByUsername(final String username) {
        final PlatformUser platformUser = new PlatformUser();
        platformUser.setUsername(username);
        final Example<PlatformUser> example = Example.of(platformUser);
        return this.repository.findOne(example);
    }
}
