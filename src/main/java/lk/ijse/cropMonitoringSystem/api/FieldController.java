package lk.ijse.cropMonitoringSystem.api;

import lk.ijse.cropMonitoringSystem.DTO.FieldDTO;
import lk.ijse.cropMonitoringSystem.DTO.StaffDTO;
import lk.ijse.cropMonitoringSystem.exception.DataPersistException;
import lk.ijse.cropMonitoringSystem.service.FieldServiceIMPL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
@RequestMapping("api/v1/fields")
public class FieldController {
    static Logger logger= LoggerFactory.getLogger(FieldController.class);

    @Autowired
    private FieldServiceIMPL fieldService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> saveField(@RequestBody FieldDTO fieldDTO) {
        logger.info("saveField method call");
        Map<String, String> response = new HashMap<>();
        if (!fieldDTO.getFieldCode().matches("^FLD-00\\d+$")) {
            logger.info("check validation ");
            response.put("error", "Invalid field code. Expected format: FLD-00");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        }

        try {
            fieldService.saveField(fieldDTO);
            response.put("message", "Field and staff data saved successfully");
            logger.info("Field saved ");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataPersistException e) {
            e.printStackTrace();
            logger.info("Field not saved bad request ");
            response.put("error", "Bad request: Invalid data");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Field Internal server error");
            response.put("error", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<String, Object>>> getAllFields() {
        logger.info("getAllFields called");
        try {
            List<Map<String, Object>> fieldsWithStaffIds = fieldService.getAllFields();
            logger.info("get All Field");
            return ResponseEntity.ok(fieldsWithStaffIds);
        } catch (Exception e) {
            logger.info("error getAll Field");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @GetMapping("/fieldCode")
    public ResponseEntity<String> generateFieldCode() {
        try {
            String newFieldCode=fieldService.generateNextFieldCode();// Generate next code
            return ResponseEntity.ok(newFieldCode); // Return generated code
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating field code");
        }
    }

    @GetMapping("/{fieldCode}")
    public ResponseEntity<FieldDTO> getFieldAndStaff(@PathVariable String fieldCode) {
        logger.info("getFieldAndStaff method called ");
        // Validate the fieldCode format
        if (!fieldCode.matches("^FLD-\\d{3}$")) {
            logger.info("getFieldAndStaff validation sucessfully");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }
        FieldDTO fieldDTO = fieldService.getSelectedStaffAndField(fieldCode);
        return new ResponseEntity<>(fieldDTO, HttpStatus.OK);
    }
    @GetMapping("/details/{fieldCode}")
    public ResponseEntity<FieldDTO> getOnlyField(@PathVariable String fieldCode) {
        logger.info("getOnlyField Method called");
        FieldDTO fieldDetails = fieldService.getOnlySelectedField(fieldCode);
        return new ResponseEntity<>(fieldDetails, HttpStatus.OK);
    }
    @GetMapping("/{fieldCode}/staff")
    public ResponseEntity<List<StaffDTO>> getStaffByFieldCode(@PathVariable String fieldCode) {
        List<StaffDTO> staff = fieldService.getOnlySelectedFiled(fieldCode);
        return new ResponseEntity<>(staff, HttpStatus.OK);
    }
    @PutMapping("/{fieldCode}")
    public ResponseEntity<FieldDTO> updateFieldAndStaff(@PathVariable String fieldCode, @RequestBody FieldDTO fieldDTO) {
        logger.info("updateFieldAndStaff Method called");
        System.out.println("Received data: " + fieldDTO);
        try {
            FieldDTO updatedField = fieldService.updateFieldAndStaff(fieldCode, fieldDTO);
            logger.info("updated sucess");
            return new ResponseEntity<>(updatedField, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{fieldCode}")
    public ResponseEntity<Void> deleteFieldAndStaff(@PathVariable String fieldCode) {
        logger.info("deleteFieldAndStaff Method called");


        try {
            fieldService.deleteFieldAndStaff(fieldCode);
            logger.info("delete Field SucessFully");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>((HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
