package com.example.DaPhone.ServiceImpl;

import com.example.DaPhone.Entity.Ram;
import com.example.DaPhone.Service.RamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.DaPhone.Repository.RamRepo;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class RamServiceImp  implements RamService {

    @Autowired
    private RamRepo ramRepo;

    @Override
    public Page<Ram> getPageRam(Pageable pageable) {
        Page<Ram> page = ramRepo.findAll(new Specification<Ram>() {
            @Override
            public Predicate toPredicate(Root<Ram> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                List<Predicate> predicates = new ArrayList<>();
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);
        return page;
    }

    @Override
    public Ram findById(Long id) {
        return ramRepo.findById(id).get();
    }

    @Override
    public boolean saveRam(Ram ram) {
        ramRepo.save(ram);
        return true;
    }

    @Override
    public boolean deleteRam(Long id) {
        return false;
    }

    @Override
    public List<Ram> getListRam() {
        return ramRepo.findAll();
    }


}
