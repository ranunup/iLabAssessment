package data.engine;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import test.model.TestCaseResult;

public class SeleniumDriver {

    private WebDriver driver;
    private final ConfigProperties conf;

    public SeleniumDriver() {
        conf = new ConfigProperties();
        this.startDrivers();
    }

    private void startDrivers() {
        try {
        	
        	String osName = System.getProperty("os.name");
        	String chromeDriverLoc = null, firefoxDriverLoc = null;
        	
        	if (osName.contains("Windows")) {
        		chromeDriverLoc = System.getProperty("user.dir") + "/chromedriver.exe";
        		firefoxDriverLoc = System.getProperty("user.dir") + "/geckodriver.exe";
        	} else if (osName.contains("Mac")) {
        		chromeDriverLoc = System.getProperty("user.dir") + "/chromedriver";
        		firefoxDriverLoc = System.getProperty("user.dir") + "/geckodriver";
        	} else {
        		throw new Exception("[Selenium Warning] Framework only supports Windows and Mac web drivers.");
        	}

            switch (conf.getWebDriver()) {
                case "chrome":
                    this.launchChrome(chromeDriverLoc);
                    break;
                case "firefox":
                    this.launchFirefox(firefoxDriverLoc);
                    break;
                default:
                    throw new Exception("[Selenium Error] Failed to start web driver, unknown web driver  - " + conf.getWebDriver());
            }
        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to start web driver, error - " + ex.getMessage());
        }
    }

    private void launchChrome(String driverLocation) {

        try {
            
			System.setProperty("webdriver.chrome.driver", driverLocation);
			
			ChromeDriverService service = new ChromeDriverService.Builder()
	                .usingDriverExecutable(new File(System.getProperty("user.dir") + "/chromedriver"))
	                .usingAnyFreePort()
	                .build();
			
			service.start();
			
			driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());

        } catch (MalformedURLException ex) {
            System.err.println("[Selenium Error] Failed to launch chrome, error - " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to launch chrome, error - " + ex.getMessage());
        }
    }
    
    private void launchFirefox(String driverLocation) {

        try {

			System.setProperty("webdriver.gecko.driver", driverLocation);
			
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("network.proxy.type", 1);
			profile.setPreference("network.proxy.http", "localhost");
			profile.setPreference("network.proxy.http_port", 3128);
			
			FirefoxOptions options = new FirefoxOptions();
			options.setProfile(profile);
			
			
			driver = new FirefoxDriver(options);

        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to launch firefox, error - " + ex.getMessage());
        }
    }

    public void stopDriver() {
        try {
            if (driver != null) {
            	
            	System.out.println("[INFO] Closing browser.");
            	
                driver.quit();
            } else {
                System.err.println("[Selenium Warning] Could not stop web driver as it is not running.");
            }
        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to stop web driver, eror - " + ex.getMessage());
        }
    }

    public TestCaseResult goToURL(String URL) {
        boolean testPassed = false;
        String screenshotLocation = null;
        try {

            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            driver.manage().window().maximize();

            driver.get(URL);

            testPassed = true;
            screenshotLocation = this.takeScreenshot(this.conf.getScreenshotDir());

        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to go to URL : " + URL + " - error - " + ex.getMessage());
        }

        return new TestCaseResult(testPassed, screenshotLocation);
    }

    public TestCaseResult clickButtonByText(String buttonText) {
        boolean testPassed = false;
        String screenshotLocation = null;
        try {

            WebElement elem = this.clickableElemByText(buttonText);

            if (elem != null) {
                testPassed = clickElem(elem);
                screenshotLocation = this.takeScreenshot(this.conf.getScreenshotDir());
            }

        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to click button by text : " + buttonText + ", error - " + ex.getMessage());
        }

        return new TestCaseResult(testPassed, screenshotLocation);
    }

    public TestCaseResult insertTextByID(String inputStr) {

        boolean testPassed = false;
        String screenshotLocation = null;

        try {
            String[] keyVal = inputStr.split("/");

            String elemID = keyVal[0];
            String elemVal = keyVal[1];

            WebElement elem = this.elemByID(elemID);
            elem.sendKeys(elemVal);
            testPassed = true;
            screenshotLocation = this.takeScreenshot(this.conf.getScreenshotDir());

        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to insert : " + inputStr + " into element, error - " + ex.getMessage());
        }

        return new TestCaseResult(testPassed, screenshotLocation);
    }

