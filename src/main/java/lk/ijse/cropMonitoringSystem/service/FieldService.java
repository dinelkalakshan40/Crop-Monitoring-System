package lk.ijse.cropMonitoringSystem.service;

import lk.ijse.cropMonitoringSystem.DTO.FieldDTO;

public interface FieldService {
    public void saveField(FieldDTO fieldDTO);
    FieldDTO getSelectedStaffAndField(String fieldCode);
    FieldDTO getOnlySelectedField(String fieldCode);
    FieldDTO updateFieldAndStaff(String fieldCode,FieldDTO fieldDTO);
}
