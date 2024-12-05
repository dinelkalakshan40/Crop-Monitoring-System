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
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String observedImage;

    //staff Entity
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "staffId",nullable = true)
    private StaffEntity staff;

    // Field Entity
    @OneToMany(mappedBy = "monitor_log",cascade = CascadeType.ALL)//
    private List<FieldEntity> fields;

    //Crop Entity
    @OneToMany(mappedBy = "monitorCrop",cascade = CascadeType.ALL)///
    private List<CropEntity> crops;
}
