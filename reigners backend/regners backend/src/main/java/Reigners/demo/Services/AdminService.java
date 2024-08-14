package Reigners.demo.Services;


import Reigners.demo.Models.*;
import Reigners.demo.Repository.*;
import Reigners.demo.Utils.EntityResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AdminService {


    private AdminRepository adminRepository;
   private UserInterestRepository userInterestRepository;
    private SermonRepository sermonRepository;
   private EventRepository eventRepository;
   @Autowired
   private WelcomeRepo welcomeRepo;

//    private EntityResponse getWelcomeMessage() {
//        EntityResponse entityResponse = new EntityResponse();
//        log.info("Fetching welcome message...");
//
//        try {
//            String welcomeMessageRequest = adminRepository.findWelcomeMessage();
//
//            if (welcomeMessageRequest != null) {
//                entityResponse.setData(welcomeMessageRequest);
//                entityResponse.setStatusCode(HttpStatus.OK.value());
//                entityResponse.setMessage("Welcome message fetched successfully.");
//                log.info("Welcome message fetched successfully.");
//            } else {
//                entityResponse.setStatusCode(HttpStatus.NO_CONTENT.value());
//                entityResponse.setMessage("No welcome message found.");
//                log.info("No welcome message found.");
//            }
//        } catch (Exception e) {
//            log.error("Failed to fetch welcome message.", e);
//            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            entityResponse.setMessage("Failed to fetch welcome message.");
//        }
//
//        return entityResponse;


    public EntityResponse updateWelcomeMessage(String message, Long id) {
        EntityResponse entityResponse = new EntityResponse();
        log.info("Fetching welcome message with ID: {}", id);
        Optional<WelcomeMessageRequest> message1 = welcomeRepo.findById(id);

        log.info("Updating welcome message...");
        try {
            if (message1.isPresent()) {
                WelcomeMessageRequest request = message1.get();
                log.info("Found welcome message: {}", request.getMessage());

                request.setMessage(message);
                log.info("Setting new welcome message: {}", message);

                // Save the updated message
                welcomeRepo.save(request);
                log.info("Welcome message saved: {}", request.getMessage());

                entityResponse.setStatusCode(HttpStatus.OK.value());
                entityResponse.setMessage("Welcome message updated successfully.");
                entityResponse.setData(request);

                log.info("Welcome message updated successfully.");
            } else {
                entityResponse.setStatusCode(HttpStatus.NO_CONTENT.value());
                entityResponse.setMessage("No welcome message found to update.");
                log.info("No welcome message found to update.");
            }
        } catch (Exception e) {
            log.error("Failed to update welcome message.", e);
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("Failed to update welcome message.");
        }

        return entityResponse;
    }


    public EntityResponse addEvent(String event) {
        EntityResponse entityResponse = new EntityResponse();
        log.info("Adding new event...");

        try {
            Events newEvent = new Events();
            newEvent.setEvent(event);

            eventRepository.save(newEvent);

            entityResponse.setStatusCode(HttpStatus.CREATED.value());
            entityResponse.setMessage("Event added successfully.");
            entityResponse.setData(newEvent);
            log.info("Event added successfully.");
        } catch (Exception e) {
            log.error("Failed to add event.", e);
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("Failed to add event.");
        }

        return entityResponse;
    }

    public EntityResponse getLiveStreamUrl(Long id) {
        EntityResponse entityResponse = new EntityResponse();
        log.info("Fetching live stream URL for admin ID: {}", id);

        try {
            Optional<AdminModel> adminModelOptional = adminRepository.findById(id);
            if (adminModelOptional.isPresent()) {
                AdminModel adminModel = adminModelOptional.get();
                String url = adminModel.getLiveStreamUrl();

                entityResponse.setData(url);
                entityResponse.setStatusCode(HttpStatus.OK.value());
                entityResponse.setMessage("Live stream URL fetched successfully.");
                log.info("Live stream URL fetched successfully: {}", url);
            } else {
                entityResponse.setStatusCode(HttpStatus.NO_CONTENT.value());
                entityResponse.setMessage("No admin found with the provided ID.");
                log.info("No admin found with the provided ID: {}", id);
            }
        } catch (Exception e) {
            log.error("Failed to fetch live stream URL.", e);
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("Failed to fetch live stream URL.");
        }

        return entityResponse;
    }

//    public EntityResponse updateLiveStreamUrl(String url, String sermonId) {
//        EntityResponse entityResponse = new EntityResponse();
//        log.info("Updating live stream URL for sermon ID: {}", sermonId);
//
//        try {
//            // Update the live stream URL
//            sermonRepository.updateLiveStreamUrl(url, sermonId);
//
//            entityResponse.setStatusCode(HttpStatus.OK.value());
//            entityResponse.setMessage("Live stream URL updated successfully.");
//            log.info("Live stream URL updated successfully.");
//        } catch (Exception e) {
//            log.error("Failed to update live stream URL.", e);
//            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            entityResponse.setMessage("Failed to update live stream URL.");
//        }
//
//        return entityResponse;
//    }

    public EntityResponse getSermons() {
        EntityResponse entityResponse = new EntityResponse();
        log.info("Fetching sermons...");

        try {
            List<Sermon> sermons =sermonRepository.findAll();
            entityResponse.setData(sermons);
            entityResponse.setStatusCode(HttpStatus.OK.value());
            entityResponse.setMessage("Sermons fetched successfully.");
            log.info("Sermons fetched successfully.");
        } catch (Exception e) {
            log.error("Failed to fetch sermons.", e);
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("Failed to fetch sermons.");
        }

        return entityResponse;
    }

    public EntityResponse updateSermons(String sermon) {
        EntityResponse entityResponse = new EntityResponse();
        log.info("Updating sermon...");

        try {
           sermonRepository.findAll();
            entityResponse.setStatusCode(HttpStatus.OK.value());
            entityResponse.setMessage("Sermon updated successfully.");
            log.info("Sermon updated successfully.");
        } catch (Exception e) {
            log.error("Failed to update sermon.", e);
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("Failed to update sermon.");
        }

        return entityResponse;
    }

    public EntityResponse getUserInterests() {
        EntityResponse entityResponse = new EntityResponse();
        log.info("Fetching user interests...");

        try {
            // Fetch all user interests from the repository
            List<UserInterest> userInterests = userInterestRepository.findAll();

            // Check if the list is empty
            if (userInterests.isEmpty()) {
                entityResponse.setStatusCode(HttpStatus.NO_CONTENT.value());
                entityResponse.setMessage("No user interests found.");
                log.info("No user interests found.");
            } else {
                entityResponse.setData(userInterests);
                entityResponse.setStatusCode(HttpStatus.OK.value());
                entityResponse.setMessage("User interests fetched successfully.");
                log.info("User interests fetched successfully.");
            }
        } catch (Exception e) {
            log.error("Failed to fetch user interests.", e);
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("Failed to fetch user interests.");
        }

        return entityResponse;
    }
    public EntityResponse getUpcomingEvents() {
        EntityResponse entityResponse = new EntityResponse();
        log.info("Fetching upcoming events...");

        try {
            List<Events> events = eventRepository.findAll();
            entityResponse.setData(events);
            entityResponse.setStatusCode(HttpStatus.OK.value());
            entityResponse.setMessage("Upcoming events fetched successfully.");
            log.info("Upcoming events fetched successfully.");
        } catch (Exception e) {
            log.error("Failed to fetch upcoming events.", e);
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("Failed to fetch upcoming events.");
        }

        return entityResponse;
    }
}
