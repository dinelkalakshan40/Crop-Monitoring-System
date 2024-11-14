package lk.ijse.cropMonitoringSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleDTO implements Serializable {
    private String vehicleCode;
    private String plateNumber;
    private String category;
    private String fuelType;
    private String status;
    private String remarks;
    private String staffId;
}
