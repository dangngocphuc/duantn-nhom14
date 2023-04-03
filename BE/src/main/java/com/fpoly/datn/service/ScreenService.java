package com.fpoly.datn.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpoly.datn.entity.Screen;

@Service
public interface ScreenService {

    public Page<Screen> getPageScreens(Pageable pageable);

    public Screen findById(Long id);

    public boolean saveScreen(Screen screen);

    public boolean deleteScreen(Long id);

    public List<Screen> getListScreens();

}
