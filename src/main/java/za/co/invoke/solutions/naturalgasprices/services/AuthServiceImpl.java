package za.co.invoke.solutions.naturalgasprices.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import za.co.invoke.solutions.naturalgasprices.dto.*;
import za.co.invoke.solutions.naturalgasprices.entities.User;
import za.co.invoke.solutions.naturalgasprices.exceptions.InvalidLoginCredentials;
import za.co.invoke.solutions.naturalgasprices.repositories.UserRepository;

import static za.co.invoke.solutions.naturalgasprices.constants.GasPriceConstants.LOGIN_FAILED;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;

    public AuthServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, JwtTokenService jwtTokenService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
    }


    @Override
    public JwtTokenResponse creatLoginAuthToken(UserDto userDto) {
        try {
            final String username = userDto.getUsername();
            final User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new InvalidLoginCredentials(LOGIN_FAILED));

            boolean isLoginSuccessful = bCryptPasswordEncoder.matches(userDto.getPassword(), user.getPassword());

            if (isLoginSuccessful) {
                final JwtTokenRequest jwtTokenRequest = JwtTokenRequest.builder().username(username).build();
                JwtTokenResponse jwtTokenResponse = jwtTokenService.createJwtToken(jwtTokenRequest, user);
                log.info("Token generated successfully: {}", jwtTokenResponse.getTokenData().getToken());
                return jwtTokenResponse;
            } else {
                throw new InvalidLoginCredentials(LOGIN_FAILED);
            }
        } catch (InvalidLoginCredentials e) {
            log.error(LOGIN_FAILED, e);
            throw e;
        } catch (Exception e) {
            log.error("An error has occurred {}", e);
            throw e;
        }
    }

    public VerifyTokenResponse verifyJwtToken(VerifyTokenRequest request) throws Exception {
        try {
            return jwtTokenService.verifyJwtToken(request);
        } catch (Exception e) {
            log.error("An error has occurred", e.getMessage());
            throw e;
        }
    }
}
