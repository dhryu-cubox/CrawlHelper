import controller.CoupangController;
import controller.DaisoController;
import controller.GmarketController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CrawlHelper {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) throws Exception {

        while(true) {
            System.out.println("---CrawlHelper---\n" +
                    "1: 다이소\n" +
                    "2: 쿠팡\n" +
                    "3: 지마켓\n" +
                    "x를 입력하면 종료합니다."
            );
            String choice  = br.readLine();
            switch (choice) {
                case "1":
                    daisoRun();
                    break;
                case "2":
                    coupangRun();
                    break;
                case "3" :
                    gmarketRun();
                    break;
                case "x": // switch 문을 나간다.
                default:     break;
            }
            if(choice.equalsIgnoreCase("x")) break; // 프로그램을 끝낸다.
        }
        System.out.println("---good bye---");
    }

    private static void gmarketRun() throws Exception {
        GmarketController gmarketController = new GmarketController();

        while(true) {
            System.out.println("---지마켓---\n" +
                    "1: 엑셀 입력\n" +
                    "2: 구매내역 확인\n" +
                    "x를 입력하면 돌아갑니다."
            );
            String choice  = br.readLine();
            switch (choice) {
                case "1":
                    gmarketController.excelInsert();
                    break;
                case "2":
                    gmarketController.excelCheck();
                    break;
                case "x":
                default:     break;
            }
            if(choice.equalsIgnoreCase("x")) break;
        }
    }


    private static void coupangRun() throws Exception {
        br = new BufferedReader(new InputStreamReader(System.in));
        CoupangController coupangController = new CoupangController();

        while(true) {
            System.out.println("---쿠팡---\n" +
                    "1: 엑셀 입력\n" +
                    "2: 옵션 가져오기\n" +
                    "3: 장바구니 담기\n" +
                    "4: 구매내역 확인\n" +
                    "x를 입력하면 돌아갑니다."
            );
            String choice  = br.readLine();
            switch (choice) {
                case "1":
                    coupangController.excelInsert();
                    break;
                case "2":
                    coupangController.getOptions();
                    break;
                case "3" :
                    coupangController.cartInsert();
                    break;
                case "4" :
                    coupangController.excelCheck();
                    break;
                case "x":
                default:     break;
            }
            if(choice.equalsIgnoreCase("x")) break;
        }
    }

    public static void daisoRun() throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DaisoController daisoController = new DaisoController();

        while(true) {
            System.out.println("---다이소몰---\n" +
                    "1: 엑셀 입력\n" +
                    "2: 옵션 가져오기\n" +
                    "3: 장바구니 담기\n" +
                    "4: 구매내역 확인\n" +
                    "x를 입력하면 돌아갑니다."
            );
            String choice  = br.readLine();
            switch (choice) {
                case "1":
                    daisoController.excelInsert();
                    break;
                case "2":
                    daisoController.getOptions();
                    break;
                case "3" :
                    daisoController.cartInsert();
                    break;
                case "4" :
                    daisoController.excelCheck();
                    break;
                case "x":
                default:     break;
            }
            if(choice.equalsIgnoreCase("x")) break;
        }

    }

}
