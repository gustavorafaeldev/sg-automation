package com.example.sgautomation.controller;


import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping
public class SaudeController {

  @GetMapping("saude/health")
  String saude() {
    return "OK";
  }
}
