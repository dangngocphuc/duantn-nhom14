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

import com.fpoly.datn.entity.Option;
import com.fpoly.datn.entity.OptionValue;
import com.fpoly.datn.model.OptionRequest;
import com.fpoly.datn.model.OptionValueRequest;
import com.fpoly.datn.repository.OptionValueRepository;
import com.fpoly.datn.service.OptionService;
import com.fpoly.datn.service.OptionValueService;

@Service
public class OptionValueServiceImpl implements OptionValueService {

	@Autowired
	OptionValueRepository optionRepo;

	@Override
	public Page<OptionValue> findOptionValues(OptionValueRequest optionRequest, Pageable pageable) {
		Page<OptionValue> page = optionRepo.findAll(new Specification<OptionValue>() {
			@Override
			public Predicate toPredicate(Root<OptionValue> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				if (optionRequest.getOptionId() != null) {
					predicates.add(cb.and(cb.equal(root.get("option").get("id"), optionRequest.getOptionId())));
				}

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return page;
	}

	@Override
	public OptionValue findById(Long id) {
		OptionValue Option = optionRepo.findById(id).orElse(null);
		return Option;
	}

	@Override
	public OptionValue saveOptionValue(OptionValue option) {
		return optionRepo.save(option);
	}

	@Override
	public void deleteOptionValue(Long id) {
		// TODO Auto-generated method stub

	}

}
