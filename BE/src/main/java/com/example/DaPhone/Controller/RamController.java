package com.example.DaPhone.Controller;

import com.example.DaPhone.Entity.Ram;
import com.example.DaPhone.Service.RamService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/ram")
public class RamController {
    @Autowired
    private RamService ramService;

    @GetMapping(value = "")
    public ResponseEntity<Page<Ram>> getPageImei(Pageable pageable) {
        Page<Ram> pageRam = ramService.getPageRam(pageable);
        return new ResponseEntity<Page<Ram>>(pageRam, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Ram> getRamById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<Ram>(ramService.findById(id), HttpStatus.OK);
    }

    @PostMapping(value="")
    public ResponseEntity<Boolean> saveRam(@RequestBody Ram ram) {
        return new ResponseEntity<Boolean>(ramService.saveRam(ram), HttpStatus.OK);
    }
    
    @GetMapping(value = "/list")
    public ResponseEntity<List<Ram>> getListImei() {
        List<Ram> pageRam = ramService.getListRam();
        return new ResponseEntity<List<Ram>>(pageRam, HttpStatus.OK);
    }
}
