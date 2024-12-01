package lk.ijse.cropMonitoringSystem.service.AuthService;

import lk.ijse.cropMonitoringSystem.DTO.UserDTO;
import lk.ijse.cropMonitoringSystem.Secure.JWTAuthResponse;
import lk.ijse.cropMonitoringSystem.Secure.SignIn;
import lk.ijse.cropMonitoringSystem.entity.UserEntity;
import lk.ijse.cropMonitoringSystem.repository.UserRepo;
import lk.ijse.cropMonitoringSystem.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final Mapping mapping;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public JWTAuthResponse signIn(SignIn signIn){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signIn.getEmail(),signIn.getPassword()));
        var user =userRepo.findByEmail(signIn.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var generatedToken = jwtService.generateToken(user);
        return JWTAuthResponse.builder().token(generatedToken).build();
    }
    public JWTAuthResponse signUp(UserDTO userDTO){
        UserEntity savedUser =userRepo.save(mapping.toUserEntity(userDTO));
        //Generate the token and return it
        var generatedToken = jwtService.generateToken(savedUser);
        return JWTAuthResponse.builder().token(generatedToken).build();
    }
    public JWTAuthResponse refreshToken(String accessToken){
        var userName = jwtService.extractUserName(accessToken);
        var findUser =  userRepo.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var refreshToken = jwtService.refreshToken(findUser);
        return JWTAuthResponse.builder().token(refreshToken).build();
    }
}
