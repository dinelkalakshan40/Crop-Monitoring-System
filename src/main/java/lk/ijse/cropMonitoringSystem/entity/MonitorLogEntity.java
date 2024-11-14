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
@Table(name = "monitor_log")
public class MonitorLogEntity implements Serializable {
    @Id
    private String LogCode;
    private String date;
    private String logDetails;
    private String observedImage;
    @ManyToOne
    @JoinColumn(name = "staffId",nullable = false)
    private StaffEntity staff;


}
