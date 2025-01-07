package com.nova.star.repository;

import com.nova.star.domain.ProspectBoard;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProspectBoard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProspectBoardRepository extends JpaRepository<ProspectBoard, Long> {
    @Query("select prospectBoard from ProspectBoard prospectBoard where prospectBoard.owner.login = ?#{authentication.name}")
    List<ProspectBoard> findByOwnerIsCurrentUser();
}
