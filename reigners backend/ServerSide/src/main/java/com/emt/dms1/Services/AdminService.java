package com.emt.dms1.Services;


import com.emt.dms1.Models.*;
import com.emt.dms1.Repository.*;
import com.emt.dms1.utils.EntityResponse;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;



@Slf4j
@Service
public class AdminService {

@Autowired
    private AdminRepository adminRepository;
@Autowired
   private static UserInterestRepository userInterestRepository;
@Autowired
    private SermonRepository sermonRepository;
   private EventRepository eventRepository;
   @Autowired
   private WelcomeRepo welcomeRepo;
    private final Path rootLocation = Paths.get("upload-dir");

    public AdminService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;}
    public EntityResponse postWelcomeMessage(String message) {
        EntityResponse entityResponse = new EntityResponse();
        log.info("Posting new welcome message...");

        try {
            // Check if a welcome message already exists
            Optional<WelcomeMessageRequest> existingMessage = welcomeRepo.findById(1L); // Assuming ID 1 is used for the welcome message

            if (existingMessage.isPresent()) {
                entityResponse.setStatusCode(HttpStatus.CONFLICT.value());
                entityResponse.setMessage("Welcome message already exists.");
                log.info("Welcome message already exists.");
            } else {
                // Create a new welcome message
                WelcomeMessageRequest newMessage = new WelcomeMessageRequest();
                newMessage.setId(1L); // Assuming ID 1 is used for the welcome message
                newMessage.setMessage(message);

                // Save the new welcome message
                welcomeRepo.save(newMessage);

                entityResponse.setStatusCode(HttpStatus.CREATED.value());
                entityResponse.setMessage("Welcome message posted successfully.");
                entityResponse.setData(newMessage);
                log.info("Welcome message posted successfully.");
            }
        } catch (Exception e) {
            log.error("Failed to post welcome message.", e);
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("Failed to post welcome message.");
        }

        return entityResponse;
    }

    public EntityResponse getWelcomeMessage() {
        EntityResponse entityResponse = new EntityResponse();
        log.info("Fetching welcome message...");

        try {
            // Assuming ID 1 is used for the welcome message
            Optional<WelcomeMessageRequest> welcomeMessageRequest = welcomeRepo.findById(1L);

            if (welcomeMessageRequest.isPresent()) {
                String message = welcomeMessageRequest.get().getMessage();
                entityResponse.setData(message);
                entityResponse.setStatusCode(HttpStatus.OK.value());
                entityResponse.setMessage("Welcome message fetched successfully.");
                log.info("Welcome message fetched successfully: {}", message);
            } else {
                entityResponse.setStatusCode(HttpStatus.NO_CONTENT.value());
                entityResponse.setMessage("No welcome message found.");
                log.info("No welcome message found.");
            }
        } catch (Exception e) {
            log.error("Failed to fetch welcome message.", e);
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("Failed to fetch welcome message.");
        }

        return entityResponse;
    }



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


