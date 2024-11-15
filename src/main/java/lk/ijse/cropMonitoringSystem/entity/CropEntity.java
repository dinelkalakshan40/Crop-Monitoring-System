package lk.ijse.cropMonitoringSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "crop")
public class CropEntity implements Serializable {
    @Id
    private String cropCode;
    private String cropName;
    @Column(columnDefinition = "LONGTEXT")
    private String cropImage;
    private String category;
    private String cropSeason;

    //field Entity
    @ManyToOne///
    @JoinColumn(name = "fieldCode",nullable = false)
    private FieldEntity fieldCrops;

    //monitor Entity
    @ManyToOne///
    @JoinColumn(name = "LogCode")
    private MonitorLogEntity monitorCrop;

}
