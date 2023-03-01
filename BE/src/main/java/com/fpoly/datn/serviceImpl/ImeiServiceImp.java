package com.fpoly.datn.serviceImpl;

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

import com.fpoly.datn.entity.Imei;
import com.fpoly.datn.entity.Option;
import com.fpoly.datn.entity.ProductDetail;
import com.fpoly.datn.repository.ImeiRepository;
import com.fpoly.datn.request.ImeiRequest;
import com.fpoly.datn.service.ImeiService;

@Service
public class ImeiServiceImp implements ImeiService {

	@Autowired
	private ImeiRepository imeiRepo;

	@Override
	public Page<Imei> getPageImei(ImeiRequest request, Pageable pageable) {
		Page<Imei> page = imeiRepo.findAll(new Specification<Imei>() {
			@Override
			public Predicate toPredicate(Root<Imei> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				Join<Imei, ProductDetail> join = root.join("productDetail", JoinType.LEFT);
				List<Predicate> predicates = new ArrayList<>();
				if (request.getImei() != null && !request.getImei().equals("")) {
					predicates.add(cb.and(cb.like(cb.upper(root.<String>get("imei")),
							"%" + request.getImei().trim().toUpperCase() + "%")));
				}
				if (request.getProductId() != null) {
					predicates.add(cb.and(cb.equal(join.get("id"), request.getProductId())));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		
		for (Imei imei : page) {
			if(imei.getProductDetail() != null) {
				imei.getProductDetail().setListImei(null);
				imei.getProductDetail().setProduct(null);
//				imei.getProductDetail().setListProductDetailValue(null);
				imei.setProductName(imei.getProductDetail().getProductName());
			}
			imei.setBillDetail(null);
		}
		
		return page;
	}

	@Override
	public Imei findById(Long id) {
		Imei imei = imeiRepo.findById(id).get();
		return imei;
	}

	@Override
	public boolean saveImei(Imei imei) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteImei(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

}
