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

import com.fpoly.datn.entity.Ram;
import com.fpoly.datn.repository.RamRepo;
import com.fpoly.datn.service.RamService;

@Service
public class RamServiceImp implements RamService {

	@Autowired
	private RamRepo ramRepo;

	@Override
	public Page<Ram> getPageRam(Pageable pageable) {
		Page<Ram> page = ramRepo.findAll(new Specification<Ram>() {
			@Override
			public Predicate toPredicate(Root<Ram> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return page;
	}

	@Override
	public Ram findById(Long id) {
		return ramRepo.findById(id).get();
	}

	@Override
	public boolean saveRam(Ram ram) {
		if (ram.getId() != null) {
			ram.setUpdateDate(new Date());
			ramRepo.save(ram);
		} else {
			ram.setCreateDate(new Date());
//			cpu.setStatus(1);
			ramRepo.save(ram);
		}
		return true;
	}

	@Override
	public boolean deleteRam(Long id) {
		return false;
	}

	@Override
	public List<Ram> getListRam() {
		List<Ram> list = ramRepo.findAll(new Specification<Ram>() {
			@Override
			public Predicate toPredicate(Root<Ram> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				predicates.add(cb.and(cb.equal(root.get("status"), 1)));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
		return list;
	}

}
