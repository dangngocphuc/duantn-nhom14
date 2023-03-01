package com.fpoly.datn.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fpoly.datn.entity.Gpu;

public interface GpuService {

	public Page<Gpu> getPageGpu(Pageable pageable);

	public Gpu findById(Long id);

	public boolean saveGpu(Gpu gpu);

	public boolean deleteGpu(Long id);

	public List<Gpu> getListGpu();
}
