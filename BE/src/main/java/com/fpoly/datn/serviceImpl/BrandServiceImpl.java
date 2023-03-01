package com.fpoly.datn.serviceImpl;

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

import com.fpoly.datn.entity.Brand;
import com.fpoly.datn.repository.BrandRepo;
import com.fpoly.datn.request.BrandRequest;
import com.fpoly.datn.service.BrandService;
@Service
public class BrandServiceImpl implements BrandService{
	
	@Autowired
	private BrandRepo brandRepo;
	
	@Override
	public Page<Brand> findBrand(BrandRequest brandParam, Pageable pageable) {
		Page<Brand> listPage = brandRepo.findAll(new Specification<Brand>() {
			@Override
			public Predicate toPredicate(Root<Brand> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if (brandParam.getBrandName() != null && !brandParam.getBrandName().equals("")) {
					predicates.add(cb.and(cb.like(cb.upper(root.<String>get("tenHang")),
							"%" + brandParam.getBrandName().trim().toUpperCase() + "%")));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return listPage;
	}
	
	@Override
	public List<Brand> findAll(){
		return brandRepo.findAll();
	}
	@Override
	public Brand saveBrand(Brand brand) {
		return brandRepo.save(brand);
	}
	@Override
	public void deleteBrand(Long id) {
		brandRepo.deleteById(id);
	}

	@Override
	public Brand findById(Long id) {
		// TODO Auto-generated method stub
		return brandRepo.findById(id).get();
	}
	
}
