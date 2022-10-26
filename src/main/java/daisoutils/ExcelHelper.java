package daisoutils;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    private XSSFSheet sheet;
    private XSSFWorkbook workbook;
    public ExcelHelper() throws IOException {
        try {
            //InputStream inp = new FileInputStream("C:\\Works\\CrawlHelper\\구매 예정 리스트.xlsx"); // 로컬
            InputStream inp = new FileInputStream("C:\\CrawlHelper\\구매 예정 리스트.xlsx"); //배포용
            workbook = new XSSFWorkbook(inp);
            sheet = workbook.getSheetAt(0);
        } catch (Exception e){
            e.printStackTrace();
            if(workbook != null) {
                //FileOutputStream fileOut = new FileOutputStream("C:\\Works\\CrawlHelper\\구매 예정 리스트.xlsx"); // 로컬
                FileOutputStream fileOut = new FileOutputStream("C:\\CrawlHelper\\구매 예정 리스트.xlsx"); // 배포용
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
        // 반복수를 줄이고 자율성을 높이기 위해 엑셀함수로 중복여부를 확인하게 함
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

        //FileOutputStream fileOut = new FileOutputStream("C:\\Works\\CrawlHelper\\구매 예정 리스트.xlsx"); // 로컬
        FileOutputStream fileOut = new FileOutputStream("C:\\CrawlHelper\\구매 예정 리스트.xlsx"); // 배포용
        workbook.write(fileOut);
        fileOut.close();
    }

    /**
     * 이미 입력된 url이 있는지 확인하기 위해 url 리스트를가져온다
     */
    public List<String> getUrlList() throws Exception {
        List<String> urlList = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.rowIterator();

        while(rowIterator.hasNext()){
            Row row = rowIterator.next();
            urlList.add(row.getCell(5).getStringCellValue()); // 엑셀파일에 url을 리스트에 입력
        }

        if(workbook != null) {
            //FileOutputStream fileOut = new FileOutputStream("C:\\Works\\CrawlHelper\\구매 예정 리스트.xlsx"); // 로컬
            FileOutputStream fileOut = new FileOutputStream("C:\\CrawlHelper\\구매 예정 리스트.xlsx"); // 배포용
            workbook.write(fileOut);
            fileOut.close();
        }
        return urlList;

    }

}