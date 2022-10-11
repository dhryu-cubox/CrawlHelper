import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;

public class ValidationCheckTest {

    @Test
    void find() throws InterruptedException {
        WebDriver driver = new ChromeDriver(
                new ChromeOptions()
                        .addArguments("--disable-popup-blocking")               //팝업안띄움
                        .addArguments("headless")                               //브라우저 안띄움
                        .addArguments("--disable-gpu")                          //gpu 비활성화
                        .addArguments("--blink-settings=imagesEnabled=false")); //이미지 다운 안받음

        // 로그인
        String url = "https://www.daisomall.co.kr/mypage/order_detail.php?oid=20221007135428-39729";
        String id = "sstlabs";
        String pw = "Sstlabs1";
        driver.get(url);
        driver.findElement(By.name("id")).sendKeys(id);
        driver.findElement(By.name("pw")).sendKeys(pw);
        driver.findElement(By.name("pw")).sendKeys(Keys.ENTER);
        out.println("로그인 완료");
        Thread.sleep(5000);

        List<WebElement> list = driver.findElements(By.name("deliverymsg")).get(0).findElements(By.tagName("span")); // 상품명 리스트 추출

        for(WebElement a : list){
            out.println(a.getText());
        }


    }


}
