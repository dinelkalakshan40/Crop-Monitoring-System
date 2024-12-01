package lk.ijse.cropMonitoringSystem.DTO;

import lk.ijse.cropMonitoringSystem.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO implements Serializable {
    private String userId;
    private String userName;
    private String email;
    private String password;
    private Role role;
}
