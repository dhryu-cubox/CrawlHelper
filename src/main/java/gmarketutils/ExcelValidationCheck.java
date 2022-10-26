package gmarketutils;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.lang.System.out;

public class ExcelValidationCheck {

    private XSSFSheet sheet;
    private XSSFWorkbook workbook;

    public ExcelValidationCheck() throws IOException {
        try {
            InputStream inp = new FileInputStream("C:\\Works\\CrawlHelper\\장바구니 리스트.xlsx"); // 로컬
            //InputStream inp = new FileInputStream("C:\\CrawlHelper\\장바구니 리스트.xlsx"); // 배포용
            workbook = new XSSFWorkbook(inp);
        } catch (Exception e){
            e.printStackTrace();
            if(workbook !=null) {
                FileOutputStream fileOut = new FileOutputStream("C:\\Works\\CrawlHelper\\장바구니 리스트.xlsx"); //로컬
                //FileOutputStream fileOut = new FileOutputStream("C:\\CrawlHelper\\장바구니 리스트.xlsx"); // 배포용
                workbook.write(fileOut);
                fileOut.close();
            }
        }
    }

    /**
     *
     */
    public void findList(String oid) throws Exception {
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator(); // 엑셀 함수를 읽어주는 놈
        sheet = workbook.getSheetAt(1);

        WebDriver driver = new ChromeDriver(
                new ChromeOptions()
                        .addArguments("--disable-popup-blocking")               //팝업안띄움
                        .addArguments("headless")                               //브라우저 안띄움
                        .addArguments("--disable-gpu")                          //gpu 비활성화
                        .addArguments("--blink-settings=imagesEnabled=false")); //이미지 다운 안받음
        // 로그인
        Login login = new Login();
        String url = "https://receipt.gmarket.co.kr/Purchase/PurchaseReceipt?pack_no="+oid+"&acnt_way=A2";
        login.run(driver, url);

        try {
            // 상품명 리스트 추출
            String bunchOfText = driver.findElement(By.className("sales_slip")).getText();
            int start = bunchOfText.indexOf("DESCRIPTION")+14;
            int end =  bunchOfText.indexOf("합계");
            String[] list = bunchOfText.substring(start, end).split("\n");

            for (String elem : list) {
                // 행 만들기
                int rowNum = sheet.getPhysicalNumberOfRows();
                XSSFRow row = sheet.createRow(rowNum);

                // 셀 입력
                XSSFCell cell = row.createCell(0);
                cell.setCellValue(elem.trim());
                cell = row.createCell(1);
                cell.setCellFormula("COUNTIF('입고검사'!C:C,A" + (rowNum + 1) + ")"); // 동일한 소분류번호의 갯수를 확인하는 함수
                evaluator.evaluateFormulaCell(cell);

                out.println(elem.trim());
            }
            out.println("입력완료");

        } catch (Exception e){
            out.println("엑셀 입력 실패");

        } finally {
            driver.close();
            driver.quit();
            if (workbook != null) {
                FileOutputStream fileOut = new FileOutputStream("C:\\Works\\CrawlHelper\\장바구니 리스트.xlsx"); // 로컬
                //FileOutputStream fileOut = new FileOutputStream("C:\\CrawlHelper\\장바구니 리스트.xlsx"); // 배포용
                workbook.write(fileOut);
                fileOut.close();
            }
        }

    }

}
