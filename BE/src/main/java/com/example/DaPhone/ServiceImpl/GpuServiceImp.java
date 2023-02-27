package com.example.DaPhone.ServiceImpl;

import java.util.ArrayList;
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

import com.example.DaPhone.Entity.Gpu;
import com.example.DaPhone.Repository.GpuRepository;
import com.example.DaPhone.Service.GpuService;

@Service
public class GpuServiceImp implements GpuService {

	@Autowired
	private GpuRepository gpuRepo;

	@Override
	public Page<Gpu> getPageGpu(Pageable pageable) {
		Page<Gpu> page = gpuRepo.findAll(new Specification<Gpu>() {
			@Override
			public Predicate toPredicate(Root<Gpu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return page;
	}

	@Override
	public Gpu findById(Long id) {
		return gpuRepo.findById(id).get();
	}

	@Override
	public boolean saveGpu(Gpu Gpu) {
		gpuRepo.save(Gpu);
		return true;
	}

	@Override
	public boolean deleteGpu(Long id) {
		return false;
	}

	@Override
	public List<Gpu> getListGpu() {
		return gpuRepo.findAll();
	}

}
