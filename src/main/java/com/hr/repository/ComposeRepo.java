package com.hr.repository;

import com.hr.entity.Compose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComposeRepo extends JpaRepository<Compose,Integer> {

    public List<Compose> findByParentUkid(Integer parentUkid);

    // Add these new status count queries
    @Query("SELECT COUNT(c) FROM Compose c WHERE c.status = 'PENDING'")
    Long countPending();

    @Query("SELECT COUNT(c) FROM Compose c WHERE c.status = 'APPROVED'")
    Long countApproved();

    @Query("SELECT COUNT(c) FROM Compose c WHERE c.status = 'CANCELLED'")
    Long countCancelled();

    @Query("SELECT COUNT(c) FROM Compose c WHERE c.status = 'DENIED'")
    Long countDenied();

    @Query("SELECT COUNT(c) FROM Compose c")
    Long countAll();


}
