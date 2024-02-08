package com.example.Eligibility.Repository;

import com.example.Eligibility.Entity.Eligibility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EligibilityRepository extends JpaRepository<Eligibility, Long> {
}
