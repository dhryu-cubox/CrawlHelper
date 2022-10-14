package controller;

import utils.DaisoCart;
import utils.DaisoCrawler;
import utils.ExcelHelper;
import utils.ExcelValidationCheck;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class DaisoController {
    public void excelCheck() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("영수증 번호를 입력하세요");
        String oid = br.readLine();
        ExcelValidationCheck excelValidationCheck = new ExcelValidationCheck();
        excelValidationCheck.findList(oid);
    }

    public void cartInsert() throws Exception {
        DaisoCart cart = new DaisoCart();
        System.out.println("장바구니 입력을 시작합니다");
        cart.insert();
    }

    public void excelInsert() throws Exception {
        DaisoCrawler daisoCrawler = new DaisoCrawler();
        ExcelHelper excelHelper = new ExcelHelper();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.println("소분류 코드를 입력하세요");
            String code = br.readLine();
            if(code.trim().equalsIgnoreCase("exit")) return;

            System.out.println("url을 입력하세요:");
            String url = br.readLine();
            if(url.trim().equalsIgnoreCase("exit")) return;

            try {
                List<String> data = daisoCrawler.process(url, code);
                if (data.isEmpty() || data.get(0).equals("error")) continue;
                excelHelper.enterData(data);
            } catch (Exception e) {
                System.out.println("상품을 찾을 수 없습니다. 엑셀에 입력되지 않습니다...");
            }
        }
    }
}
