package gmarketutils;

import daisoutils.ExcelHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

public class GmarketCrawler {

    private static String url = "";
    private static String code = "";
    WebDriver driver;

    /**
     * url과 code(소분류코드)를 입력하면 값을 가져오는 메서드를 실행한다.
     */
    public List<String> process(String url, String code) throws Exception {
        List<String> dataList = new ArrayList();

        // driver를 매번 초기화 해주어야 여러번 process를 진행할 수 있다.
        driver = new ChromeDriver(
                new ChromeOptions()
                        .addArguments("--disable-popup-blocking")               //팝업안띄움
                        .addArguments("headless")                               //브라우저 안띄움
                        .addArguments("--disable-gpu")                          //gpu 비활성화
                        .addArguments("--blink-settings=imagesEnabled=false")); //이미지 다운 안받음

        this.url = url;
        this.code = code;

        // 만약 엑셀에 이미 url이 입력되어 있다면 중복으로 처리하고 리스트를 입력하지 않는다
        // TODO 옵션이 있으면 주소가 같을수밖에 없는데 이부분은 어떻게 처리할것인가? - 아직 옵션이 있는 상품은 크롤링하지않도록 하고있음
        ExcelHelper excelHelper = new ExcelHelper();
        if(excelHelper.getUrlList().contains(url)){
            System.out.println("이미 입력된 상품입니다. 엑셀에 입력되지 않습니다...");
            if(driver != null){
                driver.close();
                driver.quit();	//브라우저 닫기
            }
            return dataList;
        }

        driver.get(url);    //브라우저에서 url로 이동한다.
        //Thread.sleep(2000); //브라우저 로딩될때까지 잠시 기다린다.

        System.out.println("지마켓에서 정보를 찾아오는 중");
        try {
            dataList = getDataList(driver);
        } catch (Exception e) {
            if(driver != null){
                driver.close();
                driver.quit();	//브라우저 닫기
            }
        }

        return dataList;
    }


    /**
     * WebDrvier로 지마켓의 데이터를 긁어와서 리스트로 반환한다.
     */
    private List<String> getDataList(WebDriver driver) {
        List<String> list = new ArrayList<>();

        // 순번
        list.add("");
        // 소분류 코드
        list.add(code);
        // 상품명
        list.add(driver.findElements(By.className("itemtit")).get(0)
                .getText());
        // 상품번호
        String productNo = driver.findElements(By.className("text__item-number")).get(0).getText();
        list.add(productNo.substring(productNo.length()-10));
        // 구매처
        list.add("지마켓");
        // url
        list.add(url);
        // 가격
        list.add(driver.findElements(By.className("price_innerwrap")).get(0)
                        .findElement(By.tagName("strong"))
                .getText()
                .replace(",","")
                .replace("원",""));
        // 옵션
        list.add("");
        // 스토어명
        list.add(driver.findElements(By.className("text__seller")).get(0)
                .findElement(By.tagName("a"))
                .getText());
        // 비고
        list.add("");

        return list;
    }

    /**
     *
     * 옵션 목록을 가져오는 메서드
     *
     */
    public void getOptions(String url) {
        if(!url.contains("www.")) return;

        driver = new ChromeDriver(
                new ChromeOptions()
                        .addArguments("--disable-popup-blocking")               //팝업안띄움
                        .addArguments("headless")                               //브라우저 안띄움
                        .addArguments("--disable-gpu")                          //gpu 비활성화
                        .addArguments("--blink-settings=imagesEnabled=false")); //이미지 다운 안받음

        try {
            driver.get(url);
            List<WebElement> list = driver.findElements(By.xpath("//*[@id='_goods_options']"));

            if(list.isEmpty()){
                System.out.println("옵션이 없습니다.");
                return;
            }
            System.out.println(list.get(0).getText());

        } catch (Exception e){
            e.printStackTrace();
            if(driver != null) {
                driver.close();
                driver.quit();    //브라우저 닫기
            }
        }

        if(driver != null){
            driver.close();
            driver.quit();	//브라우저 닫기
        }

    }
}
