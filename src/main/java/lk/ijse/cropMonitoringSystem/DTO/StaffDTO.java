package lk.ijse.cropMonitoringSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StaffDTO {
    private String staffId;
    private String firstName;
    private String lastName;
    private String designation;
    private String gender;
    private String dob;
    private String joinedDate;
    private String address;
    private String contact;
    private String role;
    private List<String> fieldCodes; // Only store field codes for simplicity
}
