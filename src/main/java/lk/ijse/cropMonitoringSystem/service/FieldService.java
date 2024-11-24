package lk.ijse.cropMonitoringSystem.service;

import lk.ijse.cropMonitoringSystem.DTO.FieldDTO;
import lk.ijse.cropMonitoringSystem.DTO.StaffDTO;

import java.util.List;
import java.util.Map;

public interface FieldService {
    public void saveField(FieldDTO fieldDTO);
    List<Map<String, Object>> getAllFields();
    FieldDTO getSelectedStaffAndField(String fieldCode);
    FieldDTO getOnlySelectedField(String fieldCode);
    FieldDTO updateFieldAndStaff(String fieldCode,FieldDTO fieldDTO);
    public void deleteFieldAndStaff(String fieldCode);
    List<StaffDTO> getOnlySelectedFiled(String fieldCode);
}
