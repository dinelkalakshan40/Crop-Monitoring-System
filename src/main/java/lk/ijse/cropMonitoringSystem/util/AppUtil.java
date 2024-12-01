package lk.ijse.cropMonitoringSystem.util;

import java.util.UUID;

public class AppUtil {
    public static String generateFieldId(){
        return "Field-ID: "+ UUID.randomUUID();
    }
    public static String generateStaffId(){
        return "Staff-ID: "+UUID.randomUUID();
    }
    public static String generateUserId(){
        return "User-ID: "+UUID.randomUUID();
    }
}
