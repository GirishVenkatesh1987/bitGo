package examples;

import base.Hooks;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class SampleTest extends Hooks {

    private WebDriver driver;
    @BeforeTest
    public void beforeTest() throws IOException{
        driver = getDriver();
        driver.get("https://blockstream.info/block/000000000000000000076c036ff5119e5a5a74df77abf64203473364509f7732");
    }

    @Test
    public void firstTest() throws IOException {

        WebElement element = driver.findElement(By.cssSelector("h3"));
        System.out.println(element.getText().trim());

    }

    @Test
    public void secondTest() throws IOException {
        System.out.println("Transactions which are having one input and two outputs are :");
        for(int i=1;i<=25;i++) {
            try {
                String headerPath = "//div[@id='transaction-box'][" +i+ "]/descendant::div[@class='header']/div/a";
                WebElement transactionElement = driver.findElement(By.xpath(headerPath));

                String insPath = "//div[@id='transaction-box'][" +i+ "]/descendant::div[@class='vins']//descendant::span/a";
                List<WebElement> insElement = driver.findElements(By.xpath(insPath));
                if(insElement.size() == 1) {
                    String outsPath = "//div[@id='transaction-box'][" +i+ "]/descendant::div[@class='vouts']//descendant::span/a";
                    List<WebElement> outsElement = driver.findElements(By.xpath(outsPath));
                    if(outsElement.size() == 2) {
                        System.out.println(transactionElement.getText().trim());
                    }


                }
            } catch(Exception ex) {
                //ignore if href not found
            }



        }

    }
}
