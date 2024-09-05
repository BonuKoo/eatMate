package com.eatmate.security.service;

import com.eatmate.dao.repository.account.AccountRepository;
import com.eatmate.domain.entity.user.Account;
import com.eatmate.security.dto.AccountContext;
import com.eatmate.security.dto.AuthenticateAccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsService")
@RequiredArgsConstructor
@Slf4j
public class FormUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Account account = accountRepository.findByEmail(email);


        if (account == null) {
            throw new UsernameNotFoundException("No user found with email: " + email);
        }

        log.info("account:{}",account.getRoles());

        //Security 권한
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(account.getRoles()));

        AuthenticateAccountDto accountDto = AuthenticateAccountDto.builder()
                .id(account.getId())
                .email(account.getEmail())
                .nickname(account.getNickname())
                .password(account.getPassword())
                .files(account.getFiles())
                .build();

        return new AccountContext(accountDto,authorities);
    }
}
