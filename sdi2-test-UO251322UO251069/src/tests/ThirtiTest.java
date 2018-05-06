package tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import util.SeleniumUtils;
import util.views.PO_HomeView;
import util.views.PO_LoginView;
import util.views.PO_NavView;
import util.views.PO_PrivateView;
import util.views.PO_Properties;
import util.views.PO_RegisterView;
import util.views.PO_View;

//Ordenamos las pruebas por el nombre del mÃ©todo
//	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ThirtiTest {
	// En Windows (Debe ser la versiÃ³n 46.0 y desactivar las actualizacioens
	// automÃ¡ticas)):
	static String PathFirefox = "E:\\cosas\\Universidad\\3Curso\\SDI\\Firefox46.0.win\\Firefox46.win\\FirefoxPortable.exe";
	// En MACOSX (Debe ser la versiÃ³n 46.0 y desactivar las actualizaciones
	// automÃ¡ticas):
	// static String PathFirefox =
	// "/Applications/Firefox.app/Contents/MacOS/firefox-bin";
	// ComÃºn a Windows y a MACOSX
	static WebDriver driver = getDriver(PathFirefox);
	static String URL = "http://localhost:8081";

	public static WebDriver getDriver(String PathFirefox) {
		// Firefox (VersiÃ³n 46.0) sin geckodriver para Selenium 2.x.
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	// Antes de cada prueba se navega al URL home de la aplicaciÃ³nn
	@Before
	public void setUp() {
		driver.navigate().to(URL);
	}

	// DespuÃ©s de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	// Antes de la primera prueba
	@BeforeClass
	static public void begin() {
	}

	// Al finalizar la Ãºltima prueba
	@AfterClass
	static public void end() {
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	//1.1 [RegVal] Registro de Usuario con datos válidos.
	@Test
	public void PR011() {
		PO_HomeView.clickOption(driver, "registrarse", "class", "btn btn-primary");
		PO_RegisterView.fillForm(driver, "pruebaDefinitiva@prueba.com", "Cesar", "55555", "55555");
		PO_View.checkElement(driver, "text", "Nuevo usuario registrado");
	}

	//1.2 [RegInval] Registro de Usuario con datos inválidos (repetición de contraseña invalida).
	@Test
	public void PR012() {
		PO_HomeView.clickOption(driver, "registrarse", "class", "btn btn-primary");
		PO_RegisterView.fillForm(driver, "pruebaDefinitiva1@prueba.com", "Cesar", "55555", "666666");
		PO_RegisterView.checkElement(driver, "text", "deben coincidir");
	}
	
	//1.3 [RegInval] Registro de Usuario con datos inválidos (email ya registrado).
	@Test
	public void PR013() {
		PO_HomeView.clickOption(driver, "registrarse", "class", "btn btn-primary");
		PO_RegisterView.fillForm(driver, "pruebaDefinitiva@prueba.com", "Cesar", "666666", "666666");
		PO_RegisterView.checkElement(driver, "text", "Ya existe un usuario con este Email");
	}

	//2.1 [InVal] Inicio de sesión con datos válidos.
	@Test
	public void PR021() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pruebaDefinitiva@prueba.com", "55555");
		PO_View.checkElement(driver, "text", "Bienvenido");
	}

	//2.2 [InInVal] Inicio de sesión con datos inválidos (usuario no existente en la aplicación).
	@Test
	public void PR022() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pruebaDefinitiva1@prueba.com", "55555");
		PO_View.checkElement(driver, "text", "Identificación");
	}

	/**
	@Test
	public void PR031() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pruebaDefinitiva@prueba.com", "55555");
		PO_View.checkElement(driver, "text", "Bienvenido");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'users-menu')]/a");
		elementos.get(0).click();

		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'user/list')]");
		elementos.get(0).click();

		// Comprobamos que los elementos son 5 dado que la pagina esta llena.
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
		
		PO_NavView.clickOption(driver, "logout", "text", "IdentifÃ­cate");
	}

	@Test
	public void PR032() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pruebaDefinitiva2@prueba.com", "55555");
		PO_View.checkElement(driver, "text", "IdentifÃ­cate");

		driver.navigate().to("http://localhost:8090/user/list");
		PO_View.checkElement(driver, "text", "IdentifÃ­cate");
	}

	@Test
	public void PR041() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pruebaDefinitiva@prueba.com", "55555");
		PO_View.checkElement(driver, "text", "Bienvenido");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'users-menu')]/a");
		elementos.get(0).click();

		// AÃ±adir el apartado de la busqueda (Buscar 1)
		
//		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
//				PO_View.getTimeout());
//		assertTrue(elementos.size() == 1);
		
		PO_PrivateView.clickOption(driver, "logout", "text", "IdentifÃ­cate");
	}

	@Test
	public void PR042() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pruebaDefinitiva2@prueba.com", "55555");
		PO_View.checkElement(driver, "text", "IdentifÃ­cate");

		driver.navigate().to("http://localhost:8090/user/list?searchtext=1");
		PO_View.checkElement(driver, "text", "IdentifÃ­cate");
	}

	@Test
	public void PR051() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pruebaDefinitiva@prueba.com", "55555");
		PO_View.checkElement(driver, "text", "Bienvenido");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'users-menu')]/a");
		elementos.get(0).click();

		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'user/list')]");
		elementos.get(0).click();

		SeleniumUtils.esperarSegundos(driver, 1);
		
		By enlace = By.xpath("//td[contains(text(), 'prueba1@email.com')]/following-sibling::*[2]");
		driver.findElement(enlace).click();
		
		
		PO_PrivateView.clickOption(driver, "logout", "text", "IdentifÃ­cate");
	}

	@Test
	public void PR061() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "prueba1@email.com", "123456");
		PO_View.checkElement(driver, "text", "Bienvenido");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'request-menu')]/a");
		elementos.get(0).click();
		
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'request/list')]");
		elementos.get(0).click();
		
		SeleniumUtils.esperarSegundos(driver, 1);
		
		// Posee una, la invitacion previamente mandada
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
		
		PO_PrivateView.clickOption(driver, "logout", "text", "IdentifÃ­cate");
	}

	@Test
	public void PR071() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "prueba1@email.com", "123456");
		PO_View.checkElement(driver, "text", "Bienvenido");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'request-menu')]/a");
		elementos.get(0).click();
		
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'request/list')]");
		elementos.get(0).click();
		
		SeleniumUtils.esperarSegundos(driver, 1);
		
		//Aceptar la solicitud
		By enlace = By.xpath("//td[contains(text(), 'pruebaDefinitiva@prueba.com')]/following-sibling::*[2]");
		driver.findElement(enlace).click();
		
		PO_PrivateView.clickOption(driver, "logout", "text", "IdentifÃ­cate");
	}

	@Test
	public void PR081() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pruebaDefinitiva@prueba.com", "55555");
		PO_View.checkElement(driver, "text", "Bienvenido");
		
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'users-menu')]/a");
		elementos.get(0).click();

		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'user/friends')]");
		elementos.get(0).click();
		
		SeleniumUtils.esperarSegundos(driver, 1);
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 1);//Tiene el que le agrego previamente
		
		PO_PrivateView.clickOption(driver, "logout", "text", "IdentifÃ­cate");
	}
	
	**/
}
