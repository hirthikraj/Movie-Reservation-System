package com.project.mrs.service.auth;

import com.project.mrs.dto.user.UserRequestDTO;
import com.project.mrs.entity.User;
import com.project.mrs.exception.UserConflictException;
import com.project.mrs.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTService jwtService;

    AuthenticationService(UserService userService,JWTService jwtService,BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.userService = userService;
        this.jwtService = jwtService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String signUpUser(UserRequestDTO userRequestDTO) {
        if(userService.isUserPresentByUserNameOrUserEmail(userRequestDTO.getUsername(),userRequestDTO.getUserEmail()))
        {
            throw new UserConflictException("User with the same username or email already exists", HttpStatus.CONFLICT);
        }
        encodePassword(userRequestDTO);
        User newUser = userService.createNewUser(userRequestDTO);
        return jwtService.generateJWTToken(newUser);
    }

    public void encodePassword(UserRequestDTO userRequestDTO)
    {
        userRequestDTO.setPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPassword()));
    }

    public String generateTokenForUser(String userName) {
        User user = userService.getUserByUserName(userName);
        return jwtService.generateJWTToken(user);
    }
}
