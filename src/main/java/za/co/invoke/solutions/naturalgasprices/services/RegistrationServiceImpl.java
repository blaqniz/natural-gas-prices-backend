package za.co.invoke.solutions.naturalgasprices.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import za.co.invoke.solutions.naturalgasprices.dto.*;
import za.co.invoke.solutions.naturalgasprices.entities.User;
import za.co.invoke.solutions.naturalgasprices.exceptions.UserAlreadyExistsException;
import za.co.invoke.solutions.naturalgasprices.repositories.UserRepository;

@Slf4j
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    public RegistrationServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void register(UserDto userDto) {
        try {
            if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
                throw new UserAlreadyExistsException("User already exists!");
            }

            final User user = new User();
            BeanUtils.copyProperties(userDto, user);
            user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);
        } catch (UserAlreadyExistsException e) {
            log.error("Username is already taken!", e);
            throw e;
        } catch (Exception e) {
            log.error("An error has occurred {}", e);
            throw e;
        }
    }
}
