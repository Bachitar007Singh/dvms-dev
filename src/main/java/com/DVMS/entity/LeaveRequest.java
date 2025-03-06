package com.DVMS.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String studentName;
    private String studentId;
    private String username;
    @Enumerated(EnumType.STRING)
    private Admin.Department department; // Corrected to use enum only
    private String reason;
    private String qrCode;
    private Boolean adminApproval;
    private Boolean directorApproval;
    private String status;
    private String rejectionReason;
    private String approvedBy;
    private String rejectionBy;
    private LocalDateTime requestTime;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
        
       
     
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Admin.Department getDepartment() {
        return department;
    }

    public void setDepartment(Admin.Department department) {
        this.department = department;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Boolean getAdminApproval() {
        return adminApproval;
    }

    public void setAdminApproval(Boolean adminApproval) {
        this.adminApproval = adminApproval;
    }

    public Boolean getDirectorApproval() {
        return directorApproval;
    }

    public void setDirectorApproval(Boolean directorApproval) {
        this.directorApproval = directorApproval;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getRejectionBy() {
        return rejectionBy;
    }

    public void setRejectionBy(String rejectionBy) {
        this.rejectionBy = rejectionBy;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}