package com.eatmate.account.service;

import com.eatmate.dao.mybatis.AccountDao;
import com.eatmate.dao.repository.account.AccountRepository;
import com.eatmate.domain.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountDao accountDao;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    // 이메일로 사용자 조회
    public AccountDto findByEmail(String email){
        return accountDao.findByEmail(email);
    }

    // OAyth2 ID로 사용자 정보 조회
    public AccountDto findByOauth2Id(String oauth2Id) {
        return accountDao.findByOauth2Id(oauth2Id);
    }

    // 새 계정 생성
    public boolean createAccount(AccountDto accountDto) {
        try {
            // 비밀번호를 인코딩하여 저장
            String encodedPassword = passwordEncoder.encode(accountDto.getPassword());
            accountDto.setPassword(encodedPassword);

            int insertedRows = accountDao.insertAccount(accountDto);
            if (insertedRows > 0) {
                System.out.println("새 계정이 성공적으로 생성되었습니다.");
                return true;
            } else {
                System.out.println("계정 생성에 실패했습니다.");
                return false;
            }
        } catch (DataAccessException e) {
            System.err.println("데이터베이스 오류 발생: " + e.getMessage());
            return false;
        }
    }

    // 로그인 한 회원에 oauth2Id, access_token 업데이트
    public boolean updateAccount(AccountDto accountDto) {
        try {
            int updatedRows = accountDao.updateAccount(accountDto);
            if (updatedRows > 0) {
                System.out.println("카카오 정보가 성공적으로 업데이트되었습니다.");
                return true;
            } else {
                System.out.println("카카오 정보 업데이트에 실패했습니다.");
                return false;
            }
        } catch (DataAccessException e) {
            System.err.println("데이터베이스 오류 발생: " + e.getMessage());
            return false;
        }
    }

    public void updateDetailAccount(AccountDto dto) {
        // 비밀번호를 인코딩하여 업데이트
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            dto.setPassword(encodedPassword);
        }
        accountDao.updateDetailAccount(dto);
    }

    // OAuth2 ID를 기반으로 회원 탈퇴 처리
    public void deleteUserByOauth2Id(String oauth2Id) {
        accountDao.deleteByOauth2Id(oauth2Id);
    }


    /*
    @Transactional
    void createAccountMyBatis(){
        accountDao.join();
    }

    @Transactional
    void createAccountRepository(){
        accountRepository.save();
    }
    */

}
