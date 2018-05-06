package util.views;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import util.SeleniumUtils;



public class PO_LoginView extends PO_RegisterView {
	
	static public void fillForm(WebDriver driver, String dnip, String passwordp) {
		SeleniumUtils.esperarSegundos(driver, 5);
		WebElement dni = driver.findElement(By.name("email"));
		dni.click();
		dni.clear();
		dni.sendKeys(dnip);
		WebElement password = driver.findElement(By.name("password"));
		password.click();
		password.clear();
		password.sendKeys(passwordp);
		// Pulsar el boton de Alta.
		By boton = By.className("btn");
		driver.findElement(boton).click();
	}
}
