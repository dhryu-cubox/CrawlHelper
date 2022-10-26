package controller;

import daisoutils.ExcelHelper;
import gmarketutils.ExcelValidationCheck;
import gmarketutils.GmarketCrawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class GmarketController {
    public GmarketController() {
        System.setProperty("webdriver.chrome.driver","C:\\Works\\CrawlHelper\\chromedriver.exe"); // 로컬
        //System.setProperty("webdriver.chrome.driver","C:\\CrawlHelper\\chromedriver.exe"); // 배포용
    }

    public void excelCheck() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("주문 번호를 입력하세요");
        String oid = br.readLine();
        if(oid.trim().equalsIgnoreCase("x")) return;
        ExcelValidationCheck excelValidationCheck = new ExcelValidationCheck();
        excelValidationCheck.findList(oid);
    }

    public void cartInsert() throws Exception {
    }

    public void excelInsert() throws Exception {
        GmarketCrawler gmarketCrawler = new GmarketCrawler();
        ExcelHelper excelHelper = new ExcelHelper();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.println("소분류 코드를 입력하세요");
            String code = br.readLine();
            if(code.trim().equalsIgnoreCase("x")) return;

            System.out.println("url을 입력하세요:");
            String url = br.readLine();
            if(url.trim().equalsIgnoreCase("x")) return;

            try {
                List<String> data = gmarketCrawler.process(url, code);
                if (data.isEmpty()) continue;
                excelHelper.enterData(data);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("상품을 찾을 수 없습니다. 엑셀에 입력되지 않습니다...");
            }
        }
    }

    public void getOptions() throws Exception {


    }
}
