package com.example.DaPhone.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.DaPhone.Entity.Cpu;
import com.example.DaPhone.Entity.Imei;
import com.example.DaPhone.Entity.ProductDetail;
import com.example.DaPhone.Repository.CpuRepository;
import com.example.DaPhone.Request.ImeiRequest;
import com.example.DaPhone.Service.CpuService;

@Service
public class CpuServiceImp implements CpuService {

	@Autowired
	private CpuRepository cpuRepo;

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

	@Override
	public Cpu findById(Long id) {
		// TODO Auto-generated method stub
		return cpuRepo.findById(id).get();
	}

	@Override
	public boolean saveCpu(Cpu cpu) {
		cpuRepo.save(cpu);
		return true;
	}

	@Override
	public boolean deleteCpu(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Cpu> getListCpu() {
		// TODO Auto-generated method stub
		return cpuRepo.findAll();
	}

}
