package by.sapegina.springblog.service;

import by.sapegina.springblog.dto.LoginRequest;
import by.sapegina.springblog.dto.RegisterRequest;
import by.sapegina.springblog.entity.Email;
import by.sapegina.springblog.entity.User;
import by.sapegina.springblog.entity.VerificationToken;
import by.sapegina.springblog.exceptions.TheHumanException;
import by.sapegina.springblog.repository.UserRepository;
import by.sapegina.springblog.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now()); //current time
        user.setEnabled(false); //if user successful validated - true

        userRepository.save(user);

        String token = generateVerificationToken(user);

//        mailService.sendMail(new Email("Please activate your account"),
//                user.getEmail(),"..." + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString(); //generate random id for other token
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new TheHumanException("Error: Invalid Token!"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        @NotBlank(message = "Username is required!")
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new TheHumanException("Error: User not found! info: username is " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
    }
}
