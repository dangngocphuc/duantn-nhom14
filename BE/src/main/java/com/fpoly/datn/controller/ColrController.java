package com.fpoly.datn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.datn.entity.Colr;
import com.fpoly.datn.service.ColrService;

@RestController
@RequestMapping(path = "/api/color")
public class ColrController {
    @Autowired
    private ColrService colrService;

    @GetMapping(value = "")
    public ResponseEntity<Page<Colr>> getPageImei(Pageable pageable) {
        Page<Colr> pageCpu = colrService.getPageColor(pageable);
        return new ResponseEntity<Page<Colr>>(pageCpu, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Colr> getColorById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<Colr>(colrService.findById(id), HttpStatus.OK);
    }

    @PostMapping(value="")
    public ResponseEntity<Boolean> saveColor(@RequestBody Colr colr) {
        return new ResponseEntity<Boolean>(colrService.saveColor(colr), HttpStatus.OK);
    }


}
