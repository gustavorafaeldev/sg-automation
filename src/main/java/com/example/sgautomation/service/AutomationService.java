package com.example.sgautomation.service;

import com.microsoft.playwright.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

@Service
@Slf4j
public class AutomationService {

  @SneakyThrows
  public void savePrice(Map json) {
    String url = json.get("url").toString();
    String idProduct = url.split("/")[4];
    String urlProduct = "https://apprn.cloudsg.com.br/#!/productv2/edit/" + idProduct;


    try (Playwright playwright = Playwright.create()) {

      Browser browser = playwright.chromium().launch(
              new BrowserType.LaunchOptions()
                      .setHeadless(true)
                      .setSlowMo(1000));

      Page page = browser.newPage();
      page.navigate("https://apprn.cloudsg.com.br/#!/login");
      log.info("navigate login");
      sleep(10000);
      ElementHandle email = page.querySelectorAll("input").get(5);
      ElementHandle senha = page.querySelectorAll("input").get(6);
      ElementHandle entrar = page.querySelectorAll("button").get(3);

      email.fill("vipagi@hotmail.com");
      senha.fill("130843");
      entrar.click();


      sleep(10000);
      log.info("navigate product");
      page.navigate(urlProduct);
      sleep(15000);

      ElementHandle divPrice = page.querySelectorAll(".mbg-input-money-wrapper").get(5);
      ElementHandle input = divPrice.querySelector("input");
      String value2 = input.inputValue();

      ElementHandle proximo = page.querySelectorAll("button").get(24);
      proximo.dispatchEvent("click");
      log.info("proximo");
      sleep(1000);


      // acessar a tabela
      ElementHandle divProduct = page.querySelector(".product-grid-wrapper");
      ElementHandle table = divProduct.querySelector("table");
      ElementHandle tbody = table.querySelector("tbody");
      List<ElementHandle> tr = tbody.querySelectorAll("tr");

      int indexTd = 0;
      int indexTr = 0;

      for (int i = 2; i < tr.size(); i++) {
        List<ElementHandle> td = tr.get(i).querySelectorAll("td");

        for (int j = 1; j < td.size(); j++) {
          ElementHandle svg = td.get(j).querySelector("svg");
          svg.dispatchEvent("click");

          String pagination = page.querySelector(".pagination-modal").innerText();
          int totalProducts = Integer.parseInt(pagination.split(" ")[0]);

          if (totalProducts == 1) {
            indexTr = i;
            indexTd = j;
            closeModalProduct(page);
            break;
          }
          closeModalProduct(page);
        }
      }

      ElementHandle td = tr.get(indexTr).querySelectorAll("td").get(indexTd);
      ElementHandle svg = td.querySelector("svg");

      svg.dispatchEvent("click");
      sleep(500);

      clickButtonPreco(page);
      setPreco(value2, page);
      closeModalPrice(page);


      String pagination = page.querySelector(".pagination-modal").innerText();
      int totalProducts = Integer.parseInt(pagination.split(" ")[2]);


      for (int i = 1; i < totalProducts; i++) {
        goToNext(page);
        clickButtonPreco(page);
        setPreco(value2, page);
        closeModalPrice(page);
      }

      ElementHandle buttonFinish = page.querySelector(".cancel");
      buttonFinish.dispatchEvent("click");
      sleep(500);


      while (true) {
        ElementHandle buttonSave = page.querySelector(".mbg-btn-form-wrapper");
        String value = buttonSave.innerText();
        buttonSave.dispatchEvent("click");
        sleep(500);
        if (value.equals("SALVAR")) {
          break;
        }
      }
      sleep(2000);
    }
  }

  private static void closeModalProduct(Page page) throws InterruptedException {
    ElementHandle buttonClose = page.querySelector(".mbg-close-modal");
    buttonClose.dispatchEvent("click");
    sleep(500);
  }

  private static void goToNext(Page page) throws InterruptedException {
    ElementHandle buttonProximo = page.querySelectorAll("button").get(2);
    buttonProximo.dispatchEvent("click");
    sleep(500);
  }

  private static void closeModalPrice(Page page) {
    ElementHandle closeModal = page.querySelectorAll(".close-modal-button").get(0);
    closeModal.dispatchEvent("click");
  }

  private static void setPreco(String value2, Page page) throws InterruptedException {
    ElementHandle inputPreco = page.querySelectorAll("input").get(16);
    inputPreco.fill(value2, new ElementHandle.FillOptions().setForce(true));
    sleep(500);
  }

  private static void clickButtonPreco(Page page) throws InterruptedException {
    ElementHandle preco = page.querySelectorAll(".fas").get(4);
    preco.dispatchEvent("click");
    sleep(500);
  }
}

