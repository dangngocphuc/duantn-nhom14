package com.fpoly.datn.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fpoly.datn.entity.Cpu;

public interface CpuService {
	
	public Page<Cpu> getPageCpu(Pageable pageable);

	public Cpu findById(Long id);

	public boolean saveCpu(Cpu cpu);

	public boolean deleteCpu(Long id);

	public List<Cpu> getListCpu();
}
