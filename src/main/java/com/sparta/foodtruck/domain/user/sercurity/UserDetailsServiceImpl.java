package com.sparta.foodtruck.domain.user.sercurity;


import com.sparta.foodtruck.domain.user.dto.LoginRequestDto;
import com.sparta.foodtruck.domain.user.entity.AccountInfo;
import com.sparta.foodtruck.domain.user.entity.User;
import com.sparta.foodtruck.domain.user.repository.AccountInfoRepository;
import com.sparta.foodtruck.domain.user.repository.UserRepository;
import com.sparta.foodtruck.global.dto.ErrorLoginDto;
import com.sparta.foodtruck.global.redis.RedisService;
import com.sparta.foodtruck.global.util.AESUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AESUtil aesUtil;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final AccountInfoRepository accountInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String address) throws UsernameNotFoundException {
        User user = userRepository.findByAddress(address)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + address));
        AccountInfo accountInfo = accountInfoRepository.findById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + address));
        return new UserDetailsImpl(user, accountInfo);
    }

    public UserDetails loadUserByAccountInfo(String username) throws UsernameNotFoundException {
        AccountInfo accountInfo = accountInfoRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
        User user = userRepository.findById(accountInfo.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
        return new UserDetailsImpl(user, accountInfo);
    }

    public String loadUsernameByRedis(String uuid){
        return redisService.getValues(uuid);
    }

    public void saveUsernameByRedis(String uuid, String username){
        redisService.setValues(uuid, username);
    }

    public void saveUsernameByRedisExpireDay(String uuid, String username, Long Day){
        redisService.setValuesByExpireDay(uuid, username, Day);
    }

    public String encryptAES(String uuid){
        return aesUtil.encrypt(uuid);
    }

    public String decryptAES(String encrypt){
        return aesUtil.decrypt(encrypt);
    }
}