package com.uniovi.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_JustLoggedInView;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_Properties;
import com.uniovi.tests.pageobjects.PO_RegisterView;
import com.uniovi.tests.pageobjects.PO_View;
import com.uniovi.tests.util.SeleniumUtils;

//Ordenamos las pruebas por el nombre del método 
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SocialNetworkTests {

	// En Windows (Debe ser la versión 46.0 y desactivar las actualizacioens
	// automáticas)):
	// static String PathFirefox =
	// "E:\\Clase\\UNIOVI\\5_Quinto_Curso\\SDI\\PL_SDI_5\\Firefox46.0.win\\Firefox46.win\\FirefoxPortable.exe";
	static String PathFirefox = "C:\\Users\\Marcos\\Downloads\\Firefox46.win\\FirefoxPortable.exe";

	// Común a Windows y a MACOSX
	static WebDriver driver = getDriver(PathFirefox);
	static String URL = "http://localhost:8090";

	public static WebDriver getDriver(String PathFirefox) {
		// Firefox (Versión 46.0) sin geckodriver para Selenium 2.x.
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	// Antes de cada prueba se navega al URL home de la aplicaciónn
	@Before
	public void setUp() {
		driver.navigate().to(URL);
	}

	// Después de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	// Al finalizar la última prueba
	@AfterClass
	static public void end() {
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	// PR01. Acceder a la página principal
	@Test
	public void PR01() {
		PO_HomeView.checkWelcome(driver, PO_Properties.getSPANISH());
	}

	// PR02. OPción de navegación. Pinchar en el enlace Registro en la página home
	@Test
	public void PR02() {
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
	}

	// PR03. OPción de navegación. Pinchar en el enlace Identificate en la página
	// home
	@Test
	public void PR03() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
	}

	// PR04. OPción de navegación. Cambio de idioma de Español a Ingles y vuelta a
	// Español
	@Test
	public void PR04() {
		PO_HomeView.checkChangeIdiom(driver, "btnSpanish", "btnEnglish", PO_Properties.getSPANISH(),
				PO_Properties.getENGLISH());
		// SeleniumUtils.esperarSegundos(driver, 2);
	}

	// PR01 [RegVal] Registro de Usuario con datos válidos.
	@Test
	public void RegVal() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "pitina@gmail.com", "Huguín", "De Vegadeo", "123456", "123456");
		// Comprobamos que entramos en la sección privada
		PO_JustLoggedInView.checkAuthenticated(driver, PO_Properties.getSPANISH());
	}

	// PR02 [RegInval] Registro de Usuario con datos inválidos (repetición de
	// contraseña invalida).
	@Test
	public void RegInval() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "maricarmen@gmail.com", "Maria", "Del Carmen", "123456", "23456");
		PO_View.getP();
		// COmprobamos el error de repassword inválido.
		PO_RegisterView.checkKey(driver, "Error.signup.passwordConfirm.coincidence", PO_Properties.getSPANISH());
	}

	// PR03 [InVal] Inicio de sesión con datos válidos.
	@Test
	public void InVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada de Alumno
		PO_JustLoggedInView.checkAuthenticated(driver, PO_Properties.getSPANISH());
	}

	// PR05 [LisUsrVal] Acceso al listado de usuarios desde un usuario en sesión
	@Test
	public void LisUsrVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		PO_JustLoggedInView.checkAuthenticated(driver, PO_Properties.getSPANISH());
		List<WebElement> elementos;
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href,'/user/list')]");
		elementos.get(0).click();
		// Contamos el número de filas de notas
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
	}

	// PR06 [LisUsrInVal] Intento de acceso con URL desde un usuario no identificado
	// al listado de usuarios
	// desde un usuario en sesión. Debe producirse un acceso no permitido a vistas
	// privadas.
	@Test
	public void LisUsrInVal() {
		driver.navigate().to("http://localhost:8090/user/list?searchText=deniedAccess");
		PO_LoginView.checkLogIn(driver, PO_Properties.getSPANISH());
	}

	// PR07 [BusUsrVal] Realizar una búsqueda valida en el listado de usuarios desde
	// un usuario en sesión.
	@Test
	public void BusUsrVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada
		PO_JustLoggedInView.checkAuthenticated(driver, PO_Properties.getSPANISH());
		// Rellenamos el campo de busqueda
		PO_JustLoggedInView.fillSearchText(driver, "Nacho");
		// Comprobamos que aparece el deseado.
		List<WebElement> elementos = PO_View.checkElement(driver, "text", "Nacho");
	}

	// PR08 [BusUsrInVal] Intento de acceso con URL a la búsqueda de usuarios desde
	// un usuario no
	// identificado. Debe producirse un acceso no permitido a vistas privadas.
	@Test
	public void BusUsrInVal() {
		driver.navigate().to("http://localhost:8090/user/list");
		PO_LoginView.checkLogIn(driver, PO_Properties.getSPANISH());
	}

	// PRO9 [InvVal] Enviar una invitación de amistad a un usuario de forma valida.
	@Test
	public void InvVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada
		PO_JustLoggedInView.checkAuthenticated(driver, PO_Properties.getSPANISH());
		List<WebElement> elementos;
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href,'/user/list')]");
		elementos.get(0).click();
		By enlace = By.xpath("//td[contains(text(), 'Marcos')]/following-sibling::*[3]");
		SeleniumUtils.esperarSegundos(driver, 1);
		driver.findElement(enlace).click();
		// Comprobación que ahora aparece el botón Cancelar y no el de agregar
		// en la fila del usuario Marcos.
		PO_View.checkElement(driver, "id", "cancelRequestButton2");
	}

	// PR10 [InvInVal] Enviar una invitación de amistad a un usuario al que ya le
	// habíamos invitado la invitación
	// previamente. No debería dejarnos enviar la invitación, se podría ocultar el
	// botón de enviar invitación o
	// notificar que ya había sido enviada previamente.

	@Test
	public void InvInVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada
		PO_JustLoggedInView.checkAuthenticated(driver, PO_Properties.getSPANISH());
		List<WebElement> elementos;
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href,'/user/list')]");
		elementos.get(0).click();
		By enlace = By.xpath("//td[contains(text(), 'Marcos')]/following-sibling::*[3]");
		SeleniumUtils.esperarSegundos(driver, 1);
		driver.findElement(enlace).click();
		// Comprobación que ahora aparece el botón Cancelar y no el de agregar
		// en la fila del usuario Marcos, y no nos permite volver a enviar una petición.
		PO_View.checkElement(driver, "id", "cancelRequestButton2");
	}

}
