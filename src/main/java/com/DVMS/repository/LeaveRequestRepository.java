package com.DVMS.repository;



import com.DVMS.entity.Admin;
import com.DVMS.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByStatus(String status);
    List<LeaveRequest> findByUsernameAndDepartment(String username, Admin.Department department);
    List<LeaveRequest> findByUsernameAndDepartmentAndStatus(String username, Admin.Department department, String status);

    List<LeaveRequest> findByStudentId(String studentId);
    List<LeaveRequest> findByDepartment(Admin.Department department);
    
    List<LeaveRequest> findByStudentId(String studentId) {
        System.out.println("Finding leave requests for studentId: " + studentId); // Add this line
        //List<LeaveRequest> result = JpaRepository.super.findAll().stream().filter(leaveRequest -> leaveRequest.getStudentId().equals(studentId)).collect(Collectors.toList());
        List<LeaveRequest> result = leaveRequestRepository.findByStudentId(studentId);
        System.out.println("Results found:" + result); //add this line
        return result;
    }

    List<LeaveRequest> findByDepartment(Admin.Department department) {
        System.out.println("Finding leave requests for department: " + department); //add this line
       // List<LeaveRequest> result = JpaRepository.super.findAll().stream().filter(leaveRequest -> leaveRequest.getDepartment().equals(department)).collect(Collectors.toList());
        List<LeaveRequest> result = leaveRequestRepository.findByDepartment(department);
        System.out.println("Results found:" + result); //add this line
        return result;
    }
}
