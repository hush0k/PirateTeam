package com.hush0k.pirateTeam.auth.service;

import com.hush0k.pirateTeam.pirate.repository.PirateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PirateUserDetailsService implements UserDetailsService {

    private final PirateRepository pirateRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return pirateRepository.findByLogin(login)
                .map(pirate -> new User(
                        pirate.getLogin(),
                        pirate.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_" + pirate.getRank().name()))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Pirate not found: " + login));
    }
}