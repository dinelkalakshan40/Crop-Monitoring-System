package lk.ijse.cropMonitoringSystem.util;

import lk.ijse.cropMonitoringSystem.DTO.FieldDTO;
import lk.ijse.cropMonitoringSystem.DTO.StaffDTO;
import lk.ijse.cropMonitoringSystem.entity.FieldEntity;
import lk.ijse.cropMonitoringSystem.entity.StaffEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class Mapping {
    @Autowired
    public ModelMapper modelMapper;

    public FieldEntity toFieldEntity(FieldDTO fieldDTO){
        FieldEntity map = modelMapper.map(fieldDTO, FieldEntity.class);
        List<StaffEntity> staffEntities =  new ArrayList<>();
        if (!CollectionUtils.isEmpty(fieldDTO.getStaffIds())) {
            staffEntities.addAll(fieldDTO.getStaffIds().stream().map(s -> {
                StaffEntity entity = new StaffEntity();
                entity.setStaffId(s);
                return entity;
            }).collect(Collectors.toList()));
        }
        map.setStaff(staffEntities);
        return map;
    }


    public StaffEntity toStaffEntity(StaffDTO staffDTO){
        return modelMapper.map(staffDTO,StaffEntity.class);
    }

}
