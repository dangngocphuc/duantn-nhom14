package com.example.DaPhone.ServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.DaPhone.Entity.Option;
import com.example.DaPhone.Entity.OptionValue;
import com.example.DaPhone.Model.OptionRequest;
import com.example.DaPhone.Repository.OptionRepository;
import com.example.DaPhone.Service.OptionService;

@Service
public class OptionServiceImpl implements OptionService {

	@Autowired
	OptionRepository optionRepo;

	@Override
	public Page<Option> findOptions(OptionRequest optionRequest, Pageable pageable) {
		Page<Option> page = optionRepo.findAll(new Specification<Option>() {
			@Override
			public Predicate toPredicate(Root<Option> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				if (optionRequest.getOptionMa() != null && !optionRequest.getOptionMa().equals("")) {
					predicates.add(cb.and(cb.like(cb.upper(root.<String>get("optionCode")),
							"%" + optionRequest.getOptionMa().trim().toUpperCase() + "%")));
				}
				if (optionRequest.getOptionTen() != null && !optionRequest.getOptionTen().equals("")) {
					predicates.add(cb.and(cb.like(cb.upper(root.<String>get("optionName")),
							"%" + optionRequest.getOptionTen().trim().toUpperCase() + "%")));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		for (Option pro : page) {
//			pro.setListProductDetailValue(null);
			pro.setListOptionValue(null);
		}
		return page;
	}

	@Override
	public Option findById(Long id) {
		Option Option = optionRepo.getById(id);
		return Option;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public Option saveOption(Option option) {
		if (option.getId() != null) {
			// update
			Option newOption = updateOption(option);
			if (option.getListOptionValue().size() > 0) {
				if (newOption.getListOptionValue() != null) {
					for (OptionValue e : newOption.getListOptionValue()) {
						e.setOption(newOption);
					}
				}
			}
			optionRepo.save(newOption);
			return newOption;
		} else { // new
			Option optionNew = optionRepo.save(option);
			if (option.getListOptionValue().size() > 0) {
				if (option.getListOptionValue() != null) {
					for (OptionValue opValue : option.getListOptionValue()) {
						opValue.setOption(optionNew);
					}
				}
			}
			return optionNew;
		}

	}

	@Override
	public boolean deleteOption(Long id) {
		try {
			optionRepo.deleteById(id);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public Option updateOption(Option option) {
		Option op = optionRepo.findById(option.getId()).get();
		op.setOptionName(option.getOptionName());
		op.setOptionCode(option.getOptionCode());
		op.setStatus(option.getStatus());
		op.getListOptionValue().clear();
		op.getListOptionValue().addAll(option.getListOptionValue());
		return op;
	}

	public Option toResponse(Option entity) {
//		Option item = new Option();
//		item.setId(entity.getId());
//		item.setOptionCode(entity.getOptionCode());
//		item.setOptionName(entity.getOptionName());
//		item.setStatus(entity.getStatus());
//		if (!entity.getListProductDetailValue().isEmpty()) {
//			entity.setListProductDetailValue(null);
//			item.setListProductDetailValue(null);
//		}
//		if (entity.getListOptionValue() != null) {
//			for (OptionValue e : entity.getListOptionValue()) {
//				if(!e.getListProductDetailValue().isEmpty()) {
//					e.setListProductDetailValue(null);
//				}
//				e.setOption(null);
//			}
//			item.setListOptionValue(entity.getListOptionValue());
//		}

		Option item = new Option();
		item.setId(entity.getId());
		item.setOptionCode(entity.getOptionCode());
		item.setOptionName(entity.getOptionName());
		item.setStatus(entity.getStatus());
//		item.setListProductDetailValue(null);

		if (entity.getListOptionValue() != null) {
			Set<OptionValue> optionValues = new HashSet<OptionValue>();
			for (OptionValue e : entity.getListOptionValue()) {
//		        e.setListProductDetailValue(null);
				e.setOption(null);
				optionValues.add(e);
			}
			item.setListOptionValue(optionValues);
		}

		return item;
	}

	@Override
	public List<Option> ngSelect(Pageable pageable, OptionRequest optionRequest) {
		Page<Option> page = optionRepo.findAll(new Specification<Option>() {
			@Override
			public Predicate toPredicate(Root<Option> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (optionRequest != null) {
					if (optionRequest.getOptionTen() != null && !optionRequest.getOptionTen().equals("")) {
						predicates.add(criteriaBuilder.or(
								criteriaBuilder.like(criteriaBuilder.upper(root.<String>get("optionName")),
										"%" + optionRequest.getOptionTen().trim().toUpperCase() + "%"),
								criteriaBuilder.like(root.<String>get("optionCode"),
										"%" + optionRequest.getOptionTen() + "%")));
					}
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		for (Option pro : page) {
//			pro.setListProductDetailValue(null);
			pro.setListOptionValue(null);
		}
		return page.getContent();
	}

	@Override
	public List<Option> getListOption() {
		List<Option> page =  optionRepo.findAll();
		for (Option pro : page) {
			if(!pro.getListOptionValue().isEmpty()) {
				pro.getListOptionValue().forEach(e->{
					e.setOption(null);
				});
			}
		}
		return page;
	}
}
