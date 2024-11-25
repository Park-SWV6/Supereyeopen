package com.example.demo.controller;


import com.example.demo.dto.HelpRequestDTO;
import com.example.demo.entity.HelpRequestEntity;
import com.example.demo.service.HelpRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/help-requests")
@RequiredArgsConstructor
public class HelpRequestController {
    private final HelpRequestService helpRequestService;

    @GetMapping
    public List<HelpRequestDTO> getAllHelpRequests() {
        return helpRequestService.getAllHelpRequests();
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
}
