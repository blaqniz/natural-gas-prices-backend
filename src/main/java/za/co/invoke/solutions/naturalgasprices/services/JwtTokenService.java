package za.co.invoke.solutions.naturalgasprices.services;

import za.co.invoke.solutions.naturalgasprices.dto.JwtTokenRequest;
import za.co.invoke.solutions.naturalgasprices.dto.JwtTokenResponse;
import za.co.invoke.solutions.naturalgasprices.dto.VerifyTokenRequest;
import za.co.invoke.solutions.naturalgasprices.dto.VerifyTokenResponse;
import za.co.invoke.solutions.naturalgasprices.entities.User;

public interface JwtTokenService {

    JwtTokenResponse createJwtToken(final JwtTokenRequest request, User user);

    VerifyTokenResponse verifyJwtToken(final VerifyTokenRequest verifyTokenRequest) throws Exception;

    //
//    ExtendTokenResponse extendJwtToken(final VerifyTokenRequest verifyTokenRequest) throws Exception;
//
//    ExpireTokenResponse expireJwtToken(final VerifyTokenRequest verifyTokenRequest) throws Exception;
//
//    PublicPrivateKeyPair generateKeyPair() throws Exception;
}
