package za.co.invoke.solutions.naturalgasprices.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import za.co.invoke.solutions.naturalgasprices.constants.GasPriceConstants;
import za.co.invoke.solutions.naturalgasprices.dto.DecodedToken;
import za.co.invoke.solutions.naturalgasprices.dto.VerifyTokenRequest;
import za.co.invoke.solutions.naturalgasprices.dto.VerifyTokenResponse;
import za.co.invoke.solutions.naturalgasprices.exceptions.GasPriceException;
import za.co.invoke.solutions.naturalgasprices.services.JwtTokenService;
import za.co.invoke.solutions.naturalgasprices.util.TokenDecoder;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Order(1)
@Slf4j
@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private final String apiKey;
    private final String publicKey;
    private final JwtTokenService jwtTokenService;

    public JwtAuthTokenFilter(@Value("${secret.api.key}") String apiKey,
                              @Value("${secret.public.key}") String publicKey,
                              JwtTokenService jwtTokenService) {

        this.apiKey = apiKey;
        this.publicKey = publicKey;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        log.info("*** AuthToken Filter START *** ");
        try {
            String requestURI = request.getRequestURI();
            log.info("*** requestURI *** ->>>> " + requestURI);
            String authorization = "Authorization";
            log.info("Header TOKEN ->>> " + request.getHeader(authorization));

            final String authorisationKey = request.getHeader(authorization);

            if (authorisationKey != null) {
                final boolean anyMatch = getApiKeyMethods().stream().anyMatch(requestURI::contains);

                if (anyMatch) {
                    processAuthTokenVerification(request, response, chain, authorisationKey);
                } else {
                    processJwtTokenAuth(request, response, chain, authorisationKey);
                }
            } else {
                log.error("*** AuthToken Filter FINISHED with error: Missing API Key ***");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, GasPriceConstants.MISSING_AUTH_TOKEN);
            }
        } catch (
                Exception e) {
            log.error("*** AuthToken Filter FINISHED with error *** ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, GasPriceConstants.TECH_ERROR_MSG);
        }
    }

    private boolean verifyToken(final String token, final HttpServletResponse response) throws IOException {
        boolean validToken = false;
        try {
            final VerifyTokenResponse verifyTokenResponse = jwtTokenService.verifyJwtToken(getTokenRequest(token));
            if (null != verifyTokenResponse) {
                validToken = verifyTokenResponse.isValidToken();
            }
        } catch (Exception e) {
            log.error("An error occurred while verifying jwt token:", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, GasPriceConstants.TECH_ERROR_MSG);
        }
        return validToken;
    }

    private VerifyTokenRequest getTokenRequest(final String token) throws GasPriceException {
        final DecodedToken decodedToken = TokenDecoder.getDecodedToken(token);
        return new VerifyTokenRequest().withPublicKey(publicKey).withToken(token).withRoles(decodedToken.getRoles()).withUniqueId(Long.valueOf(decodedToken.getUid()));
    }

    private List<String> getApiKeyMethods() {
        final List<String> apiKeyMethods = new ArrayList<>();
        apiKeyMethods.add("/api/user/register");
        apiKeyMethods.add("/api/auth/token");
        return apiKeyMethods;
    }

    private void processJwtTokenAuth(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain,
                                     final String authorisationKey) throws IOException {
        try {
            final boolean isValidToken = verifyToken(authorisationKey, response);
            if (isValidToken) {
                log.info("*** JWT token verified successfully *** ");
                chain.doFilter(request, response);
            } else {
                log.error(GasPriceConstants.INVALID_TOKEN + authorisationKey);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, GasPriceConstants.INVALID_TOKEN);
            }
        } catch (Exception e) {
            log.error(GasPriceConstants.ERROR_MESSAGE + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, GasPriceConstants.TECH_ERROR_MSG);
        }
    }

    private void processAuthTokenVerification(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain,
                                              final String authorisationKey) throws IOException {
        try {
            if (authorisationKey.equals(apiKey)) {
                log.info("*** AuthToken Filter END *** ");
                chain.doFilter(request, response);
            } else {
                log.error(GasPriceConstants.INVALID_TOKEN + authorisationKey);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, GasPriceConstants.INVALID_TOKEN);
            }
        } catch (Exception e) {
            log.error(GasPriceConstants.ERROR_MESSAGE + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, GasPriceConstants.TECH_ERROR_MSG);
        }
    }
}
