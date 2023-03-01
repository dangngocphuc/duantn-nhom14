package com.fpoly.datn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fpoly.datn.entity.Ram;

import java.util.List;

public interface  RamService {

    public Page<Ram> getPageRam(Pageable pageable);

    public Ram findById(Long id);

    public boolean saveRam(Ram ram);

    public boolean deleteRam(Long id);

    public List<Ram> getListRam();
}
