package com.example.DaPhone.Service;
;
import com.example.DaPhone.Entity.Screen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScreenService {

    public Page<Screen> getPageScreens(Pageable pageable);

    public Screen findById(Long id);

    public boolean saveScreen(Screen screen);

    public boolean deleteScreen(Long id);

    public List<Screen> getListScreens();

}
