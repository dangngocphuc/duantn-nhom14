package com.example.DaPhone.ServiceImpl;

import com.example.DaPhone.Entity.Screen;
import com.example.DaPhone.Repository.ScreenRepo;
import com.example.DaPhone.Service.ScreenService;
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

public class ScreenServiceImpl implements ScreenService {

    @Autowired
    private ScreenRepo screenRepo;


    @Override
    public Page<Screen> getPageScreens(Pageable pageable) {
        Page<Screen> page = screenRepo.findAll(new Specification<Screen>() {
            @Override
            public Predicate toPredicate(Root<Screen> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                List<Predicate> predicates = new ArrayList<>();
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);
        return page;
    }

    @Override
    public Screen findById(Long id) {
        return screenRepo.findById(id).get();
    }

    @Override
    public boolean saveScreen(Screen screen) {
        screenRepo.save(screen);
        return true;
    }

    @Override
    public boolean deleteScreen(Long id) {
        return false;
    }

    @Override
    public List<Screen> getListScreens() {
        return screenRepo.findAll();
    }
}
