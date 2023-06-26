package za.co.invoke.solutions.naturalgasprices.services;

import za.co.invoke.solutions.naturalgasprices.dto.JwtTokenResponse;
import za.co.invoke.solutions.naturalgasprices.dto.UserDto;
import za.co.invoke.solutions.naturalgasprices.dto.VerifyTokenRequest;
import za.co.invoke.solutions.naturalgasprices.dto.VerifyTokenResponse;

public interface AuthService {

    JwtTokenResponse creatLoginAuthToken(UserDto userDto);
    VerifyTokenResponse verifyJwtToken(VerifyTokenRequest request) throws Exception;
}
