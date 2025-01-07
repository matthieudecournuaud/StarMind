package com.nova.star.repository;

import com.nova.star.domain.ProspectEntry;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProspectEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProspectEntryRepository extends JpaRepository<ProspectEntry, Long> {
    @Query("select prospectEntry from ProspectEntry prospectEntry where prospectEntry.prospect.login = ?#{authentication.name}")
    List<ProspectEntry> findByProspectIsCurrentUser();
}
