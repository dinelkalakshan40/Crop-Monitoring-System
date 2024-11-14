package lk.ijse.cropMonitoringSystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "field")
public class FieldEntity implements Serializable {

    @Id
    private String fieldCode;
    private String fieldName;
    private Point  fieldLocation; // represents a location in (x, y) coordinate space
    private Double fieldSize;
    @Column(columnDefinition = "LONGTEXT")
    private String fieldImage1;
    @Column(columnDefinition = "LONGTEXT")
    private String fieldImage2;

    //field_staff
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "field_staff",
            joinColumns = @JoinColumn(name = "fieldCode"),
            inverseJoinColumns = @JoinColumn(name = "staffId")
    )
    @JsonManagedReference
    private List<StaffEntity> staff;

    //crop Entity
    @OneToMany(mappedBy = "fieldCrops")//
    private List<CropEntity> crops;

    //equipment Entity;
    @OneToMany(mappedBy = "fieldEquipment")//
    private List<EquipmentEntity> equipments;//

    //monitor_Log Entity
    @ManyToOne///
    @JoinColumn(name = "LogCode")
    private MonitorLogEntity monitorLogs;

}
