import controller.DaisoController;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CrawlHelper {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) throws Exception {

        while(true) {
            System.out.println("---CrawlHelper---\n" +
                    "1: 다이소\n" +
                    "2: 쿠팡\n" +
                    "3: 지마켓\n" +
                    "exit을 입력하면 종료합니다."
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
                case "exit": // switch 문을 나간다.
                default:     break;
            }
            if(choice.equalsIgnoreCase("exit")) break; // 프로그램을 끝낸다.
        }
        System.out.println("---good bye---");
    }

    private static void gmarketRun() {
        System.out.println("아직 만드는중");
    }

    private static void coupangRun() {
        System.out.println("아직 만드는중");
    }

    public static void daisoRun() throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DaisoController daisoController = new DaisoController();

        while(true) {
            System.out.println("---다이소몰을 선택하셨습니다.---\n" +
                    "1: 엑셀입력\n" +
                    "2: 장바구니담기\n" +
                    "3: 구매내역확인\n" +
                    "exit을 입력하면 돌아갑니다."
            );
            String choice  = br.readLine();
            switch (choice) {
                case "1":
                    daisoController.excelInsert();
                    break;
                case "2":
                    daisoController.cartInsert();
                    break;
                case "3" :
                    daisoController.excelCheck();
                case "exit":
                default:     break;
            }
            if(choice.equalsIgnoreCase("exit")) break;
        }

    }

}
