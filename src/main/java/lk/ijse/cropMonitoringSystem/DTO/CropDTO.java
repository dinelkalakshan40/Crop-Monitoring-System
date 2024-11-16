package lk.ijse.cropMonitoringSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CropDTO implements Serializable {
    private String cropCode;
    private String cropName;
    private String cropImage;
    private String category;
    private String cropSeason;
    private String LogCode;
    private String fieldCode;
}
