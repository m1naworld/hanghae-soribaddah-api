package com.sparta.soundsea.user.service;


import com.sparta.soundsea.user.dto.SignUpUserDto;
import com.sparta.soundsea.user.entity.User;
import com.sparta.soundsea.user.mapper.UserMapper;
import com.sparta.soundsea.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.sparta.soundsea.common.exception.ExceptionMessage.DUPLICATE_USER_ERROR_MSG;
import static com.sparta.soundsea.common.exception.ExceptionMessage.INVALID_LOGINID_MSG;
import static com.sparta.soundsea.common.exception.ExceptionMessage.INVALID_PASSWORD_MSG;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final Validator validator;
    private final UserMapper userMapper;

    @Transactional
    public void signUp(SignUpUserDto signUpUserDto) {
        // 1. LoginId 확인
        // 1-1. LoginId 형식 확인
        if (!validator.isValidMemberName(signUpUserDto.getLoginId())){
            throw new IllegalArgumentException(INVALID_LOGINID_MSG.getMsg());
        }
        // 1-2. LoginId 중복 확인
        userRepository.findByLoginId(signUpUserDto.getLoginId())
                .ifPresent( m-> {
                    throw new IllegalArgumentException(DUPLICATE_USER_ERROR_MSG.getMsg());
                });

        // 2. Password 확인
        // 2-1. Password 형식 확인
        if (!validator.isValidPassword(signUpUserDto.getPassword())){
            throw new IllegalArgumentException(INVALID_PASSWORD_MSG.getMsg());
        }

        // 3. Entity 생성 및 DB 저장
        User user = userMapper.toEntity(signUpUserDto);
        userRepository.save(user);
    }
}
