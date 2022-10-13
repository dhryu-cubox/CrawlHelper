package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import static java.lang.System.out;

public class Login {

    public void run(WebDriver driver, String urlInput) {
        try {
            String id = "sstlabs";
            String pw = "Sstlabs1";
            driver.get(urlInput);
            driver.findElement(By.name("id")).sendKeys(id);
            driver.findElement(By.name("pw")).sendKeys(pw);
            driver.findElement(By.name("pw")).sendKeys(Keys.ENTER);
            Thread.sleep(5000);

            out.println("로그인 완료");
        } catch (Exception e) {
            out.println("로그인 실패");
        }
    }
}
