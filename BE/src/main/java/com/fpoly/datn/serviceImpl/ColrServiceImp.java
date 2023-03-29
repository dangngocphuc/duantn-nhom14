package com.fpoly.datn.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fpoly.datn.entity.Colr;
import com.fpoly.datn.entity.Gpu;
import com.fpoly.datn.repository.ColrRepo;
import com.fpoly.datn.service.ColrService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ColrServiceImp implements ColrService {

    @Autowired
    private ColrRepo colrRepo;


    @Override
    public Page<Colr> getPageColor(Pageable pageable) {
        Page<Colr> page = colrRepo.findAll(new Specification<Colr>() {
            @Override
            public Predicate toPredicate(Root<Colr> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                List<Predicate> predicates = new ArrayList<>();
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);
        return page;
    }

    @Override
    public Colr findById(Long id) {
        return colrRepo.findById(id).get();
    }

    @Override
    public boolean saveColor(Colr colr) {
    	if (colr.getId() != null) {
    		colr.setUpdateDate(new Date());
    		colrRepo.save(colr);
		} else {
			colr.setCreateDate(new Date());
//			Gpu.setStatus(1);
			colrRepo.save(colr);
		}
		return true;
    }

    @Override
    public boolean deleteColor(Long id) {
        return false;
    }

    @Override
    public List<Colr> getListColors() {
    	List<Colr> list = colrRepo.findAll(new Specification<Colr>() {
			@Override
			public Predicate toPredicate(Root<Colr> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				predicates.add(cb.and(cb.equal(root.get("status"), 1)));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
		return list;
    }
}
