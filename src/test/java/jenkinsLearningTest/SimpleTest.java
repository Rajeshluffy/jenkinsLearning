package jenkinsLearningTest;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SimpleTest {
	ChromeDriver driver;
	@Test
	public void login() {
		//Chrome Driver improvements
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.google.co.in/");
		String pageTitle = driver.getTitle();

		Assert.assertEquals(pageTitle, "Google");
		driver.quit();
	}
}
