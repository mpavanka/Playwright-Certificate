package context;

import com.google.gson.JsonObject;
import com.microsoft.playwright.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class commonconfigs {

    Page page;
    String cdpUrl;
    Browser browser;
    BrowserContext context;
    Playwright playwright;
    BrowserType browserType;


    String username = "pavankalyan68335";
    String accesskey = "CtSOjmjSWEH8nb5P1izMThNjbHdGVC2fcoZ2Is3HtKDlxAieCK";
    RemoteWebDriver driver = null;
    String gridURL = "@hub.lambdatest.com/wd/hub";
    boolean status = false;

    public void openBrowser() {
        playwright = Playwright.create();
        this.lamdaSetup();
//        String browserType = System.getenv("BrowserType");
        System.out.println("BrowserType: " + browserType);
        boolean headLess = Boolean.parseBoolean((System.getProperty("headLess")));

        switch ("edge") {
            case "chrome" -> {
                browserType = playwright.chromium();

            }
            case "edge" ->
                    browserType = playwright.webkit();
            case "webkit" ->
                    browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headLess));
            case "Firefox" ->
                    browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headLess));
            default -> throw new IllegalArgumentException("Browser not supported: " + browserType);
        }
        browser = browserType.connect(cdpUrl)
       ;
        context = browser.newContext();

    }

    public Page getPage() {
        return page = browser.newPage();
    }


    public void lamdaSetup() {
        JsonObject capabilities = new JsonObject();
        JsonObject ltOptions = new JsonObject();
//
//        String user = System.getenv("LT_USERNAME");
//        String accessKey = System.getenv("LT_ACCESS_KEY");

        capabilities.addProperty("browsername", "Chrome"); // Browsers allowed: `Chrome`, `MicrosoftEdge`, `pw-chromium`, `pw-firefox` and `pw-webkit`
        capabilities.addProperty("browserVersion", "latest");
        ltOptions.addProperty("platform", "Windows 10");
        ltOptions.addProperty("name", "Playwright Test");
        ltOptions.addProperty("build", "Playwright Java Build");
        ltOptions.addProperty("user", username);
        ltOptions.addProperty("accessKey", accesskey);
        capabilities.add("LT:Options", ltOptions);

//        BrowserType chromium = playwright.chromium();
//        String caps = URLEncoder.encode(capabilities.toString(), "utf-8");
         cdpUrl = "wss://cdp.lambdatest.com/playwright?capabilities=" + capabilities;
//        browser = chromium.connect(cdpUrl);


    }

    public void teardown() {
        page.close();
        context.close();
        browser.close();
        playwright.close();
    }
}

