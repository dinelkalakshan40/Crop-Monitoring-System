package lk.ijse.cropMonitoringSystem.api;

import lk.ijse.cropMonitoringSystem.DTO.FieldDTO;
import lk.ijse.cropMonitoringSystem.DTO.StaffDTO;
import lk.ijse.cropMonitoringSystem.exception.DataPersistException;
import lk.ijse.cropMonitoringSystem.service.FieldService;
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
    @Autowired
    private FieldService fieldService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> saveField(@RequestBody FieldDTO fieldDTO) {
        Map<String, String> response = new HashMap<>();
        if (!fieldDTO.getFieldCode().matches("^FLD-00\\d+$")) {
            response.put("error", "Invalid field code. Expected format: FLD-00");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
            fieldService.saveField(fieldDTO);
            response.put("message", "Field and staff data saved successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataPersistException e) {
            e.printStackTrace();
            response.put("error", "Bad request: Invalid data");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<String, Object>>> getAllFields() {
        try {
            List<Map<String, Object>> fieldsWithStaffIds = fieldService.getAllFields();
            return ResponseEntity.ok(fieldsWithStaffIds);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @GetMapping("/{fieldCode}")
    public ResponseEntity<FieldDTO> getFieldAndStaff(@PathVariable String fieldCode) {
        // Validate the fieldCode format
        if (!fieldCode.matches("^FLD-\\d{3}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        FieldDTO fieldDTO = fieldService.getSelectedStaffAndField(fieldCode);
        return new ResponseEntity<>(fieldDTO, HttpStatus.OK);
    }
    @GetMapping("/details/{fieldCode}")
    public ResponseEntity<FieldDTO> getOnlyField(@PathVariable String fieldCode) {
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
        // Validate the fieldCode format
        if (!fieldCode.matches("^FLD-\\d{3}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            FieldDTO updatedField = fieldService.updateFieldAndStaff(fieldCode, fieldDTO);
            return new ResponseEntity<>(updatedField, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{fieldCode}")
    public ResponseEntity<Void> deleteFieldAndStaff(@PathVariable String fieldCode) {
        // Validate the fieldCode format
        if (!fieldCode.matches("^FLD-\\d{3}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            fieldService.deleteFieldAndStaff(fieldCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>((HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
