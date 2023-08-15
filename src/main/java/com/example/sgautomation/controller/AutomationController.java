package com.example.sgautomation.controller;

import com.example.sgautomation.service.AutomationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
@CrossOrigin("*")
@RequiredArgsConstructor
public class AutomationController {

  private final AutomationService service;

  @PostMapping("automation")
  ResponseEntity<?> automation(@RequestBody Map<String, Object> body) {
    service.savePrice(body);
    return ResponseEntity.ok().build();
  }
}
