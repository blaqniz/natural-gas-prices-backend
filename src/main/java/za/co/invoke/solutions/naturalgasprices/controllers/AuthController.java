package za.co.invoke.solutions.naturalgasprices.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.invoke.solutions.naturalgasprices.dto.*;
import za.co.invoke.solutions.naturalgasprices.services.AuthService;

import java.util.List;

import static za.co.invoke.solutions.naturalgasprices.util.GasPriceHelper.getXTokenAndSTSHeaders;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ResponseEntity<GenericResponse<JwtTokenResponse>> createJwtToken(@RequestBody final UserDto userDto) {

        log.info("*** Login START *** Request {}", userDto);

        final JwtTokenResponse tokenResponse = authService.creatLoginAuthToken(userDto);

        final GenericResponse<JwtTokenResponse> response = new GenericResponse<>()
                .withSuccessful(true)
                .withMessage("Token generated successfully.")
                .withPayload(tokenResponse);

        log.info("*** Login END *** ");

        return new ResponseEntity<>(response, getXTokenAndSTSHeaders(tokenResponse.getTokenData().getToken()), HttpStatus.ACCEPTED);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/verify-token")
    public ResponseEntity<GenericResponse<JwtTokenResponse>> verifyJwtToken(@RequestBody final VerifyTokenRequest request) throws Exception {

        log.info("*** Verify JWT Token START *** ");

        final VerifyTokenResponse verifyTokenResponse = authService.verifyJwtToken(request);

        final GenericResponse<JwtTokenResponse> response = new GenericResponse<>()
                .withSuccessful(true)
                .withMessage(verifyTokenResponse.isValidToken() ? "Token verified successfully." : "Token verification failed.")
                .withPayload(verifyTokenResponse);

        log.info("*** Verify JWT Token END *** ");

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
