package lk.ijse.cropMonitoringSystem.service.impl;

import lk.ijse.cropMonitoringSystem.DTO.StaffDTO;
import lk.ijse.cropMonitoringSystem.entity.FieldEntity;
import lk.ijse.cropMonitoringSystem.entity.StaffEntity;
import lk.ijse.cropMonitoringSystem.exception.DataPersistException;
import lk.ijse.cropMonitoringSystem.repository.FieldRepository;
import lk.ijse.cropMonitoringSystem.repository.StaffRepo;
import lk.ijse.cropMonitoringSystem.service.StaffService;
import lk.ijse.cropMonitoringSystem.util.Mapping;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StaffServiceIMPL implements StaffService {
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Transactional
    public void saveStaff(StaffDTO staffDTO) {
        // Convert StaffDTO to StaffEntity
        StaffEntity staffEntity = modelMapper.map(staffDTO,StaffEntity.class);

        // Map and associate FieldEntity objects
        List<FieldEntity> fieldEntities = staffDTO.getFields().stream()
                .map(fieldDTO -> {

                    // Convert FieldDTO to FieldEntity
                    FieldEntity fieldEntity = modelMapper.map(fieldDTO, FieldEntity.class);

                    // Check if the staff already exists in the database
                    return fieldRepository.findById(fieldEntity.getFieldCode())
                            .orElse(fieldEntity); // Use the existing entity if found, or the new one
                })
                .collect(Collectors.toList());

        // Set staff list in the field entity
        staffEntity.setFields(fieldEntities);

        // Save the field entity along with its associated staff
        staffRepo.save(staffEntity);
    }
}
