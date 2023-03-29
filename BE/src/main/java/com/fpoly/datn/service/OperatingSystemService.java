package com.fpoly.datn.service;

import com.fpoly.datn.entity.OperatingSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OperatingSystemService {

    public Page<com.fpoly.datn.entity.OperatingSystem> getPageOperatingSystem(Pageable pageable);

    public OperatingSystem findById(Long id);

    public boolean saveOperatingSystem(OperatingSystem oSystem);

    public boolean deleteColor(Long id);

    public List<OperatingSystem> getListColors();
}
