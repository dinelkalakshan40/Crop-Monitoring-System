package lk.ijse.cropMonitoringSystem.service.impl;

import lk.ijse.cropMonitoringSystem.DTO.FieldDTO;
import lk.ijse.cropMonitoringSystem.entity.FieldEntity;
import lk.ijse.cropMonitoringSystem.entity.StaffEntity;
import lk.ijse.cropMonitoringSystem.exception.DataPersistException;
import lk.ijse.cropMonitoringSystem.repository.FieldRepository;
import lk.ijse.cropMonitoringSystem.repository.StaffRepo;
import lk.ijse.cropMonitoringSystem.service.FieldService;
import lk.ijse.cropMonitoringSystem.util.Mapping;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FieldServiceIMPL implements FieldService {
    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private StaffRepo staffRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public void saveField(FieldDTO fieldDTO) {
        // Convert FieldDTO to FieldEntity
        FieldEntity fieldEntity = modelMapper.map(fieldDTO, FieldEntity.class);

        // Map and associate StaffEntity objects
        List<StaffEntity> staffEntities = fieldDTO.getStaff().stream()
                .map(staffDTO -> {
                    // Convert StaffDTO to StaffEntity
                    StaffEntity staffEntity = modelMapper.map(staffDTO, StaffEntity.class);

                    // Check if the staff already exists in the database
                    return staffRepository.findById(staffEntity.getStaffId())
                            .orElse(staffEntity); // Use the existing entity if found, or the new one
                })
                .collect(Collectors.toList());

        // Set staff list in the field entity
        fieldEntity.setStaff(staffEntities);

        // Save the field entity along with its associated staff
        fieldRepository.save(fieldEntity);
    }
}
