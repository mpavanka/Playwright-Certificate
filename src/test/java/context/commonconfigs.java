package context;

import com.google.gson.JsonObject;
import com.microsoft.playwright.*;

import java.net.URLEncoder;

public class commonconfigs {

    private Page page;
    private String cdpUrl;
    private Browser browser;
    private BrowserContext context;
    private Playwright playwright;
    private BrowserType browserType;
    private final String username = "pavankalyan68335";
    private final String accessKey = "CtSOjmjSWEH8nb5P1izMThNjbHdGVC2fcoZ2Is3HtKDlxAieCK";

    public void openBrowser() {
        playwright = Playwright.create();
        lamdaSetup();

        String browserTypeString = System.getProperty("BrowserType", "chrome"); // Default to chrome
        System.out.println("BrowserType: " + browserTypeString);

        boolean headless = Boolean.parseBoolean(System.getProperty("headLess", "true")); // Default to headless

        browserType = getBrowserType(browserTypeString);
        if (cdpUrl != null) {
            browser = browserType.connect(cdpUrl);
        } else {
            browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(headless));
        }

        context = browser.newContext();
        page = context.newPage();
    }

    private BrowserType getBrowserType(String browserTypeString) {
        return switch (browserTypeString.toLowerCase()) {
            case "chrome", "chromium" -> playwright.chromium();
            case "edge", "webkit" -> playwright.webkit();
            case "firefox" -> playwright.firefox();
            default -> throw new IllegalArgumentException("Browser not supported: " + browserTypeString);
        };
    }

    public Page getPage() {
        if (page == null) {
            throw new IllegalStateException("Browser is not initialized. Call openBrowser() first.");
        }
        return page;
    }

    private void lamdaSetup() {
        JsonObject capabilities = new JsonObject();
        JsonObject ltOptions = new JsonObject();

        capabilities.addProperty("browserName", "Chrome");
        capabilities.addProperty("browserVersion", "latest");
        ltOptions.addProperty("platform", "Windows 10");
        ltOptions.addProperty("name", "Playwright Test");
        ltOptions.addProperty("build", "Playwright Java Build");
        ltOptions.addProperty("user", username);
        ltOptions.addProperty("accessKey", accessKey);
        capabilities.add("LT:Options", ltOptions);

        try {
            cdpUrl = "wss://cdp.lambdatest.com/playwright?capabilities=" + URLEncoder.encode(capabilities.toString(), "utf-8");
        } catch (Exception e) {
            System.err.println("Failed to encode LambdaTest capabilities: " + e.getMessage());
            cdpUrl = null; // Fallback to local execution
        }
    }

    public void teardown() {
        if (page != null) page.close();
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
