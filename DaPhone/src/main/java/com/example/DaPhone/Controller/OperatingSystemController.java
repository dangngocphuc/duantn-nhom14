package com.example.DaPhone.Controller;


import com.example.DaPhone.Entity.OperatingSystem;
import com.example.DaPhone.Service.OperatingSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/operating_system")
public class OperatingSystemController {

    @Autowired
    private OperatingSystemService operatingSystemService;

    @GetMapping(value = "")
    public ResponseEntity<Page<OperatingSystem>> getPageImei(Pageable pageable) {
        Page<OperatingSystem> pageCpu = operatingSystemService.getPageOperatingSystem(pageable);
        return new ResponseEntity<Page<OperatingSystem>>(pageCpu, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OperatingSystem> getColorById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<OperatingSystem>(operatingSystemService.findById(id), HttpStatus.OK);
    }

    @PostMapping(value="")
    public ResponseEntity<Boolean> saveColor(@RequestBody OperatingSystem oSystem) {
        return new ResponseEntity<Boolean>(operatingSystemService.saveOperatingSystem(oSystem), HttpStatus.OK);
    }
}
