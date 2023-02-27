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

import com.example.DaPhone.Entity.Rom;
import com.example.DaPhone.Repository.RomRepository;
import com.example.DaPhone.Service.RomService;

@Service
public class RomServiceImp implements RomService {

	@Autowired
	private RomRepository romRepo;

	@Override
	public Page<Rom> getPageRom(Pageable pageable) {
		Page<Rom> page = romRepo.findAll(new Specification<Rom>() {
			@Override
			public Predicate toPredicate(Root<Rom> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return page;
	}

	@Override
	public Rom findById(Long id) {
		return romRepo.findById(id).get();
	}

	@Override
	public boolean saveRom(Rom rom) {
		romRepo.save(rom);
		return true;
	}

	@Override
	public boolean deleteRom(Long id) {
		return false;
	}

	@Override
	public List<Rom> getListRom() {
		return romRepo.findAll();
	}

}
