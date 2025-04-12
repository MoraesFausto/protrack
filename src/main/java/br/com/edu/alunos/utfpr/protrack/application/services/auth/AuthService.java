package br.com.edu.alunos.utfpr.protrack.application.services.auth;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import br.com.edu.alunos.utfpr.protrack.application.services.PlatformUserService;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.auth.LoginRequest;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.platformusers.CreateUserDTO;
import br.com.edu.alunos.utfpr.protrack.security.JwtTokenProvider;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PlatformUserService platformUserService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final AuthenticationManager authenticationManager, final PlatformUserService platformUserService,
            final JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.platformUserService = platformUserService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Map<String, String> login(final LoginRequest request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        final UserDetails user = (UserDetails) authentication.getPrincipal();
        final String token = jwtTokenProvider.generateToken(user.getUsername(), new HashMap<>());

        final Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }

    public Map<String, String> register(final LoginRequest request) {
        final CreateUserDTO createUserDTO = new CreateUserDTO(request.username(), request.password());
        this.platformUserService.create(createUserDTO);
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        final UserDetails user = (UserDetails) authentication.getPrincipal();
        final String token = jwtTokenProvider.generateToken(user.getUsername(), new HashMap<>());
        final Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }
}
