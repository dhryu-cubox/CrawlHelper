package CrawlHelper;

import utils.DaisoCart;
import utils.DaisoCrawler;
import utils.ExcelHelper;
import utils.ExcelValidationCheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CrawlHelper {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.println("1. 엑셀입력\n2. 장바구니담기\n3. 구매내역확인\nexit을 입력하면 종료합니다.");
            String choice  = br.readLine();
            switch (choice) {
                case "1":
                    excelInsert();
                    break;
                case "2":
                    cartInsert();
                    break;
                case "3" :
                    excelCheck();
                case "exit":
                default:     break;
            }
            if(choice.equalsIgnoreCase("exit")) break;
        }
    }

    private static void excelCheck() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("영수증 번호를 입력하세요");
        // TODO 영수증번호를 입력하세요
        String oid = "20221007135428-39729"; // br.readLine();
        ExcelValidationCheck e = new ExcelValidationCheck();
        e.findList(oid);
    }

    private static void cartInsert() throws Exception {
        DaisoCart cart = new DaisoCart();
        System.out.println("장바구니 입력을 시작합니다");
        cart.insert();
    }

    private static void excelInsert() throws IOException {
        DaisoCrawler daisoCrawler = new DaisoCrawler();
        ExcelHelper excelHelper = new ExcelHelper();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                System.out.println("소분류 코드를 입력하세요");
                String code = br.readLine();
                if(code.equalsIgnoreCase("exit")) return;

                System.out.println("url을 입력하세요:");
                String url = br.readLine();
                if(url.equalsIgnoreCase("exit")) return;

                excelHelper.enterData(daisoCrawler.process(url, code));

            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}