//package com.DVMS.service;

//public class AdminService {

//}
package com.DVMS.service;

import com.DVMS.entity.Admin;
import com.DVMS.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    
    //===================
    public Admin.Department getAdminDepartment(Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        if (admin != null) {
            return admin.getDepartment();
        }
        return null; // Or throw an exception
    }
    //===================

    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username).orElse(null);
    }

    // Add other service methods here if needed.
    public Admin createAdmin(Admin admin) {
        admin.setDepartment(Admin.Department.HR); // Setting department to HR
        return adminRepository.save(admin);
    }

    public Admin updateAdminDepartment(Long adminId, Admin.Department newDepartment) {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        if (admin != null) {
            admin.setDepartment(newDepartment);
            return adminRepository.save(admin);
        }
        return null; // Or throw an exception
    }
}