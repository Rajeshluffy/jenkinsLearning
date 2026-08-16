package jenkinsLearningTest;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Parameterized {
	@Test
	public void login() {
		RemoteWebDriver driver=null;
		String browser = System.getProperty("env", "chrome");
		switch (browser) {
		case "chrome":
			ChromeOptions options = new ChromeOptions();

// 1. Run without a GUI (Mandatory for Docker)
options.addArguments("--headless=new"); 

// 2. Disable sandbox security (Mandatory because Docker runs as root)
options.addArguments("--no-sandbox"); 

// 3. Prevent Chrome from crashing due to Docker's limited /dev/shm memory
options.addArguments("--disable-dev-shm-usage"); 

// 4. (Optional but recommended) Set a default window size for headless testing
options.addArguments("--window-size=1920,1080"); 

 
			driver=new ChromeDriver(options);
			break;
		case "edge":
			driver = new EdgeDriver();
			break;
		default:
		    throw new RuntimeException("Invaild");
		}

		driver.manage().window().maximize();
		driver.get("https://www.google.co.in/");
		String title = driver.getTitle();

		Assert.assertEquals(title, "Google");
		driver.quit();
	}
}
