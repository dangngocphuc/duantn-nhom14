package com.example.DaPhone.Service;

import com.example.DaPhone.Entity.Colr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ColrService {
    public Page<Colr> getPageColor(Pageable pageable);

    public Colr findById(Long id);

    public boolean saveColor(Colr colr);

    public boolean deleteColor(Long id);

    public List<Colr> getListColors();

}
