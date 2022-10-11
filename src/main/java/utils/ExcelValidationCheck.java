package utils;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.awt.*;
import java.io.*;
import java.util.List;

import static java.lang.System.out;

public class ExcelValidationCheck {

    private XSSFSheet sheet;
    private XSSFWorkbook workbook;

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
        String id = "sstlabs";
        String pw = "Sstlabs1";
        driver.get(url);
        driver.findElement(By.name("id")).sendKeys(id);
        driver.findElement(By.name("pw")).sendKeys(pw);
        driver.findElement(By.name("pw")).sendKeys(Keys.ENTER);
        out.println("로그인 완료");
        Thread.sleep(5000);

        // 상품명 리스트 추출
        List<WebElement> productNameList = driver.findElements(By.name("deliverymsg")).get(0)
                .findElements(By.tagName("span"));

        for(WebElement elem : productNameList){
            /**
             *  데이터를 가져온 다음 새 시트에 전부 입력한다.
             *  가져온 값과 일치여부를 확인하는 함수를 입력한다.
             *
             */
            // 행 만들기
            int rowNum = sheet.getPhysicalNumberOfRows();
            XSSFRow row = sheet.createRow(rowNum);

            // 셀 입력
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(elem.getText());
            cell = row.createCell(1);
            cell.setCellFormula("COUNTIF('제출용'!C:C,A"+(rowNum+1) +")");
            evaluator.evaluateFormulaCell(cell);
            /*if(cell.getNumericCellValue() == 2.0 || cell.getNumericCellValue() == 0.0) {
                cell.setCellValue("");
            } else {
                cell.setCellValue("OK");

            }*/

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

    public void changeCellBackgroundColor(XSSFCell cell) {
        CellStyle cellStyle = cell.getCellStyle();
        if(cellStyle == null) {
            cellStyle = cell.getSheet().getWorkbook().createCellStyle();
        }
        cellStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(cellStyle);
    }

}
