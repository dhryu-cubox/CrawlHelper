package utils;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ExcelHelper {
    private XSSFSheet sheet;
    private XSSFWorkbook workbook;
    public ExcelHelper() throws IOException {
        try {
            InputStream inp = new FileInputStream("구매 예정 리스트.xlsx");
            workbook = new XSSFWorkbook(inp);
            sheet = workbook.getSheetAt(0);
        } catch (Exception e){
            e.printStackTrace();
            if(workbook != null) {
                FileOutputStream fileOut = new FileOutputStream("구매 예정 리스트.xlsx");
                workbook.write(fileOut);
                fileOut.close();
            }
        }
    }

    public void enterData(List scrapedData) throws Exception {
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator(); // 엑셀 함수를 읽어주는 놈

        // 다음 행 만들기
        int rows = sheet.getPhysicalNumberOfRows();
        XSSFRow row = sheet.createRow(rows);

        // 순번 세팅
        String rowStr = String.valueOf(rows);
        scrapedData.set(0, rowStr);

        // 엑셀에 데이터 입력하기
        for(int cellNum = 0; cellNum <= 9; cellNum++){
            XSSFCell cell = row.createCell(cellNum);
            cell.setCellValue((String) scrapedData.get(cellNum));
            if(cellNum == 9) {
                cell.setCellFormula(String.format(
                        "IF((COUNTIF('구매 물품 리스트(다이소몰)'!$C:$C,C"+(rows+1) +"))>0,\"중복\",\"\")"
                ));
                evaluator.evaluateFormulaCell(cell);
                scrapedData.set(9,cell.getStringCellValue());
            }
        }

        System.out.println(sheet.getPhysicalNumberOfRows()-1+"번째 줄 생성 완료");
        System.out.println(scrapedData);

        FileOutputStream fileOut = new FileOutputStream("구매 예정 리스트.xlsx");
        workbook.write(fileOut);
        fileOut.close();
    }

}