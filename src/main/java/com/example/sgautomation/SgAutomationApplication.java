package com.example.sgautomation;

import com.microsoft.playwright.*;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

import static java.lang.Thread.sleep;

@SpringBootApplication
public class SgAutomationApplication {

  public static void main(String[] args) {
    SpringApplication.run(SgAutomationApplication.class, args);
  }
}
