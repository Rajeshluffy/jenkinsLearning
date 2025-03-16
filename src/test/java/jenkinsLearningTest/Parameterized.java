package jenkinsLearningTest;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Parameterized {
	@Test
	public void login() {
		RemoteWebDriver driver=null;
		String browser = System.getProperty("env", "chrome");
		switch (browser) {
		case "chrome":
			driver=new ChromeDriver();
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
