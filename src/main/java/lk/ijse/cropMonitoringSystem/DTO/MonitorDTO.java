package lk.ijse.cropMonitoringSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MonitorDTO implements Serializable {
    private String LogCode;
    private String date;
    private String logDetails;
    private String observedImage;
    private List<CropDTO> cropDTOS;
    private String staffId;
}
