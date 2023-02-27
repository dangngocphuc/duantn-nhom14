package com.example.DaPhone.ServiceImpl;

import com.example.DaPhone.Entity.Colr;
import com.example.DaPhone.Repository.ColrRepo;
import com.example.DaPhone.Service.ColrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class ColrServiceImp implements ColrService {

    @Autowired
    private ColrRepo colrRepo;


    @Override
    public Page<Colr> getPageColor(Pageable pageable) {
        Page<Colr> page = colrRepo.findAll(new Specification<Colr>() {
            @Override
            public Predicate toPredicate(Root<Colr> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                List<Predicate> predicates = new ArrayList<>();
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);
        return page;
    }

    @Override
    public Colr findById(Long id) {
        return colrRepo.findById(id).get();
    }

    @Override
    public boolean saveColor(Colr colr) {
        colrRepo.save(colr);
        return true;
    }

    @Override
    public boolean deleteColor(Long id) {
        return false;
    }

    @Override
    public List<Colr> getListColors() {
        return colrRepo.findAll();
    }
}
