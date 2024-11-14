package lk.ijse.cropMonitoringSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "equipment")
public class EquipmentEntity {
    @Id
    private String equipmentId;
    private String name;
    private String type;
    private String status;

    //staff Entity
    @ManyToOne//
    @JoinColumn(name = "staffId",nullable = false)
    private StaffEntity staff;

    //field Entity
    @ManyToOne//
    @JoinColumn(name = "fieldCode")
    private FieldEntity fieldEquipment;

}
