package za.co.invoke.solutions.naturalgasprices.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.invoke.solutions.naturalgasprices.dto.*;
import za.co.invoke.solutions.naturalgasprices.services.RegistrationService;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@Slf4j
@RestController
@RequestMapping("/api/user")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<Void>> register(@RequestBody final UserDto userDto) {

        log.info("*** Registration START *** Request {}", userDto);

        registrationService.register(userDto);

        final GenericResponse<Void> response = new GenericResponse<>()
                .withSuccessful(true)
                .withMessage("User registered successfully.");

        log.info("*** Registration END *** ");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
