package lk.ijse.cropMonitoringSystem.service.AuthService;

import lk.ijse.cropMonitoringSystem.DTO.UserDTO;
import lk.ijse.cropMonitoringSystem.entity.UserEntity;
import lk.ijse.cropMonitoringSystem.exception.DataPersistException;
import lk.ijse.cropMonitoringSystem.repository.UserRepo;
import lk.ijse.cropMonitoringSystem.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private Mapping mapping;

    public void saveUser(UserDTO userDTO){
        UserEntity savedUser =userRepo.save(mapping.toUserEntity(userDTO));
        if (savedUser ==null){
            throw new DataPersistException("User Not saved");

        }
    }
    public List<UserDTO> getAllUsers(){
        List<UserEntity> allUsers =userRepo.findAll();
        return mapping.asUserDTOList(allUsers);
    }
    public UserDTO getUser(String userId) {
        // Fetch the user entity or throw an exception if not found
        UserEntity selectedUser = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

        // Map and return the UserDTO
        return mapping.toUserDTO(selectedUser);
    }

    public void deleteUser(String userId){
        Optional<UserEntity> existedUser = userRepo.findById(userId);
        if(!existedUser.isPresent()){
            throw new DataPersistException("User with id " + userId + " not found");
        }else {
            userRepo.deleteById(userId);
        }
    }

    public void updateUser(String userId, UserDTO userDTO) {
        Optional<UserEntity> tmpUser = userRepo.findById(userId);
        if(tmpUser.isPresent()) {
            tmpUser.get().setUserName(userDTO.getUserName());
            tmpUser.get().setEmail(userDTO.getEmail());
            tmpUser.get().setPassword(userDTO.getPassword());
        }
    }
    public UserDetailsService userDetailsService() {
        return email ->
                (UserDetails) userRepo.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

}
