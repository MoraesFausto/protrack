package br.com.edu.alunos.utfpr.protrack.application.services.auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.edu.alunos.utfpr.protrack.application.services.PlatformUserService;
import br.com.edu.alunos.utfpr.protrack.domain.models.PlatformUser;

@Service
public class PlatformUserDetailsService implements UserDetailsService {
    final PlatformUserService platformUserService;

    public PlatformUserDetailsService(final PlatformUserService platformUserService) {
        this.platformUserService = platformUserService;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final PlatformUser usuario = platformUserService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        return User.withUsername(usuario.getUsername()).password(usuario.getPassword()) // senha criptografada
                .authorities(usuario.getAccessLevel().getAccessLevelName()) // ou busque de uma coluna/entidade separada
                .build();
    }
}
