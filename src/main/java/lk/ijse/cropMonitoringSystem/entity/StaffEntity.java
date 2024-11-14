package lk.ijse.cropMonitoringSystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "staff")

public class StaffEntity implements Serializable {
    @Id
    private String staffId;
    private String firstName;
    private String lastName;
    private String designation;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String dob;
    private String joinedDate;
    private String address;
    private String contact;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "staff",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
    private List<FieldEntity> fields; // Many-to-many relationship with fields

    //Vehicle Entity
    @OneToMany(mappedBy = "staff" ,cascade = CascadeType.ALL)
    private List<VehicleEntity> vehicles;///

    //MonitorLog Entity
    @OneToMany(mappedBy = "staff",cascade = CascadeType.ALL)
    private List<MonitorLogEntity> monitorLogs;

    //EquipmentEntity
    @OneToMany(mappedBy = "staff",cascade = CascadeType.ALL)///
    private List<EquipmentEntity> equipments;

    enum Gender {
        MALE, FEMALE
    }

    enum Role {
        MANAGER, WORKER, ASSISTANT, TECHNICIAN
    }
}


