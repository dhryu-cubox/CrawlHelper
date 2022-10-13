import utils.DaisoCart;
import utils.DaisoCrawler;
import utils.ExcelHelper;
import utils.ExcelValidationCheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CrawlHelper {

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            System.out.println("" +
                    "1: 엑셀입력\n" +
                    "2: 장바구니담기\n" +
                    "3: 구매내역확인\n" +
                    "exit을 입력하면 종료합니다."
            );
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
        String oid = br.readLine();
        ExcelValidationCheck excelValidationCheck = new ExcelValidationCheck();
        excelValidationCheck.findList(oid);
    }

    private static void cartInsert() throws Exception {
        DaisoCart cart = new DaisoCart();
        System.out.println("장바구니 입력을 시작합니다");
        cart.insert();
    }

    private static void excelInsert() throws Exception {
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
