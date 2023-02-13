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

import com.example.DaPhone.Entity.Option;
import com.example.DaPhone.Entity.OptionValue;
import com.example.DaPhone.Model.OptionRequest;
import com.example.DaPhone.Model.OptionValueRequest;
import com.example.DaPhone.Repository.OptionValueRepository;
import com.example.DaPhone.Service.OptionService;
import com.example.DaPhone.Service.OptionValueService;

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
