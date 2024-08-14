package Reigners.demo.Controller;

import Reigners.demo.Models.Events;
import Reigners.demo.Services.AdminService;
import Reigners.demo.Utils.EntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v3/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // Uncomment and fix as needed
    // @GetMapping("/welcome-message")
    // public EntityResponse getWelcomeMessage() {
    //     return adminService.getWelcomeMessage();
    // }

    @PutMapping("/update-welcome-message")
    public EntityResponse updateWelcomeMessage(@RequestParam String message, @RequestParam Long id) {
        return adminService.updateWelcomeMessage(message, id);
    }

    @GetMapping("/upcoming-events")
    public EntityResponse getUpcomingEvents() {
        return adminService.getUpcomingEvents();
    }

//    @PostMapping("/add-event")
//    public EntityResponse addEvent(@RequestBody Events request) {
//        return adminService.addEvent(request);
//    }

    @GetMapping("/live-stream-url/{id}")
    public EntityResponse getLiveStreamUrl(@PathVariable Long id) {
        return adminService.getLiveStreamUrl(id);
    }

//    @PutMapping("/update-live-stream-url")
//    public EntityResponse updateLiveStreamUrl(@RequestParam String url, @RequestParam String sermonId) {
//        return adminService.updateLiveStreamUrl(url, sermonId);
//    }

    @GetMapping("/sermons")
    public EntityResponse getSermons() {
        return adminService.getSermons();
    }

    @PutMapping("/update-sermons")
    public EntityResponse updateSermons(@RequestParam String sermon) {
        return adminService.updateSermons(sermon);
    }

    @GetMapping("/user-interests")
    public EntityResponse getUserInterests() {
        return adminService.getUserInterests();
    }
}
