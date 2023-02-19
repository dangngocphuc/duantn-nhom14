package com.example.DaPhone.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.DaPhone.Entity.Option;
import com.example.DaPhone.Model.OptionRequest;

public interface OptionService {
	public Page<Option> findOptions(OptionRequest optionRequest, Pageable pageable);
	public Option findById(Long id);
	public Option saveOption(Option option);
	public boolean deleteOption(Long id);
	public List<Option> ngSelect(Pageable pageable, OptionRequest optionRequest);
	public List<Option> getListOption();
}
