package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

public class DaisoCrawler {

    private static String url = "";
    private static String code = "";

    public List process(String url, String code) {
        List dataList = new ArrayList();

        WebDriver driver = new ChromeDriver(
                new ChromeOptions()
                        .addArguments("--disable-popup-blocking")               //팝업안띄움
                        .addArguments("headless")                               //브라우저 안띄움
                        .addArguments("--disable-gpu")                          //gpu 비활성화
                        .addArguments("--blink-settings=imagesEnabled=false")); //이미지 다운 안받음

        this.url = url;
        this.code = code;

        driver.get(url);    //브라우저에서 url로 이동한다.
        //Thread.sleep(2000); //브라우저 로딩될때까지 잠시 기다린다.

        try {
            System.out.println("다이소에서 정보를 찾아오는 중");
            dataList = getDataList(driver);
        } catch (Exception e) {
            System.out.println("뭔가잘못됨");
            e.printStackTrace();
            if(driver != null){
                driver.close();
                driver.quit();	//브라우저 닫기
            }
        }

        return dataList;
    }


    /**
     * data가져오기
     */
    private List<String> getDataList(WebDriver driver) throws InterruptedException {
        List<String> list = new ArrayList<>();

        // 순번
        list.add("");
        // 소분류 코드
        list.add(code);
        // 상품명
        list.add(driver.findElements(By.className("goods_info_content")).get(0)
                .findElement(By.tagName("strong"))
                .getText());
        // 상품번호
        String productNo = driver.findElements(By.className("goods_view_top")).get(0).getText();
        list.add(productNo.substring(productNo.length()-10));
        // 구매처
        list.add("다이소몰");
        // url
        list.add(url);
        // 가격
        list.add(driver.findElements(By.className("table_goods_info")).get(0)
                .findElement(By.tagName("strong"))
                .getText()
                .replace(",","")
                .replace("원",""));
        // 옵션
        list.add("");
        // 스토어명
        list.add(driver.findElements(By.className("minishop_seller_info")).get(0)
                .findElement(By.className("vm"))
                .getText());
        // 비고
        list.add("");

        if(driver != null){
            driver.close();
            driver.quit();	//브라우저 닫기
        }

        return list;
    }
}
