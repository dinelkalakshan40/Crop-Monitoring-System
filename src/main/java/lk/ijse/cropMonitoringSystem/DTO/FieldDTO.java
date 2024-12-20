package lk.ijse.cropMonitoringSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldDTO {
    private String fieldCode;
    private String fieldName;
    private String fieldLocation;
    private Double fieldSize;
    private String fieldImage1;
    private String fieldImage2;
    private String logCode;
    private List<StaffDTO> staff;
}
