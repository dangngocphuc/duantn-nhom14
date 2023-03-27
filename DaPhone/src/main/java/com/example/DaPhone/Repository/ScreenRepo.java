package com.example.DaPhone.Repository;

import com.example.DaPhone.Entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ScreenRepo extends JpaRepository<Screen, Long>, JpaSpecificationExecutor<Screen> {
}
