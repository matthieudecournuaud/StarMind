package com.nova.star.repository;

import com.nova.star.domain.RewardHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RewardHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RewardHistoryRepository extends JpaRepository<RewardHistory, Long> {}
