package com.example.demo.service;

import com.example.demo.dto.DailyRankingDTO;
import com.example.demo.dto.GroupMemberRankingDTO;
import com.example.demo.dto.GroupRankingDTO;
import com.example.demo.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final RankingRepository rankingRepository;

    public List<DailyRankingDTO> getDailyRankings() {
        return rankingRepository.fetchDailyRankings();
    }

    public List<DailyRankingDTO> fetchDailyRankingWithRank() {
        List<DailyRankingDTO> rankings = rankingRepository.fetchDailyRankings();
        // studyTime 내림차순, userId 오름차순 정렬
        rankings.sort((a, b) -> {
            if (a.getStudyTime() == b.getStudyTime()) {
                return Long.compare(a.getUserId(), b.getUserId());
            }
            return Long.compare(b.getStudyTime(), a.getStudyTime());
        });

        int rank = 1;
        for (int i = 0; i < rankings.size(); i++) {
            if (i > 0 && rankings.get(i).getStudyTime() != rankings.get(i - 1).getStudyTime()) {
                rank = i + 1;
            }
            rankings.get(i).setRank(rank);
        }

        return rankings;
    }

    public List<GroupRankingDTO> getGroupRankings() {
        return rankingRepository.fetchGroupRankings();
    }

    public List<GroupRankingDTO> fetchGroupRankings() {
        List<GroupRankingDTO> rankings = rankingRepository.fetchGroupRankings();

        // rank 계산
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRank(i + 1);
        }

        return rankings;
    }

    public List<GroupMemberRankingDTO> getGroupMemberRankings(Long groupId) {
        List<GroupMemberRankingDTO> rankings = rankingRepository.fetchGroupMemberRankings(groupId);

        // rank 계산
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRank(i + 1);
        }

        return rankings;
    }

}
