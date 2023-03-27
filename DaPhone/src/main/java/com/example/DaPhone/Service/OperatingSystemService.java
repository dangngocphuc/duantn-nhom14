package com.example.DaPhone.Service;

import com.example.DaPhone.Entity.OperatingSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OperatingSystemService {

    public Page<OperatingSystem> getPageOperatingSystem(Pageable pageable);

    public OperatingSystem findById(Long id);

    public boolean saveOperatingSystem(OperatingSystem oSystem);

    public boolean deleteColor(Long id);

    public List<OperatingSystem> getListColors();
}
