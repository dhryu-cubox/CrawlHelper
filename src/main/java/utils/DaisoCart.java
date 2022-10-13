package utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.out;

public class DaisoCart {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private WebDriver driver;

    public DaisoCart() {
        driver = new ChromeDriver(
                new ChromeOptions()
                        .addArguments("--disable-popup-blocking")               //팝업안띄움
                        .addArguments("headless")                               //브라우저 안띄움
                        .addArguments("--disable-gpu")                          //gpu 비활성화
                        .addArguments("--blink-settings=imagesEnabled=false")); //이미지 다운 안받음
    }

    public void insert() throws Exception {
        Login login = new Login();
        login.run(driver, "https://www.daisomall.co.kr/member/login.php?url=");

        InputStream inp = new FileInputStream("구매 예정 리스트.xlsx");
        workbook = new XSSFWorkbook(inp);
        sheet = workbook.getSheetAt(0);
        List<String> errors = new ArrayList<>();
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // 엑셀 파일을 읽는다.
        Iterator<Row> rowIterator = sheet.rowIterator();

        // row 수 만큼
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            int rowNum = row.getRowNum(); // 열 번호

            if (rowNum == 0) {
                continue; // 첫번째 열 skip
            }

            String url = row.getCell(5).getStringCellValue(); // url
            String option = row.getCell(7).getStringCellValue(); // 옵션

            if (row.getCell(9).getStringCellValue().equals("중복")) {
                out.println(rowNum + "번 중복 상품입니다. 장바구니에 등록되지 않습니다.");
                continue;
            }


            driver.get(url);    //브라우저에서 url로 이동한다.
            // 장바구니 버튼이 로딩될때까지 기다린다
            Thread.sleep(1000);

            // 옵션이 있는 경우 드롭다운 선택
            if (!option.equals("") && !option.isEmpty()) {
                Select select = new Select(driver.findElement(By.xpath("//*[@id='_goods_options']")));
                select.selectByVisibleText(option);
            }
            try {
                // 장바구니 담기
                jsExecutor.executeScript("goCart(document.pinfo, true,'')");

                // 예외처리
                if (driver.switchTo().alert().getText().contains("수량이 부족하여")) {
                    out.println(rowNum + "번 실패 (수량이 부족합니다)");
                    errors.add(url);
                    driver.switchTo().alert().dismiss();
                    continue;
                }
                if (driver.switchTo().alert().getText().contains("선택해주세요")) {
                    out.println(rowNum + "번 실패 (옵션이 정확히 입력되었는지 확인하세요)");
                    errors.add(url);
                    driver.switchTo().alert().dismiss();
                    continue;
                }

                driver.switchTo().alert().dismiss(); // 장바구니 보기 취소
                out.println(String.format(rowNum + "번 상품 장바구니 담기 완료"));
                row.createCell(10).setCellValue("담기완료");

            } catch (Exception e) {
                // 그외다른 에러는 찾아서 추가할 예정...
                out.println("장바구니 담기 중 발생한 에러");
                e.printStackTrace();
            }
        }
        // 엑셀 저장
        FileOutputStream fileOut = new FileOutputStream("구매 예정 리스트.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        out.println("장바구니 등록 완료\n등록실패한 상품은 아래와 같습니다.");
        out.println(errors);

        driver.close();	//탭 닫기
        driver.quit();	//브라우저 닫기

    }



}
