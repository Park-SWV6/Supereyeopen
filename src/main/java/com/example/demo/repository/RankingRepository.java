package com.example.demo.repository;

import com.example.demo.dto.DailyRankingDTO;
import com.example.demo.dto.GroupMemberRankingDTO;
import com.example.demo.dto.GroupRankingDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RankingRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<DailyRankingDTO> fetchDailyRankings() {
        String query = """
            SELECT new com.example.demo.dto.DailyRankingDTO(
                u.userName,
                u.studyTime,
                sg.name
            )
            FROM UserEntity u
            LEFT JOIN u.studyGroup sg
            ORDER BY u.studyTime DESC
                """;
        return entityManager.createQuery(query, DailyRankingDTO.class).getResultList();
    }

    public List<GroupRankingDTO> fetchGroupRankings() {
        String query = """
                SELECT new com.example.demo.dto.GroupRankingDTO(
                    0,
                    sg.name,
                    SUM(u.studyTime),
                    COUNT(u.id),
                    sg.limit,
                    sg.leader.userName
                )
                FROM StudyGroupEntity sg
                JOIN sg.members u
                GROUP BY sg.id, sg.name, sg.limit, sg.leader.userName
                ORDER BY SUM(u.studyTime) DESC
                """;
        return entityManager.createQuery(query, GroupRankingDTO.class).getResultList();
    }

    public List<GroupMemberRankingDTO> fetchGroupMemberRankings(Long groupId) {
        String query = """
                SELECT new com.example.demo.dto.GroupMemberRankingDTO(
                    0,
                    u.userName,
                    u.studyTime,
                    sg.name
                )
                FROM UserEntity u
                JOIN u.studyGroup sg
                WHERE sg.id = :groupId
                ORDER BY u.studyTime DESC
                """;
        return entityManager.createQuery(query, GroupMemberRankingDTO.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }
}
