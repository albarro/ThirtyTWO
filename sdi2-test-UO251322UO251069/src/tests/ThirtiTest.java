package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.bson.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import util.SeleniumUtils;
import util.views.PO_HomeView;
import util.views.PO_LoginView;
import util.views.PO_NavView;
import util.views.PO_PrivateView;
import util.views.PO_RegisterView;
import util.views.PO_View;
     
        
//Ordenamos las pruebas por el nombre del mÃ©todo
//	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ThirtiTest {
	// En Windows (Debe ser la versiÃ³n 46.0 y desactivar las actualizacioens
	// automÃ¡ticas)):
	static String PathFirefox = "C:\\Users\\Álvaro\\Desktop\\Firefox46.win\\FirefoxPortable.exe";
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
		MongoClientURI uri  = new MongoClientURI("mongodb://admin:sdi_2018@ds139219.mlab.com:39219/red-social"); 
        MongoClient client = new MongoClient(uri);
        MongoDatabase db = client.getDatabase(uri.getDatabase());
        
        MongoCollection<Document> usuarios = db.getCollection("usuarios");
        Document user = new Document();
        user.append("email", "pruebaDefinitiva@prueba.com");
        usuarios.deleteOne(user);

        driver.manage().deleteAllCookies();
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
		PO_HomeView.clickOption(driver, "desconectarse", "class", "btn btn-primary");
	}

	//2.2 [InInVal] Inicio de sesión con datos inválidos (usuario no existente en la aplicación).
	@Test
	public void PR022() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pruebaDefinitiva1@prueba.com", "55555");
		PO_View.checkElement(driver, "text", "Identificación");
	}

	
	@Test
	public void PR031() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pruebaDefinitiva@prueba.com", "55555");
		PO_View.checkElement(driver, "text", "Bienvenido"); 

		// Comprobamos que los elementos son 5 dado que la pagina esta llena.
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		SeleniumUtils.esperarSegundos(driver, 2);
		assertEquals(5, elementos.size());
		
		PO_HomeView.clickOption(driver, "desconectarse", "class", "btn btn-primary");
	}


	@Test
	public void PR032() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pruebaDefinitiva2@prueba.com", "55555");
		PO_View.checkElement(driver, "text", "de usuario");

		driver.navigate().to("http://localhost:8081/usuarios");
		PO_View.checkElement(driver, "text", "de usuario");
	}

	@Test
	public void PR041() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pruebaDefinitiva@prueba.com", "55555");
		PO_View.checkElement(driver, "text", "Bienvenido");

		PO_PrivateView.fillCampoBusqueda(driver, "Cesar");
		
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
		
		PO_HomeView.clickOption(driver, "desconectarse", "class", "btn btn-primary");
	}

	
	@Test
	public void PR042() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pruebaDefinitiva2@prueba.com", "55555");
		PO_View.checkElement(driver, "text", "de usuario");

		driver.navigate().to("http://localhost:8081/usuarios?busqueda=1");
		PO_View.checkElement(driver, "text", "de usuario");
	}

	
	@Test
	public void PR051() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pruebaDefinitiva@prueba.com", "55555");
		PO_View.checkElement(driver, "text", "Bienvenido");

		SeleniumUtils.esperarSegundos(driver, 1);
		
		By enlace = By.xpath("//td[contains(text(), 'Cesar')]/following-sibling::*[2]");
		driver.findElement(enlace).click();
		
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_View.checkElement(driver, "text", "Solicidud enviada con exito");
		
		PO_HomeView.clickOption(driver, "desconectarse", "class", "btn btn-primary");;
	}
	
	@Test
	public void PR052() {
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pruebaDefinitiva@prueba.com", "55555");
		PO_View.checkElement(driver, "text", "Bienvenido");

		SeleniumUtils.esperarSegundos(driver, 1);
		
		By enlace = By.xpath("//td[contains(text(), 'Cesar')]/following-sibling::*[2]");
		assertEquals("Ya se ha eniado una solicitud", driver.findElement(enlace).getText());
		
		
		PO_HomeView.clickOption(driver, "desconectarse", "class", "btn btn-primary");;
	}

	/**
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
