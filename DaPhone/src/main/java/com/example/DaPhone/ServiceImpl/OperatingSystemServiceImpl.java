package com.example.DaPhone.ServiceImpl;

import com.example.DaPhone.Entity.OperatingSystem;
import com.example.DaPhone.Repository.OperatingSystemRepo;
import com.example.DaPhone.Service.OperatingSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class OperatingSystemServiceImpl implements OperatingSystemService {
    @Autowired
    private OperatingSystemRepo oSystemRepo;

    @Override
    public Page<OperatingSystem> getPageOperatingSystem(Pageable pageable) {
        Page<OperatingSystem> page = oSystemRepo.findAll(new Specification<OperatingSystem>() {
            @Override
            public Predicate toPredicate(Root<OperatingSystem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                List<Predicate> predicates = new ArrayList<>();
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);
        return page;
    }

    @Override
    public OperatingSystem findById(Long id) {
        return oSystemRepo.findById(id).get();
    }

    @Override
    public boolean saveOperatingSystem(OperatingSystem opSystem) {
        oSystemRepo.save(opSystem);
        return true;
    }

    @Override
    public boolean deleteColor(Long id) {
        return false;
    }

    @Override
    public List<OperatingSystem> getListColors() {
        return oSystemRepo.findAll();
    }
}
