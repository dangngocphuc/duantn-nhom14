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

import com.example.DaPhone.Entity.Ram;
import com.example.DaPhone.Repository.RamRepository;
import com.example.DaPhone.Service.RamService;

@Service
public class RamServiceImp implements RamService {

	@Autowired
	private RamRepository ramRepo;

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
		// TODO Auto-generated method stub
		return ramRepo.findById(id).get();
	}

	@Override
	public boolean saveRam(Ram ram) {
		ramRepo.save(ram);
		return true;
	}

	@Override
	public boolean deleteRam(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Ram> getListRam() {
		// TODO Auto-generated method stub
		return ramRepo.findAll();
	}

}
