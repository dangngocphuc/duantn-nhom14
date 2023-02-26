package com.example.DaPhone.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.DaPhone.Entity.Cpu;

public interface CpuService {
	
	public Page<Cpu> getPageCpu(Pageable pageable);

	public Cpu findById(Long id);

	public boolean saveCpu(Cpu cpu);

	public boolean deleteCpu(Long id);

	public List<Cpu> getListCpu();
}
