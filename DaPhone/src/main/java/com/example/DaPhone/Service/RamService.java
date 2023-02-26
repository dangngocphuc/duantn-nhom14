package com.example.DaPhone.Service;

import com.example.DaPhone.Entity.Ram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface  RamService {

    public Page<Ram> getPageRam(Pageable pageable);

    public Ram findById(Long id);

    public boolean saveRam(Ram ram);

    public boolean deleteRam(Long id);

    public List<Ram> getListRam();
}