public EntityResponse addEvent(String event, MultipartFile imageFile, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    EntityResponse entityResponse = new EntityResponse<>();
    log.info("Adding new event...");

    try {
        Events newEvent = new Events();
        newEvent.setEvent(event);
        newEvent.setDate(date); // Set the date for the event

        if (imageFile != null && !imageFile.isEmpty()) {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
            Path destinationFile = rootLocation.resolve(Paths.get(imageFile.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            Files.copy(imageFile.getInputStream(), destinationFile);
            newEvent.setImageUrl(destinationFile.toString());
        }

        eventRepository.save(newEvent);

        entityResponse.setStatusCode(HttpStatus.CREATED.value());
        entityResponse.setMessage("Event added successfully.");
        entityResponse.setData(newEvent);
        log.info("Event added successfully.");
    } catch (IOException e) {
        log.error("Failed to save image file.", e);
        entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        entityResponse.setMessage("Failed to save image file.");
    } catch (Exception e) {
        log.error("Failed to add event.", e);
        entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        entityResponse.setMessage("Failed to add event.");
    }

    return entityResponse;
}

    public EntityResponse deleteEventById(Long eventId) {
        EntityResponse entityResponse = new EntityResponse<>();
        log.info("Deleting event with ID: {}", eventId);

        try {
            Optional<Events> eventOptional = eventRepository.findById(eventId);

            if (eventOptional.isPresent()) {
                eventRepository.deleteById(eventId);
                entityResponse.setStatusCode(HttpStatus.OK.value());
                entityResponse.setMessage("Event deleted successfully.");
                log.info("Event deleted successfully with ID: {}", eventId);
            } else {
                entityResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
                entityResponse.setMessage("Event not found.");
                log.info("Event not found with ID: {}", eventId);
            }
        } catch (Exception e) {
            log.error("Failed to delete event with ID: {}", eventId, e);
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("Failed to delete event.");
        }

        return entityResponse;
    }

    public void deletePastEvents() {
        List<Events> pastEvents = eventRepository.findAll().stream()
                .filter(event -> event.getDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());

        if (!pastEvents.isEmpty()) {
            eventRepository.deleteAll(pastEvents);
            log.info("Deleted past events.");
        }


}
    public EntityResponse getUpcomingEvents() {
        EntityResponse entityResponse = new EntityResponse();
        log.info("Fetching upcoming events...");

        try {
            // Fetch upcoming events
            List<Events> upcomingEvents = eventRepository.findAll(); // Ensure Event class is used here

            if (!upcomingEvents.isEmpty()) {
                entityResponse.setData(upcomingEvents);
                entityResponse.setStatusCode(HttpStatus.OK.value());
                entityResponse.setMessage("Upcoming events fetched successfully.");
                log.info("Upcoming events fetched successfully.");
            } else {
                entityResponse.setStatusCode(HttpStatus.NO_CONTENT.value());
                entityResponse.setMessage("No upcoming events found.");
                log.info("No upcoming events found.");
            }
        } catch (Exception e) {
            log.error("Failed to fetch upcoming events.", e);
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("Failed to fetch upcoming events.");
        }

        return entityResponse;
    }
    public EntityResponse postLiveStreamUrl(String url) {
        EntityResponse entityResponse = new EntityResponse();
        log.info("Posting live stream URL: {}", url);

        try {
            // Validate the YouTube URL
            if (url == null || !url.startsWith("https://www.youtube.com/watch")) {
                entityResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
                entityResponse.setMessage("Invalid YouTube URL.");
                log.warn("Invalid YouTube URL: {}", url);
                return entityResponse;
            }
            entityResponse.setStatusCode(HttpStatus.OK.value());
            entityResponse.setMessage("Live stream URL posted successfully.");
            entityResponse.setData(url);
            log.info("Live stream URL posted successfully: {}", url);
        } catch (Exception e) {
            log.error("Failed to post live stream URL.", e);
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("Failed to post live stream URL.");
        }

        return entityResponse;
    }

    public EntityResponse getLiveStreamUrl() {
        EntityResponse entityResponse = new EntityResponse();
        log.info("Fetching live stream URL...");

        try {

            // If admin check is not required, directly fetch the URL from a static configuration or another source
            String liveStreamUrl = "https://www.youtube.com/watch?v=YOUR_DEFAULT_VIDEO_ID"; // Replace with your logic

            if (liveStreamUrl == null || liveStreamUrl.isEmpty()) {
                entityResponse.setStatusCode(HttpStatus.NO_CONTENT.value());
                entityResponse.setMessage("No live stream URL found.");
                log.info("No live stream URL found.");
            } else {
                entityResponse.setStatusCode(HttpStatus.OK.value());
                entityResponse.setMessage("Live stream URL fetched successfully.");
                entityResponse.setData(liveStreamUrl);
                log.info("Live stream URL fetched successfully: {}", liveStreamUrl);
            }
        } catch (Exception e) {
            log.error("Failed to fetch live stream URL.", e);
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("Failed to fetch live stream URL.");
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
    public EntityResponse uploadSermon(String title, String videoUrl, MultipartFile notesFile) {
        EntityResponse entityResponse = new EntityResponse();
        log.info("Uploading sermon: {}", title);

        try {
            Sermons sermon = new Sermons();
            sermon.setTitle(title);
            sermon.setVideoUrl(videoUrl);

            if (notesFile != null && !notesFile.isEmpty()) {
                sermon.setNotesFile(notesFile.getBytes());
            }

            sermonRepository.save(sermon);

            entityResponse.setStatusCode(HttpStatus.OK.value());
            entityResponse.setMessage("Sermon uploaded successfully.");
            log.info("Sermon uploaded successfully: {}", title);
        } catch (Exception e) {
            log.error("Failed to upload sermon.", e);
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("Failed to upload sermon.");
        }


        return entityResponse;
    }

    public EntityResponse getSermons() {
        EntityResponse entityResponse = new EntityResponse();
        log.info("Fetching sermons...");

        try {
            List<Sermons> sermons = sermonRepository.findAll();

            if (!sermons.isEmpty()) {
                entityResponse.setData(sermons);
                entityResponse.setStatusCode(HttpStatus.OK.value());
                entityResponse.setMessage("Sermons fetched successfully.");
                log.info("Sermons fetched successfully.");
            } else {
                entityResponse.setStatusCode(HttpStatus.NO_CONTENT.value());
                entityResponse.setMessage("No sermons found.");
                log.info("No sermons found.");
            }
        } catch (Exception e) {
            log.error("Failed to fetch sermons.", e);
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("Failed to fetch sermons.");
        }

        return entityResponse;
    }
    public static EntityResponse submitUserInterests(String name, long phoneNumber, List<String> interests) {
        EntityResponse entityResponse = new EntityResponse();
        try {
            // Create a new UserInterest entity
            UserInterest userInterest = new UserInterest();
            userInterest.setName(name);
            userInterest.setPhoneNumber(phoneNumber);
            userInterest.setInterests(interests); // Assuming this is a List<String>

            // Save the UserInterest to the database
            userInterestRepository.save(userInterest);

            entityResponse.setStatusCode(HttpStatus.OK.value());
            entityResponse.setMessage("User interests submitted successfully.");
            entityResponse.setData(userInterest);
        } catch (Exception e) {
            log.error("Failed to submit user interests.", e);
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            entityResponse.setMessage("Failed to submit user interests.");
        }
        return entityResponse;
    }

}
