package context;

import com.microsoft.playwright.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class commonconfigs {

    Page page;
    Browser browser;
    BrowserContext context;
    Playwright playwright;

    String username = "pavankalyan68335";
    String accesskey = "CtSOjmjSWEH8nb5P1izMThNjbHdGVC2fcoZ2Is3HtKDlxAieCK";
    RemoteWebDriver driver = null;
    String gridURL = "@hub.lambdatest.com/wd/hub";
    boolean status = false;

    public void openBrowser() {
        this.lamdaSetup();
        playwright = Playwright.create();
        String browserType = System.getenv("BrowserType");
        System.out.println("BrowserType: " + browserType);
        boolean headLess = Boolean.parseBoolean((System.getProperty("headLess")));
        switch ("chrome") {
            case "chrome" -> {
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chromium").setHeadless(headLess));
            }
            case "edge" ->
                    browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(headLess));
            case "webkit" ->
                    browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headLess));
            case "Firefox" ->
                    browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headLess));
            default -> throw new IllegalArgumentException("Browser not supported: " + browserType);
        }
        context = browser.newContext();
        page = context.newPage();
    }

    public Page getPage() {
        return page;
    }


    public void lamdaSetup() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("version", "130");
        capabilities.setCapability("platform", "win11"); // If this cap isn't specified, it will just get any available one.
        capabilities.setCapability("build", "LambdaTestSampleApp");
        capabilities.setCapability("name", "LambdaTestJavaSample");
        try {
            driver = new RemoteWebDriver(new URL("https://" + username + ":" + accesskey + gridURL), capabilities);
        } catch (MalformedURLException e) {
            System.out.println("Invalid grid URL");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void teardown() {
        page.close();
        context.close();
        browser.close();
        playwright.close();
    }
}

