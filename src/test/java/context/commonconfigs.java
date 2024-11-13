package context;

import com.microsoft.playwright.*;

public class commonconfigs {
    Page page;
    Browser browser;
    BrowserContext context;
    Playwright playwright;

    public void openBrowser(){
        playwright = Playwright.create();
        String browserType = System.getProperty("BrowserType");
        boolean headLess = Boolean.parseBoolean((System.getProperty("headLess")));
        switch (browserType){
            case "chrome" -> {
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chromium").setHeadless(headLess));
            }
            case "edge" ->
                    browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(headLess));
            case "webkit" ->
                    browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headLess));
            case "Firefox" ->
                    browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headLess));
            default ->
                    throw new IllegalArgumentException("Browser not supported: " + browserType);
        }
        context = browser.newContext();
        page = context.newPage();
    }

    public  Page getPage(){
        return page;
    }


    public void teardown() {
        page.close();
        context.close();
        browser.close();
        playwright.close();
    }
}
