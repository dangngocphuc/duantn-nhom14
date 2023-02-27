package com.example.DaPhone.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.DaPhone.Entity.Rom;

public interface RomService {
	
	public Page<Rom> getPageRom(Pageable pageable);

	public Rom findById(Long id);

	public boolean saveRom(Rom Rom);

	public boolean deleteRom(Long id);

	public List<Rom> getListRom();
}
