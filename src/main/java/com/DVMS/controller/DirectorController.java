package com.DVMS.controller;

import com.DVMS.entity.Admin;
import com.DVMS.entity.LeaveRequest;
import com.DVMS.entity.Visitor;
import com.DVMS.repository.AdminRepository;
import com.DVMS.repository.VisitorRepository;
import com.DVMS.service.AdminService; // Import AdminService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.DVMS.service.LeaveRequestService;
import com.DVMS.service.VisitorService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.DVMS.entity.Director;
import com.DVMS.repository.DirectorRepository;


import java.util.Optional;

@Controller
@RequestMapping("/director")
public class DirectorController {

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminService adminService; 
    
    @Autowired
    private VisitorService visitorService;// Inject AdminService
    
    @Autowired
    private LeaveRequestService leaveRequestService;
//=======================
    @Autowired
    private DirectorRepository directorRepository;
    @GetMapping("/assigned-requests")
    public String getAssignedRequests(@RequestParam(value = "type", required = false, defaultValue = "pending") String type, Model model) {
        String loggedInUsername = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Admin loggedInDirector = adminRepository.findByUsername(loggedInUsername).orElse(null);

        if (loggedInDirector != null) {
            String loggedInDirectorName = loggedInDirector.getName();
            List<Visitor> visitors = visitorRepository.findAll();
            List<Visitor> filteredVisitors = visitors.stream()
                    .filter(visitor -> visitor.getMeetWith().equals(loggedInDirectorName))
                    .filter(visitor -> {
                        if (type.equalsIgnoreCase("pending")) {
                            return visitor.getStatus().equalsIgnoreCase("pending");
                        } else if (type.equalsIgnoreCase("approved")) {
                            return visitor.getStatus().equalsIgnoreCase("approved");
                        } else if (type.equalsIgnoreCase("rejected")) {
                            return visitor.getStatus().equalsIgnoreCase("rejected");
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            model.addAttribute("visitors", filteredVisitors);
            model.addAttribute("type", type);
        }
        return "assigned-requests";
    }
    
    // leave logs request director dashboard
    
 //   @GetMapping("/director/leave-logs")
   // public String showDirectorLeaveLogs(Model model) {
     //   String username = SecurityContextHolder.getContext().getAuthentication().getName();
       // List<LeaveRequest> pendingRequests = leaveRequestService.getPendingLeaveRequestsByUsername(username);
    //    System.out.println("Pending requests in director leave logs: " + pendingRequests); //add this line
      //  model.addAttribute("pendingRequests", pendingRequests);
        //return "director-leave-logs";// Create this Thymeleaf template
 //   }
    //==================
    @GetMapping("/leave-logs")
    public String showDirectorLeaveLogs(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Admin> adminOptional = adminRepository.findByUsername(username);

        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();

            List<LeaveRequest> pendingRequests = leaveRequestService.getPendingLeaveRequestsByUsernameAndDepartment(username);
            List<LeaveRequest> approvedRequests = leaveRequestService.getApprovedLeaveRequestsByUsernameAndDepartment(username);
            List<LeaveRequest> rejectedRequests = leaveRequestService.getRejectedLeaveRequestsByUsernameAndDepartment(username);

            model.addAttribute("pendingRequests", pendingRequests);
            model.addAttribute("approvedRequests", approvedRequests);
            model.addAttribute("rejectedRequests", rejectedRequests);

            return "leave-logs";
        } else {
            return "redirect:/director/dashboard";
        }
    }
    
   //===================

  //  @GetMapping("/director/leave-logs")
 //   public String showDirectorLeaveLogs(Model model) {
       // Get the logged-in director's username
 //      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
   //     String username = authentication.getName();
//
  //      // Find the director by username
    //    Optional<Director> directorOptional = directorRepository.findByUsername(username);
/*
        if (directorOptional.isPresent()) {
            Director director = directorOptional.get();

            // Fetch leave requests for this director's department
            List<LeaveRequest> pendingRequests = leaveRequestService.getPendingLeaveRequestsByDepartment(director.getDepartment());
            List<LeaveRequest> approvedRequests = leaveRequestService.getApprovedLeaveRequestsByDepartment(director.getDepartment());
            List<LeaveRequest> rejectedRequests = leaveRequestService.getRejectedLeaveRequestsByDepartment(director.getDepartment());

            model.addAttribute("pendingRequests", pendingRequests);
            model.addAttribute("approvedRequests", approvedRequests);
            model.addAttribute("rejectedRequests", rejectedRequests);

            return "leave-logs"; // Use the same leave-logs.html
        } else {
            // Handle case where director is not found
            return "redirect:/director/dashboard"; // Or another appropriate redirect
        }
    */
    
   
    
    @PostMapping("/leave-request")
    public String submitLeaveRequest(@ModelAttribute LeaveRequest leaveRequest) {
        leaveRequestService.saveLeaveRequest(leaveRequest);
        return "redirect:/director/leave-logs";
    }
    
    @PostMapping("/approve-leave/{id}")
    public String approveLeave(@PathVariable Long id, @RequestParam String approvedBy) {
        leaveRequestService.approveLeaveRequest(id, approvedBy);
        return "redirect:/director/leave-logs";
    }

    @PostMapping("/reject-leave/{id}")
    public String rejectLeave(@PathVariable Long id, @RequestParam String rejectedBy, @RequestParam String reason) {
        leaveRequestService.rejectLeaveRequest(id, rejectedBy, reason);
        return "redirect:/director/leave-logs";
    }
    @GetMapping("/approved-leaves")
    public String showApprovedLeaves(Model model){
        List<LeaveRequest> approvedLeaves = leaveRequestService.getApprovedLeaveRequests();
        model.addAttribute("approvedLeaves", approvedLeaves);
        return "approved-leaves";
    }

    @GetMapping("/rejected-leaves")
    public String showRejectedLeaves(Model model){
        List<LeaveRequest> rejectedLeaves = leaveRequestService.getRejectedLeaveRequests();
        model.addAttribute("rejectedLeaves", rejectedLeaves);
        return "rejected-leaves";
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<String> approveRequest(@PathVariable Long id) {
        Visitor visitor = visitorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid visitor ID"));
        visitor.setStatus(com.DVMS.entity.Visitor.VisitorStatus.APPROVED.name());
        visitor.setApprovalTime(LocalDateTime.now());
        String loggedInUsername = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Admin loggedInDirector = adminRepository.findByUsername(loggedInUsername).orElse(null);
        if (loggedInDirector != null) {
            visitor.setHostApprovedBy(loggedInDirector.getName());
        }
        visitorRepository.save(visitor);
        return ResponseEntity.ok("Approved"); // Return a response
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<String> rejectRequest(@PathVariable Long id, @RequestParam("cancellationReason") String cancellationReason) {
        Visitor visitor = visitorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid visitor ID"));
        visitor.setStatus(com.DVMS.entity.Visitor.VisitorStatus.REJECTED.name());
        visitor.setApprovalTime(LocalDateTime.now());
        visitor.setCancellationReason(cancellationReason);
        String loggedInUsername = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Admin loggedInDirector = adminRepository.findByUsername(loggedInUsername).orElse(null);
        if (loggedInDirector != null) {
            visitor.setRejectedBy(loggedInDirector.getName()); // Use getName() to get the full name
        }
        visitorRepository.save(visitor);
        return ResponseEntity.ok("Rejected"); // Return a response
    }

    @GetMapping("/dashboard")
    public String directorDashboard(Model model) {
        String loggedInUsername = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Admin loggedInDirector = adminRepository.findByUsername(loggedInUsername).orElse(null);

        if (loggedInDirector != null) {
            String loggedInDirectorName = loggedInDirector.getName();
            List<Visitor> visitors = visitorRepository.findAll();
            List<Visitor> filteredVisitors = visitors.stream()
                    .filter(visitor -> visitor.getMeetWith().equals(loggedInDirectorName))
                    .collect(Collectors.toList());
            model.addAttribute("visitors", filteredVisitors);
        }
        return "director_dashboard";
    }
    @GetMapping("/director/dashboard-stats")
    public ResponseEntity<Map<String, Integer>> getDashboardStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalVisitors", visitorService.getTotalVisitors());
        stats.put("pendingVisitors", visitorService.getPendingVisitors());
        stats.put("approvedRequests", visitorService.getApprovedVisitors());
        stats.put("pendingLeaves", leaveRequestService.getPendingLeaveRequests().size());
        System.out.println("Pending leaves in director dashboard: " + stats.get("pendingLeaves")); //add this line
        return ResponseEntity.ok(stats);
    }
    @GetMapping("/profile")
    @ResponseBody
    public Admin getDirectorProfile() {
        String loggedInUsername = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return adminService.findByUsername(loggedInUsername);
    }
}