package jenkinsLearningTest;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SimpleTest {
	@Test
	public void login() {
		ChromeDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.google.co.in/");
		String title = driver.getTitle();

		Assert.assertEquals(title, "Google");
	}
}
