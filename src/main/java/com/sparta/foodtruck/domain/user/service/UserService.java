package com.sparta.foodtruck.domain.user.service;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.foodtruck.domain.user.dto.AddressSameRequestDto;
import com.sparta.foodtruck.domain.user.dto.SignupRequestDto;
import com.sparta.foodtruck.domain.user.dto.UsernameResponseDto;
import com.sparta.foodtruck.domain.user.dto.UsernameVaildRequestDto;
import com.sparta.foodtruck.domain.user.entity.AccountInfo;
import com.sparta.foodtruck.domain.user.entity.QUser;
import com.sparta.foodtruck.domain.user.entity.User;
import com.sparta.foodtruck.domain.user.repository.AccountInfoRepository;
import com.sparta.foodtruck.domain.user.repository.UserRepository;
import com.sparta.foodtruck.global.dto.CustomStatusResponseDto;
import com.sparta.foodtruck.global.exception.CustomStatusException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.foodtruck.domain.user.entity.QUser.user;

@Service
@Slf4j(topic = "UserService")
@RequiredArgsConstructor
public class UserService {

    private final JPAQueryFactory queryFactory;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountInfoRepository accountInfoRepository;

    @Transactional
    public ResponseEntity<CustomStatusResponseDto> createAccount(SignupRequestDto requestDto) {
        String address = requestDto.getAddress();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String username = requestDto.getUsername();
        CustomStatusResponseDto responseDto = new CustomStatusResponseDto(true);

        // 회원 중복 확인
        Optional<User> checkAddress = userRepository.findByAddress(address);
        if (checkAddress.isPresent()) {
            throw CustomStatusException.builder("Same Id").status(409).build();
        }

        Optional<AccountInfo> checkUsername = accountInfoRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw CustomStatusException.builder("Same Username").status(409).build();
        }

        User newUser = new User(address, password);
        newUser = userRepository.save(newUser);
        log.info("newUser.getId().toString() = " + newUser.getId().toString());
        AccountInfo accountInfo = new AccountInfo(newUser.getId(), username);
        accountInfoRepository.save(accountInfo);

        return ResponseEntity.status(201).body(responseDto);
    }
    public ResponseEntity<CustomStatusResponseDto> usernameCheck(UsernameVaildRequestDto requestDto){
        Optional<AccountInfo> checkUsername = accountInfoRepository.findByUsername(requestDto.getUsername());

        if (checkUsername.isPresent()) {
            throw CustomStatusException.builder("Same Username").status(409).build();
        }

        return ResponseEntity.ok(new CustomStatusResponseDto(true));
    }

    public ResponseEntity<CustomStatusResponseDto> addressCheck(AddressSameRequestDto requestDto) {
//        Optional<User> checkAddress = userRepository.findByAddress(requestDto.getAddress());
        Long userId = queryFactory.select(user.id).from(user).where(user.address.eq(requestDto.getAddress())).fetchFirst();
        if (userId != null) {
            throw CustomStatusException.builder("Same Id").status(409).build();
        }

        return ResponseEntity.ok(new CustomStatusResponseDto(true));
    }
}
