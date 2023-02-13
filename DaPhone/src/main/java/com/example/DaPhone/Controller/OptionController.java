package com.example.DaPhone.Controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DaPhone.Dto.OptionDto;
import com.example.DaPhone.Entity.Option;
import com.example.DaPhone.Entity.OptionValue;
import com.example.DaPhone.Model.OptionRequest;
import com.example.DaPhone.Model.Response;
import com.example.DaPhone.Service.OptionService;

@RestController
@RequestMapping(path = "/api/option")
public class OptionController {

	@Autowired
	private OptionService optionService;

	public Option convertToEntity(OptionDto optionDto) {
		ModelMapper mapper = new ModelMapper();
		Option entity = new Option();
		mapper.map(optionDto, entity);
		return entity;
	}

	public OptionDto convertToDTO(Option entity) {
		ModelMapper mapper = new ModelMapper();
		OptionDto optionDto = new OptionDto();
		mapper.map(entity, optionDto);
		return optionDto;
	}

	@GetMapping(value = "")
	public ResponseEntity<Page<Option>> getOptions(Pageable pageable, OptionRequest optionRequest) {
		Page<Option> pageOption = optionService.findOptions(optionRequest, pageable);
		return new ResponseEntity<Page<Option>>(pageOption, HttpStatus.OK);
	}

	@PostMapping(value = "/save")
//	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response<Option>> saveOption(@RequestBody Option option) {
//		Option option = convertToEntity(optionDto);
		if (option != null) {
			return new ResponseEntity<Response<Option>>(new Response<Option>(toResponse(optionService.saveOption(option))),
					HttpStatus.OK);
		}
		return new ResponseEntity<Response<Option>>(new Response<Option>("loi", "10001"), HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Option> getOption(@PathVariable(name = "id") Long id) {
		return new ResponseEntity<Option>(toResponse(optionService.findById(id)), HttpStatus.OK);
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Boolean> deleteOption(@PathVariable(name = "id") Long id) {
		return new ResponseEntity<Boolean>(optionService.deleteOption(id), HttpStatus.OK);
	}
	
	@GetMapping(value = "/ngselect")
	public ResponseEntity<List<Option>> ngSelect(Pageable pageable, OptionRequest optionRequest) {
		List<Option> lstOption = optionService.ngSelect(pageable,optionRequest);
		return new ResponseEntity<List<Option>>(lstOption, HttpStatus.OK);
	}

	public Option toResponse(Option entity) {
		if(entity==null) {
			return null;
		}
		Option item = new Option();
		item.setId(entity.getId());
		item.setOptionCode(entity.getOptionCode());
		item.setOptionName(entity.getOptionName());
		item.setStatus(entity.getStatus());
		if (entity.getListOptionValue() != null) {
			Set<OptionValue> optionValues = new HashSet<OptionValue>();
			for (OptionValue e : entity.getListOptionValue()) {
				e.setOption(null);
				optionValues.add(e);
			}
			item.setListOptionValue(optionValues);
		}

		return item;
	}
}
