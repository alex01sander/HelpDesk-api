package com.alex.helpdesk.security;

import com.alex.helpdesk.repository.TecnicoRepository;
import com.alex.helpdesk.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final TecnicoRepository tecnicoRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository,
                                    TecnicoRepository tecnicoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {      
        return usuarioRepository.findByEmail(email)
                .map(u -> (UserDetails) u)
                .or(() -> tecnicoRepository.findByEmail(email).map(t -> (UserDetails) t))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));
    }
}
