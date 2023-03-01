package com.fpoly.datn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fpoly.datn.entity.OptionValue;
import com.fpoly.datn.model.OptionValueRequest;

public interface OptionValueService {
	public Page<OptionValue> findOptionValues(OptionValueRequest optionRequest, Pageable pageable);
	public OptionValue findById(Long id);
	public OptionValue saveOptionValue(OptionValue option);
	public void deleteOptionValue(Long id);
}
