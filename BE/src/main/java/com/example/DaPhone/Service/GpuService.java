package com.example.DaPhone.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.DaPhone.Entity.Gpu;

public interface GpuService {

	public Page<Gpu> getPageGpu(Pageable pageable);

	public Gpu findById(Long id);

	public boolean saveGpu(Gpu gpu);

	public boolean deleteGpu(Long id);

	public List<Gpu> getListGpu();
}
