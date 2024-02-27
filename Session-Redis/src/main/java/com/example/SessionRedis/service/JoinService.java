package com.example.SessionRedis.service;


import com.example.SessionRedis.dto.JoinDTO;
import com.example.SessionRedis.entity.UserEntity;
import com.example.SessionRedis.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(JoinDTO joinDTO) {

        //db에 이미 동일한 username을 가진 회원이 존재하는지?
        if(userRepository.existsByUsername(joinDTO.getUsername())) {
            return;
        }


        UserEntity newUser = new UserEntity();

        newUser.setUsername(joinDTO.getUsername());
        newUser.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        newUser.setRole("ROLE_ADMIN");

        userRepository.save(newUser);

    }
}
