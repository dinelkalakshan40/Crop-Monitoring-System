package lk.ijse.cropMonitoringSystem.service.impl;

import lk.ijse.cropMonitoringSystem.DTO.CropDTO;
import lk.ijse.cropMonitoringSystem.entity.CropEntity;
import lk.ijse.cropMonitoringSystem.repository.CropRepo;
import lk.ijse.cropMonitoringSystem.repository.FieldRepository;
import lk.ijse.cropMonitoringSystem.repository.MonitoringRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
@Transactional
public class CropService {
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private MonitoringRepo monitoringRepo;
    @Autowired
    private CropRepo cropRepo;
    public void saveCrop(CropDTO cropDTO, MultipartFile cropImage, double sizeInMB) throws IOException {
        CropEntity cropEntity = new CropEntity();
        cropEntity.setCropCode(cropDTO.getCropCode());
        cropEntity.setCropName(cropDTO.getCropName());

        // Convert the MultipartFile (image) to Base64 and set it
        byte[] cropImageBytes = cropImage.getBytes();
        String base64Image = Base64.getEncoder().encodeToString(cropImageBytes);
        cropEntity.setCropImage(base64Image);  // Store as Base64 string
        cropEntity.setCategory(cropDTO.getCategory());
        cropEntity.setCropSeason(cropDTO.getCropSeason());
        cropEntity.setFieldCrops(fieldRepository.findById(cropDTO.getFieldCode()).orElseThrow(
                () -> new RuntimeException("Field not found")
        ));
        if (cropDTO.getLogCode() != null) {
            cropEntity.setMonitorCrop(monitoringRepo.findById(cropDTO.getLogCode()).orElse(null));
        }

        // Optionally log or store the image size in MB
        System.out.println("Crop image size: " + String.format("%.2f MB", sizeInMB));

        // Save the entity
        cropRepo.save(cropEntity);
    }

}
