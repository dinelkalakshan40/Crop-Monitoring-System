package lk.ijse.cropMonitoringSystem.entity;

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
@Table(name = "monitor_log")
public class MonitorLogEntity implements Serializable {
    @Id
    private String LogCode;
    private String date;
    private String logDetails;
    private String observedImage;

    //staff Entity
    @ManyToOne////
    @JoinColumn(name = "staffId",nullable = false)
    private StaffEntity staff;

    // Field Entity
    @OneToMany(mappedBy = "monitor_log")//
    private List<FieldEntity> fields;


    //Crop Entity
    @OneToMany(mappedBy = "monitorCrop")///
    private List<CropEntity> crops;


}
