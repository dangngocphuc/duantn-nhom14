package com.fpoly.datn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpoly.datn.entity.Promotion;
import com.fpoly.datn.service.PromotionService;

@RestController
@RequestMapping(path = "/api/promotion")
public class PromotionController {
    @Autowired
    private PromotionService promotionService;

    @GetMapping(value = "")
    public ResponseEntity<Page<Promotion>> getPagePromotion(Pageable pageable) {
        Page<Promotion> pagePromotion = promotionService.getPagePromotion(pageable);
        return new ResponseEntity<Page<Promotion>>(pagePromotion, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Promotion> getPromotionById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<Promotion>(promotionService.findById(id), HttpStatus.OK);
    }
    
    @GetMapping(value = "/code/{code}")
    public ResponseEntity<Promotion> getPromotionByCode(@PathVariable(name = "code") String code) {
        return new ResponseEntity<Promotion>(promotionService.findByCode(code), HttpStatus.OK);
    }

    @PostMapping(value="")
    public ResponseEntity<Boolean> savePromotion(@RequestBody Promotion Promotion) {
        return new ResponseEntity<Boolean>(promotionService.savePromotion(Promotion), HttpStatus.OK);
    }
    
    @GetMapping(value = "/list")
    public ResponseEntity<List<Promotion>> getListImei() {
        List<Promotion> pagePromotion = promotionService.getListPromotion();
        return new ResponseEntity<List<Promotion>>(pagePromotion, HttpStatus.OK);
    }
}
