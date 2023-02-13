package com.example.DaPhone.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.DaPhone.Entity.OptionValue;
import com.example.DaPhone.Model.OptionValueRequest;

public interface OptionValueService {
	public Page<OptionValue> findOptionValues(OptionValueRequest optionRequest, Pageable pageable);
	public OptionValue findById(Long id);
	public OptionValue saveOptionValue(OptionValue option);
	public void deleteOptionValue(Long id);
}
