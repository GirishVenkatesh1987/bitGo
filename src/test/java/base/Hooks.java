package base;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class Hooks {

    protected ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
    protected String browserType;
    protected String isGrid;

    @BeforeTest
    public void beforeTestHooks(ITestContext testContext) throws IOException {


        getDriver();
    }


    public WebDriver getDriver() throws IOException {
        if(threadLocalDriver.get() == null) {
            threadLocalDriver.set(createDriver());
        }
        return threadLocalDriver.get();
    }

    private WebDriver createDriver() throws IOException {
        WebDriver driver = null;
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/config.properties");
        properties.load(fileInputStream);
        browserType = properties.getProperty("browser").toLowerCase().trim();
        isGrid = properties.getProperty("isGrid").toLowerCase().trim();
        switch (isGrid) {
            case "no":
                switch (browserType) {
                    //driver set not required for selenium 4
                    case "chrome":
                        //System.setProperty("webdriver.chorme.driver", System.getProperty("user.dir") + "/src/test/resources/drivers/chromedriver.exe");
                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                        driver = new ChromeDriver(chromeOptions);
                        break;
                    case "firefox":
                        //System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/src/test/resources/drivers/geckodriver.exe");
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                        driver = new FirefoxDriver(firefoxOptions);
                        break;
                    case "edge":
                        //System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") + "/src/test/resources/drivers/edgedriver.exe");
                        EdgeOptions edgeOptions = new EdgeOptions();
                        edgeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                        driver = new EdgeDriver(edgeOptions);
                        break;
                    default:
                        System.out.println("The browser type is not supported :" + browserType);
                        System.exit(0);


                }
                break;


        case "yes":
        switch (browserType) {
            //driver set not required for selenium 4
            case "chrome":
                System.setProperty("webdriver.chorme.driver", System.getProperty("user.dir") + "/src/test/resources/drivers/chromedriver.exe");
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                chromeOptions.addArguments("start-maximized");
                driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),chromeOptions);
                break;
            case "firefox":
                System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/src/test/resources/drivers/geckodriver.exe");
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),firefoxOptions);
                break;
            case "edge":
                //System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") + "/src/test/resources/drivers/edgedriver.exe");
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),edgeOptions);
                break;
            default:
                System.out.println("The browser type is not supported :" + browserType);
                System.exit(0);


            }
            break;
        }
        return driver;
    }

    @AfterTest
    public void afterTestHooks() throws IOException {
        WebDriver driver = getDriver();
        if(driver != null) {
            driver.quit();
            threadLocalDriver.remove();
        }


    }
}
