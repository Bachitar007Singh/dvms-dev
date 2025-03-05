package com.DVMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DVMS.entity.Admin;
import com.DVMS.entity.LeaveRequest;
import com.DVMS.repository.LeaveRequestRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.DVMS.repository.AdminRepository;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private AdminRepository adminRepository;

    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequest.setStatus("Pending");
        return leaveRequestRepository.save(leaveRequest);
    }

    public void saveLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequest.setRequestTime(LocalDateTime.now());
        System.out.println("Saving leave request: " + leaveRequest);
        LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);
        System.out.println("Leave request saved with ID: " + savedRequest.getId());
    }

    public List<LeaveRequest> getPendingLeaveRequestsByDepartment(Admin.Department department) {
        return leaveRequestRepository.findByStatus("Pending").stream()
                .filter(request -> request.getDepartment().equals(department))
                .collect(Collectors.toList());
    }

    public List<LeaveRequest> getApprovedLeaveRequestsByDepartment(Admin.Department department) {
        return leaveRequestRepository.findByStatus("Approved").stream()
                .filter(request -> request.getDepartment().equals(department))
                .collect(Collectors.toList());
    }

    public List<LeaveRequest> getRejectedLeaveRequestsByDepartment(Admin.Department department) {
        return leaveRequestRepository.findByStatus("Rejected").stream()
                .filter(request -> request.getDepartment().equals(department))
                .collect(Collectors.toList());
    }
//======
 /*   public List<LeaveRequest> getPendingLeaveRequestsByUsernameAndDepartment(String username) {
        Admin admin = // get the admin object
        return leaveRequestRepository.findByUsernameAndDepartmentAndStatus(username, admin.getDepartment(), "pending");
    }*/
    //=========
   /* public List<LeaveRequest> getPendingLeaveRequestsByUsernameAndDepartment(String username) {
        Optional<Admin> director = adminRepository.findByUsername(username);
        if (director.isPresent()) {
            Admin.Department directorDepartment = director.get().getDepartment();
            return leaveRequestRepository.findByDepartment(directorDepartment).stream()
                    .filter(request -> request.getStudentId().equals(username))
                    .filter(request -> request.getStatus().equals("Pending"))
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }*/
//==========
    public List<LeaveRequest> getPendingLeaveRequestsByUsernameAndDepartment(String username) {
        Optional<Admin> adminOptional = adminRepository.findByUsername(username);

        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            return leaveRequestRepository.findByUsernameAndDepartmentAndStatus(username, admin.getDepartment(), "pending");
        } else {
            return List.of();
        }
    }
    //==========
    public List<LeaveRequest> getApprovedLeaveRequestsByUsernameAndDepartment(String username) {
        Optional<Admin> director = adminRepository.findByUsername(username);
        if (director.isPresent()) {
            Admin.Department directorDepartment = director.get().getDepartment();
            return leaveRequestRepository.findByDepartment(directorDepartment).stream()
                    .filter(request -> request.getStudentId().equals(username))
                    .filter(request -> request.getStatus().equals("Approved"))
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    public List<LeaveRequest> getRejectedLeaveRequestsByUsernameAndDepartment(String username) {
        Optional<Admin> director = adminRepository.findByUsername(username);
        if (director.isPresent()) {
            Admin.Department directorDepartment = director.get().getDepartment();
            return leaveRequestRepository.findByDepartment(directorDepartment).stream()
                    .filter(request -> request.getStudentId().equals(username))
                    .filter(request -> request.getStatus().equals("Rejected"))
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    public void approveLeaveRequest(Long id, String approvedBy) {
        Optional<LeaveRequest> leaveRequestOpt = leaveRequestRepository.findById(id);
        if (leaveRequestOpt.isPresent()) {
            LeaveRequest leaveRequest = leaveRequestOpt.get();
            leaveRequest.setStatus("Approved");
            leaveRequest.setApprovedBy(approvedBy);
            leaveRequestRepository.save(leaveRequest);
        } else {
            throw new RuntimeException("Leave request not found.");
        }
    }

    public LeaveRequest rejectLeaveRequest(Long id, String rejectedBy, String reason) {
        Optional<LeaveRequest> optionalLeaveRequest = leaveRequestRepository.findById(id);
        if (optionalLeaveRequest.isPresent()) {
            LeaveRequest leaveRequest = optionalLeaveRequest.get();
            leaveRequest.setStatus("Rejected");
            leaveRequest.setRejectionReason(reason);
            leaveRequest.setRejectionBy(rejectedBy);
            return leaveRequestRepository.save(leaveRequest);
        }
        return null;
    }

    public List<LeaveRequest> getApprovedLeaveRequests() {
        return leaveRequestRepository.findByStatus("Approved");
    }

    public List<LeaveRequest> getRejectedLeaveRequests() {
        return leaveRequestRepository.findByStatus("Rejected");
    }
}