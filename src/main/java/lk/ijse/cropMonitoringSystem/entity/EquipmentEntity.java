package lk.ijse.cropMonitoringSystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "equipment")
public class EquipmentEntity implements Serializable {
    private static final long serialVersionUID = -7941769011539363185L;
    @Id
    private String equipmentId;
    private String name;
    private String type;
    private String status;

    //staff Entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staffId",nullable = false)
    private StaffEntity staffEquipment;

    //field Entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fieldCode",nullable = false)
    private FieldEntity fieldEquipment;

}
