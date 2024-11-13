package lk.ijse.cropMonitoringSystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "vehicle")
public class VehicleEntity implements Serializable {
    @Id
    private String vehicleCode;
    private String plateNumber;
    private String category;
    private String fuelType;
    private String status;
    private String remarks;
}
