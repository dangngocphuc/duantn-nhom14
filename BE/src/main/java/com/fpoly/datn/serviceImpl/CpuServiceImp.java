package com.fpoly.datn.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fpoly.datn.entity.Cpu;
import com.fpoly.datn.repository.CpuRepository;
import com.fpoly.datn.service.CpuService;

@Service
public class CpuServiceImp implements CpuService {

	@Autowired
	private CpuRepository cpuRepo;

	// Lấy danh sách Cpu có phân trang
	@Override
	public Page<Cpu> getPageCpu(Pageable pageable) {
		Page<Cpu> page = cpuRepo.findAll(new Specification<Cpu>() {
			@Override
			public Predicate toPredicate(Root<Cpu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return page;
	}

	//Lấy Cpu theo id
	@Override
	public Cpu findById(Long id) {
		// TODO Auto-generated method stub
		return cpuRepo.findById(id).get();
	}

	//Thêm hoặc cập nhập Cpu
	@Override
	public boolean saveCpu(Cpu cpu) {
		if (cpu.getId() != null) {
			cpu.setUpdateDate(new Date());
			cpuRepo.save(cpu);
		} else {
			cpu.setCreateDate(new Date());
//			cpu.setStatus(1);
			cpuRepo.save(cpu);
		}
		return true;
	}

	//Không xóa mà cập nhật trạng thái về 0
	@Override
	public boolean deleteCpu(Long id) {
		Cpu cpu = cpuRepo.findById(id).get();
		if (cpu != null) {
			cpu.setStatus(0);
			return true;
		}
		return false;
	}

	
	// Lấy danh sách Cpu có trạng thái bằng 1
	@Override
	public List<Cpu> getListCpu() {
		List<Cpu> list = cpuRepo.findAll(new Specification<Cpu>() {
			@Override
			public Predicate toPredicate(Root<Cpu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				predicates.add(cb.and(cb.equal(root.get("status"), 1)));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
		return list;
	}

}
