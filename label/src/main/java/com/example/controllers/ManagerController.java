package com.example.controllers;

import com.example.Manager;
import com.example.exceptions.ManagerNotFoundException;
import com.example.services.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    @Autowired
    private ManagerService managerService;
    @GetMapping
    public Collection<Manager> getManagers() {
        return managerService.getAll();
    }

    @GetMapping(value = "/{managerId}", produces = "application/json; charset=UTF-8")
    public Manager getManager(@PathVariable Integer managerId) {
        return managerService.getObjectById(managerId);
    }

    @PostMapping
    public Manager createManager(@Valid @RequestBody Manager newManager) {
        return managerService.saveObject(newManager);
    }

    @PutMapping(value = "/{managerId}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public Manager updateManager(@Valid @RequestBody Manager updatedManager, @PathVariable Integer managerId) {
        return managerService.updateObject(updatedManager, managerId);
    }

    @DeleteMapping("/{managerId}/delall")
    public ResponseEntity<?> deleteManager(@PathVariable Integer managerId) {
        try {
            managerService.deleteObject(managerId);
        } catch (ManagerNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception ex){
            System.out.println("ATTENTION " + ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }


}