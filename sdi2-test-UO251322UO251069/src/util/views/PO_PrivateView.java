package util.views;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class PO_PrivateView extends PO_NavView {
	static public void fillFormAddMark(WebDriver driver, int userOrder, String descriptionp, String scorep) {
		// Seleccionamos el alumnos userOrder
		new Select(driver.findElement(By.id("user"))).selectByIndex(userOrder);
		// Rellenemos el campo de descripci�n
		WebElement description = driver.findElement(By.name("description"));
		description.clear();
		description.sendKeys(descriptionp);
		WebElement score = driver.findElement(By.name("score"));
		score.click();
		score.clear();
		score.sendKeys(scorep);
		By boton = By.className("btn");
		driver.findElement(boton).click();
	}
	
	static public void fillCampoBusqueda(WebDriver driver,  String texto) {
		WebElement busqueda = driver.findElement(By.name("busqueda"));
		busqueda.clear();
		busqueda.sendKeys(texto);
		By boton = By.className("btn");
		driver.findElement(boton).click();
	}
}