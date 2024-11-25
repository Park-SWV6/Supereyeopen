package com.example.demo.controller;


import com.example.demo.dto.DailyRankingDTO;
import com.example.demo.dto.GroupMemberRankingDTO;
import com.example.demo.dto.GroupRankingDTO;
import com.example.demo.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rankings")
@RequiredArgsConstructor
public class RankingController {
    private final RankingService rankingService;

    @GetMapping("/daily")
    public ResponseEntity<List<DailyRankingDTO>> getDailyRankings() {
        List<DailyRankingDTO> rankings = rankingService.fetchDailyRankingWithRank();
        return ResponseEntity.ok(rankings);
    }

    @GetMapping("/groups")
    public ResponseEntity<List<GroupRankingDTO>> getGroupRankings() {
        List<GroupRankingDTO> rankings = rankingService.fetchGroupRankings();
        return ResponseEntity.ok(rankings);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<GroupMemberRankingDTO>> getGroupMemberRankings(@PathVariable Long groupId) {
        return ResponseEntity.ok(rankingService.getGroupMemberRankings(groupId));
    }
}
