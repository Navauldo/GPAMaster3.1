package Backend;

import java.util.ArrayList;

public class AdminControl {

    private static ArrayList<Admin> eadmins = new ArrayList<>();

    public AdminControl(){}

    public static boolean addAdmin(Admin admin){
        if (admin == null) {
            return false;
        }

        if (!isAdminIdUnique(admin.getId())) {
            return false;
        }

        eadmins.add(admin);
        return true;
    }

    public static Admin findAdminById(int adminId){
        for (Admin admin : eadmins) {
            if (admin.getId() == adminId) {
                return admin;
            }
        }
        return null;
    }

    public static boolean adminExists(int adminId) {
        return findAdminById(adminId) != null;
    }

    public static boolean isAdminIdUnique(int adminId) {
        return findAdminById(adminId) == null;
    }

    public static ArrayList<Admin> getAllAdmins(){
        return eadmins;
    }

    public static boolean editAdmin(int adminId, String newName, String newPassword){
        Admin admin = findAdminById(adminId);

        if (admin == null) {
            System.out.println("Admin does not exist.");
            return false;
        }

        admin.setName(newName);
        admin.setPassword(newPassword);
        return true;
    }

    public static boolean deleteAdmin(int adminId){
        Admin admin = findAdminById(adminId);

        if (admin == null) {
            System.out.println("Admin does not exist.");
            return false;
        }

        eadmins.remove(admin);
        return true;
    }

    //FOR FILE UPDATE CLASS
    public static void clearAdmins() {
        eadmins.clear();
    }
}