    public TestCaseResult clickHrefAfterText(String locatorText) {
        boolean testPassed = false;
        String screenshotLocation = null;
        try {

            WebElement elem = this.hreAfterText(locatorText);

            if (elem != null) {
                testPassed = clickElem(elem);
            }

        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to click href position after text, error - " + ex.getMessage());
        }
        return new TestCaseResult(testPassed, screenshotLocation);
    }

    public TestCaseResult verifyVisitedScreen(String URL) {
        boolean testPassed = false;
        String screenshotLocation = null;

        try {
            String driverURL = this.driver.getCurrentUrl();

            testPassed = driverURL.equals(URL);

            screenshotLocation = this.takeScreenshot(this.conf.getScreenshotDir());

        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to verify visited screen, error - " + ex.getMessage());
        }

        return new TestCaseResult(testPassed, screenshotLocation);
    }

    public TestCaseResult selectLinkByText(String linkText) {
        boolean testPassed = false;
        String screenshotLocation = null;

        try {

            WebElement href = this.elemByLinkText(linkText);

            if (href != null) {
                testPassed = clickElem(href);
                screenshotLocation = this.takeScreenshot(this.conf.getScreenshotDir());
            }
        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to select link by text, error - " + ex.getMessage());
        }

        return new TestCaseResult(testPassed, screenshotLocation);
    }
    
    public TestCaseResult selectLinkByContainedText(String linkText) {
        boolean testPassed = false;
        String screenshotLocation = null;

        try {

            WebElement href = this.hrefByContainedText(linkText);

            if (href != null) {
                testPassed = clickElem(href);
                screenshotLocation = this.takeScreenshot(this.conf.getScreenshotDir());
            }
        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to select link by contained text, error - " + ex.getMessage());
        }

        return new TestCaseResult(testPassed, screenshotLocation);
    }

    public TestCaseResult clickByOptionText(String optionText) {
        boolean testPassed = false;
        String screenshotLocation = null;

        try {

            WebElement elem = this.elemByClickableOptionText(optionText);

            if (elem != null) {
                testPassed = clickElem(elem);
            }

        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to click by option text : " + optionText + ", error - " + ex.getMessage());
        }

        return new TestCaseResult(testPassed, screenshotLocation);
    }

    public TestCaseResult verifyTextOnPage(String pageText) {
        boolean testPassed = false;
        String screenshotLocation = null;

        try {
            WebElement elem = this.elemByPageText(pageText);

            this.scrollElementToView(elem);

            testPassed = elem != null;
            screenshotLocation = this.takeScreenshot(this.conf.getScreenshotDir());

        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to verify text : " + pageText + " on page, error - " + ex.getMessage());
        }

        return new TestCaseResult(testPassed, screenshotLocation);
    }

    public TestCaseResult insertCellNumByID(String elemID) {
        boolean testPassed = false;
        String screenshotLocation = null;

        try {

            WebElement elem = elemByID(elemID);

            if (elem != null) {

                elem.sendKeys(this.autoGenPhoneNum());
                testPassed = true;
                screenshotLocation = this.takeScreenshot(this.conf.getScreenshotDir());
            }

        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to insert cell number by ID : " + elemID + ", error - " + ex.getMessage());
        }

        return new TestCaseResult(testPassed, screenshotLocation);
    }

    public TestCaseResult clickByInputValue(String inputVal) {
        boolean testPassed = true;
        String screenshotLocation = null;

        try {
            WebElement elem = elemByInputValue(inputVal);

            if (elem != null) {
                testPassed = clickElem(elem);
                screenshotLocation = this.takeScreenshot(this.conf.getScreenshotDir());
            }
        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to click by input value: " + inputVal + ", erorr - " + ex.getMessage());
        }

        return new TestCaseResult(testPassed, screenshotLocation);
    }

