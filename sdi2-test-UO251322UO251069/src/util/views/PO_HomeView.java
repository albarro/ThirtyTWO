package util.views;

import org.openqa.selenium.WebDriver;

import util.SeleniumUtils;


public class PO_HomeView extends PO_NavView {

	static public void checkWelcome(WebDriver driver, int language) {
		// Esperamos a que se cargue el saludo de bienvenida en Espa�ol
		SeleniumUtils.EsperaCargaPagina(driver, "text", p.getString("welcome.message", language),
				getTimeout());
	}


}
