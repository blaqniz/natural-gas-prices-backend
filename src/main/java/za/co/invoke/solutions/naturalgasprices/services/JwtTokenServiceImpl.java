package za.co.invoke.solutions.naturalgasprices.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.invoke.solutions.naturalgasprices.enums.ExceptionConstantsEnum;
import za.co.invoke.solutions.naturalgasprices.dto.*;
import za.co.invoke.solutions.naturalgasprices.entities.JwtToken;
import za.co.invoke.solutions.naturalgasprices.entities.User;
import za.co.invoke.solutions.naturalgasprices.exceptions.GasPriceException;
import za.co.invoke.solutions.naturalgasprices.exceptions.JwtTokenException;
import za.co.invoke.solutions.naturalgasprices.repositories.JwtTokenRepository;
import za.co.invoke.solutions.naturalgasprices.helper.JwtTokenHelper;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import static za.co.invoke.solutions.naturalgasprices.enums.ExceptionConstantsEnum.DB_ERROR_OCCURRED;
import static za.co.invoke.solutions.naturalgasprices.enums.ExceptionConstantsEnum.TOKEN_DECODE_EXCEPTION;

@Slf4j
@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtTokenRepository jwtTokenRepository;
    private final JwtTokenHelper jwtTokenHelper;

    public JwtTokenServiceImpl(JwtTokenRepository jwtTokenRepository, JwtTokenHelper jwtTokenHelper) {

        this.jwtTokenRepository = jwtTokenRepository;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Override
    public JwtTokenResponse createJwtToken(JwtTokenRequest request, User user) {

        try {
            JwtToken jwtToken = jwtTokenHelper.createAuthToken(String.valueOf(user.getId()), user, Arrays.asList("user"));
            jwtTokenRepository.save(jwtToken);

            return JwtTokenResponse.builder()
                    .tokenData(TokenData.builder().token(jwtToken.getToken()).build())
                    .build();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new JwtTokenException(TOKEN_DECODE_EXCEPTION, TOKEN_DECODE_EXCEPTION.getMessage(), TOKEN_DECODE_EXCEPTION.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public VerifyTokenResponse verifyJwtToken(final VerifyTokenRequest verifyTokenRequest) throws GasPriceException {
        final VerifyTokenResponse verifyTokenResponse = jwtTokenHelper.verifyTokenSignatureWithPublicKey(verifyTokenRequest.getToken(), verifyTokenRequest.getPublicKey(), String.valueOf(verifyTokenRequest.getUserId()));

        if (!verifyTokenResponse.isValidToken()) {
            return verifyTokenResponse;
        }

        if (!verifyTokenWithUniqueId(verifyTokenRequest.getToken(), String.valueOf(verifyTokenRequest.getUserId()))) {
            verifyTokenResponse.setNotValidReason(ExceptionConstantsEnum.TOKEN_NEVER_ISSUED_FOR_ID.getMessage());
            verifyTokenResponse.setValidToken(false);
            return verifyTokenResponse;
        }

        verifyTokenResponse.setValidToken(true);
        verifyTokenResponse.setNotValidReason(null);

        return verifyTokenResponse;
    }

    /**
     * This method verifies the token by passed in userId
     *
     * @param jwtToken
     */
    private boolean verifyTokenWithUniqueId(final String jwtToken, final String userId) throws GasPriceException {
        try {
            final JwtToken token = jwtTokenRepository.findByToken(jwtToken)
                    .orElseThrow(() -> new JwtTokenException(DB_ERROR_OCCURRED, DB_ERROR_OCCURRED.getMessage(), DB_ERROR_OCCURRED.getMessage()));

            return String.valueOf(token.getUser().getId()).equals(userId);
        } catch (JwtTokenException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new GasPriceException("An error has occurred: ", e);
        }
    }
}
