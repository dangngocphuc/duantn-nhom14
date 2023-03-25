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

import com.fpoly.datn.common.CommonUtils;
import com.fpoly.datn.entity.Promotion;
import com.fpoly.datn.repository.PromotionRepo;
import com.fpoly.datn.service.PromotionService;

@Service
public class PromotionServiceImp implements PromotionService {

	@Autowired
	private PromotionRepo promotionRepo;

	@Override
	public Page<Promotion> getPagePromotion(Pageable pageable) {
		Page<Promotion> page = promotionRepo.findAll(new Specification<Promotion>() {
			@Override
			public Predicate toPredicate(Root<Promotion> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return page;
	}

	@Override
	public Promotion findById(Long id){
		return promotionRepo.findById(id).get();
	}

	@Override
	public boolean savePromotion(Promotion promotion) {
		if (promotion.getId() != null) {
//			Promotion.setUpdateDate(new Date());

			promotionRepo.save(promotion);
		} else {
//			Promotion.setCreateDate(new Date());
//			cpu.setStatus(1);
			promotion.setCode(CommonUtils.generatePromotionCode());
			promotionRepo.save(promotion);
		}
		return true;
	}

	@Override
	public boolean deletePromotion(Long id) {
		return false;
	}

	@Override
	public List<Promotion> getListPromotion() {
		List<Promotion> list = promotionRepo.findAll(new Specification<Promotion>() {
			@Override
			public Predicate toPredicate(Root<Promotion> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
//				predicates.add(cb.and(cb.equal(root.get("status"), 1)));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
		return list;
	}

	@Override
	public Promotion findByCode(String ma) {
		return promotionRepo.findByCode(ma);
	}

}
