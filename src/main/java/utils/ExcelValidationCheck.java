package utils;

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
    private Login login;

    public ExcelValidationCheck() throws IOException {
        try {
            InputStream inp = new FileInputStream("장바구니 리스트.xlsx");
            workbook = new XSSFWorkbook(inp);
        } catch (Exception e){
            e.printStackTrace();
            if(workbook !=null) {
                FileOutputStream fileOut = new FileOutputStream("장바구니 리스트.xlsx");
                workbook.write(fileOut);
                fileOut.close();
            }
        }
    }

    /**
     * 결제영수증의 oid를 입력하면 구매한 상품을 가져와서 장바구니 리스트.xlsx에 올려준다.
     * 가져온 값과 일치여부를 확인하는 함수를 입력해준다.
     * 1이면 정상,
     * 2라면 중복으로 구매된 것,
     * 0이라면 구매되지 않은것이다.
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
        String url = "https://www.daisomall.co.kr/mypage/order_detail.php?oid="+ oid;
        login.run(driver, url);

        // 상품명 리스트 추출
        List<WebElement> productNameList = driver.findElements(By.name("deliverymsg")).get(0)
                .findElements(By.tagName("span"));

        for(WebElement elem : productNameList){
            // 행 만들기
            int rowNum = sheet.getPhysicalNumberOfRows();
            XSSFRow row = sheet.createRow(rowNum);

            // 셀 입력
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(elem.getText());
            cell = row.createCell(1);
            cell.setCellFormula("COUNTIF('입고검사'!C:C,A"+(rowNum+1) +")"); // 동일한 소분류번호의 갯수를 확인하는 함수
            evaluator.evaluateFormulaCell(cell);
        }
        out.println("입력완료");

        if(workbook != null) {
            FileOutputStream fileOut = new FileOutputStream("장바구니 리스트.xlsx");
            workbook.write(fileOut);
            fileOut.close();
        }
        driver.close();	//탭 닫기
        driver.quit();	//브라우저 닫기

    }

}
