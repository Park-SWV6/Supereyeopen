package com.example.demo.controller;


import com.example.demo.dto.HelpRequestDTO;
import com.example.demo.entity.HelpRequestEntity;
import com.example.demo.service.HelpRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/help-requests")
@RequiredArgsConstructor
public class HelpRequestController {
    private final HelpRequestService helpRequestService;

    @GetMapping
    public List<HelpRequestDTO> getAllHelpRequests() {
        return helpRequestService.getAllHelpRequests();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HelpRequestDTO> getHelpRequestId(@PathVariable Long id) {
        try {
            HelpRequestDTO helpRequest = helpRequestService.getHelpRequestById(id);
            return ResponseEntity.ok(helpRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @PostMapping
    public HelpRequestDTO createHelpRequest(
            @RequestBody HelpRequestDTO helpRequest,
            @RequestParam Long userId) {
        return helpRequestService.saveHelpRequest(helpRequest, userId);
    }

    @PutMapping("/{id}")
    public HelpRequestDTO updateHelpRequest(
            @PathVariable Long id,
            @RequestBody HelpRequestDTO updatedRequest,
            @RequestParam Long userId
    ) {
        return helpRequestService.updateHelpRequest(id, updatedRequest, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHelpRequest(@PathVariable Long id) {
        helpRequestService.deleteHelpRequest(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/increment-comments")
    public ResponseEntity<Void> incrementComments(@PathVariable Long id) {
        helpRequestService.updateCommentsCount(id, 1);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/decrement-comments")
    public ResponseEntity<Void> decrementComments(@PathVariable Long id) {
        helpRequestService.updateCommentsCount(id, -1);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<Map<String, String>> uploadHelpRequestImage(
            @PathVariable Long id,
            @RequestParam("file")MultipartFile file
            ) {
        try {
            String imageUri = helpRequestService.uploadHelpRequestImage(id, file);

            Map<String, String> response = new HashMap<>();
            response.put("imageUri", imageUri);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload image: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/delete-images")
    public ResponseEntity<Void> deleteHelpRequestImage(
            @PathVariable Long id,
            @RequestBody List<String> imageUris
    ) {
        try {
            helpRequestService.deleteHelpRequestImages(id, imageUris);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
