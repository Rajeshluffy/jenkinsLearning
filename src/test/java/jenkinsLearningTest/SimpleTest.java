package jenkinsLearningTest;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SimpleTest {
	ChromeDriver driver;
	@Test
	public void login() {
		//Chrome Driver improvements
		// Poll scm trigger
ChromeOptions options = new ChromeOptions();

// 1. Run without a GUI (Mandatory for Docker)
options.addArguments("--headless=new"); 

// 2. Disable sandbox security (Mandatory because Docker runs as root)
options.addArguments("--no-sandbox"); 

// 3. Prevent Chrome from crashing due to Docker's limited /dev/shm memory
options.addArguments("--disable-dev-shm-usage"); 

// 4. (Optional but recommended) Set a default window size for headless testing
options.addArguments("--window-size=1920,1080"); 


		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.get("https://www.google.co.in/");
		String pageTitle = driver.getTitle();

		Assert.assertEquals(pageTitle, "Google");
		driver.quit();
	}
}