    private String autoGenPhoneNum() {

        try {

            Random rand = new Random();
            int grp1 = rand.nextInt(99) + 1;
            int grp2 = rand.nextInt(1000);
            int grp3 = rand.nextInt(1000);

            DecimalFormat df1 = new DecimalFormat("000");
            DecimalFormat df2 = new DecimalFormat("0000");

            String str1 = String.format("%03d", grp1);

            return str1 + ' ' + df1.format(grp2) + ' ' + df2.format(grp3);

        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to auto generate phone number, error - " + ex.getMessage());
        }

        return null;
    }

    private void scrollElementToView(WebElement elem) {

        try {

            JavascriptExecutor jExec = (JavascriptExecutor) driver;

            jExec.executeScript("arguments[0].scrollIntoView(true); window.scrollBy(0, -window.innerHeight / 4);", elem);

        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to scroll into view, error - " + ex.getMessage());
        }
    }

    private String takeScreenshot(String filePath) {
        try {
            TakesScreenshot screenShot = ((TakesScreenshot) driver);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date now = new Date();

            String dateStr = dateFormat.format(now);

            File srcFile = screenShot.getScreenshotAs(OutputType.FILE);

            String fileName = dateStr + ".png";

            filePath = filePath + fileName;

            File destinationFile = new File(filePath);

            FileUtils.copyFile(srcFile, destinationFile);

            return fileName;

        } catch (IOException | WebDriverException ex) {
            System.err.println("[Selenium Error] Failed to take screenshot, error - " + ex.getMessage());
            ex.printStackTrace();
        }

        return null;
    }

    private WebElement elemByInputValue(String inputVal) {

        try {

            String elemLocator = "//input[@value='" + inputVal + "']";

            return this.elemByXpath(elemLocator);

        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to get element by input value : " + inputVal + ", error - " + ex.getMessage());
        }

        return null;
    }

    private WebElement elemByID(String elemId) {
        try {
            By id = By.id(elemId);
            return driver.findElement(id);
        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to get element by ID, error" + ex.getMessage());
        }
        return null;
    }

    private WebElement hreAfterText(String locatorText) {

        try {

            String elemLocator = "//*/text()[.='" + locatorText + "']/following::a";

            return this.elemByXpath(elemLocator);

        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to get element by href position after text, error - " + ex.getMessage());
        }

        return null;
    }

    private WebElement clickableElemByText(String elemText) {
        try {
            String elemLocator = "//*[contains(text(),'" + elemText + "')]";

            return this.elemByXpath(elemLocator);
        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to get clickable element by text, error - " + ex.getMessage());
        }
        return null;
    }

    private WebElement elemByXpath(String xPathSelector) {
        try {
            By xpath = By.xpath(xPathSelector);
            return driver.findElement(xpath);
        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to get element by xpath : " + xPathSelector + " , error - " + ex.getMessage());
        }
        return null;
    }
    
    private WebElement hrefByContainedText(String hrefText) {
        try {
            String elemLocator = "//a[text()='" + hrefText + "']";
            
            return this.elemByXpath(elemLocator);
            
        } catch(Exception ex) {
            System.err.println("[Selenium Error] Failed to get href by contained text : " + hrefText + " , error - " + ex.getMessage());
        }
        
        return null;
    }

    private WebElement elemByClickableOptionText(String optionText) {

        try {

            String elemLocator = "//*[text()='" + optionText + "']/..";

            return this.elemByXpath(elemLocator);

        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to get element by option text, error - " + ex.getMessage());
        }

        return null;
    }

    private WebElement elemByPageText(String pageText) {

        try {
            String elemLocator = "//*[text()='" + pageText + "']";

            return this.elemByXpath(elemLocator);

        } catch (Exception ex) {
            System.err.println("[Selemiun Error] Failed to get element by page text, error - " + ex.getMessage());
        }

        return null;
    }

    private WebElement elemByLinkText(String linkText) {
        try {
            By link = By.linkText(linkText);
            return driver.findElement(link);
        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to get element by link text, error - " + ex.getMessage());
        }
        return null;
    }

    private boolean clickElem(WebElement element) {
        try {

            this.scrollElementToView(element);

            element.click();
            return true;
        } catch (Exception ex) {
            System.err.println("[Selenium Error] Failed to click elem : " + element + ", error - " + ex.getMessage());
        }

        return false;
    }
}
