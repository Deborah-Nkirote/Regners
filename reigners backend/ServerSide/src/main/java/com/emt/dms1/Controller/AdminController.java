package com.emt.dms1.Controller;
import java.time.LocalDate;
import java.util.List;

import com.emt.dms1.Services.AdminService;
import com.emt.dms1.utils.EntityResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api/v3/admin")
public class AdminController {

    private final AdminService adminService;


    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }



    @PutMapping("/update-welcome-message")
    public EntityResponse updateWelcomeMessage(@RequestParam String message, @RequestParam Long id) {
        return adminService.updateWelcomeMessage(message, id);
    }
    @PostMapping("/welcome-message")
    public EntityResponse postWelcomeMessage(@RequestParam String message) {
        return adminService.postWelcomeMessage(message);
    }
    @GetMapping("/welcome message")
    public EntityResponse getWelcomeMessage() {
        return adminService.getWelcomeMessage();
    }

    @GetMapping("/events/upcoming")
    public EntityResponse getUpcomingEvents() {
        return adminService.getUpcomingEvents();
    }


    @PostMapping(value = "/events", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EntityResponse events(
            @RequestParam String event,
            @RequestParam MultipartFile imageFile,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) { // Add the date parameter with format
        return adminService.addEvent(event, imageFile, date);
    }


    @DeleteMapping("/events/{id}")
    public ResponseEntity<EntityResponse> deleteEventById(@PathVariable("id") Long eventId) {
        EntityResponse response = adminService.deleteEventById(eventId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/sermons")
    public EntityResponse getSermons() {
        return adminService.getSermons();
    }
    @PostMapping("/live-stream-url")
    public EntityResponse postLiveStreamUrl(@RequestParam String url) {
        return adminService.postLiveStreamUrl(url);
    }
    @GetMapping("/live-stream-url")
    public EntityResponse getLiveStreamUrl() {
        return adminService.getLiveStreamUrl();
    }



    @GetMapping("/user-interests")
    public EntityResponse getUserInterests() {
        return adminService.getUserInterests();
    }

    @PostMapping(value = "/sermons", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EntityResponse uploadSermon(
            @RequestParam String title,
            @RequestParam String videoUrl,
            @RequestParam(required = false) MultipartFile notesFile) {
        return adminService.uploadSermon(title, videoUrl, notesFile);
    }
    @PostMapping("/submit-interests")
    public EntityResponse submitUserInterests(
            @RequestParam String name,
            @RequestParam long phoneNumber,
            @RequestParam List<String> interests) {
        return AdminService.submitUserInterests(name, phoneNumber, interests);
    }

}
