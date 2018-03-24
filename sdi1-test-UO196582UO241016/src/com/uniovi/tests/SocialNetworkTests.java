package com.uniovi.tests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.uniovi.tests.pageobjects.PO_AddPublicationView;
import com.uniovi.tests.pageobjects.PO_AdminLoginView;
import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_JustLoggedInView;
import com.uniovi.tests.pageobjects.PO_ListUsers;
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
	static String PathFirefox = "E:\\Clase\\UNIOVI\\5_Quinto_Curso\\SDI\\PL_SDI_5\\Firefox46.0.win"
			+ "\\Firefox46.win\\FirefoxPortable.exe";
	// static String PathFirefox =
	// "C:\\Users\\UO241016\\Downloads\\PL_SDI_5\\PL_SDI_5\\Firefox46.0.win"
	// + "\\Firefox46.win\\FirefoxPortable.exe";
	// static String PathFirefox =
	// "C:\\Users\\Marcos\\Downloads\\Firefox46.win\\FirefoxPortable.exe";

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

	// PR01 [RegVal] Registro de Usuario con datos válidos.
	@Test
	public void PR01_RegVal() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "pitina@gmail.com", "Huguín",
				"De Vegadeo", "123456", "123456");
		// Comprobamos que entramos en la sección privada
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
	}

	// PR02 [RegInval] Registro de Usuario con datos inválidos (repetición de
	// contraseña invalida).
	@Test
	public void PR02_RegInval() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "maricarmen@gmail.com", "Maria",
				"Del Carmen", "123456", "23456");
		PO_View.getP();
		// COmprobamos el error de repassword inválido.
		PO_RegisterView.checkKey(driver,
				"Error.signup.passwordConfirm.coincidence",
				PO_Properties.getSPANISH());
	}

	// PR03 [InVal] Inicio de sesión con datos válidos.
	@Test
	public void PR03_InVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada de Alumno
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
	}

	// PR04 [InInVal] Inicio de sesión con datos inválidos (usuario no existente
	// en
	// la aplicación).
	@Test
	public void PR04_InInVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "12345");
		// COmprobamos que aparece el error de que las credenciales introducidas
		// son
		// inválidas.
		PO_LoginView.checkInvalidLogIn(driver, PO_Properties.getSPANISH());
	}

	// PR05 [LisUsrVal] Acceso al listado de usuarios desde un usuario en sesión
	@Test
	public void PR05_LisUsrVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
		List<WebElement> elementos;
		elementos = PO_View.checkElement(driver, "free",
				"//a[contains(@href,'/user/list')]");
		elementos.get(0).click();
		// Contamos el número de filas de notas
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free",
				"//tbody/tr", PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
	}

	// PR06 [LisUsrInVal] Intento de acceso con URL desde un usuario no
	// identificado
	// al listado de usuarios
	// desde un usuario en sesión. Debe producirse un acceso no permitido a
	// vistas
	// privadas.
	@Test
	public void PR06_LisUsrInVal() {
		driver.navigate()
				.to("http://localhost:8090/user/list?searchText=deniedAccess");
		PO_LoginView.checkLogIn(driver, PO_Properties.getSPANISH());
	}

	// PR07 [BusUsrVal] Realizar una búsqueda valida en el listado de usuarios
	// desde
	// un usuario en sesión.
	@Test
	public void PR07_BusUsrVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
		// Accedemos a la vista de usuarios
		List<WebElement> elementos;
		elementos = PO_View.checkElement(driver, "free",
				"//a[contains(@href,'/user/list')]");
		elementos.get(0).click();
		// Rellenamos el campo de busqueda
		PO_ListUsers.fillSearchText(driver, "Nacho");
		// Comprobamos que aparece el deseado.
		PO_View.checkElement(driver, "text", "Nacho");
	}

	// PR08 [BusUsrInVal] Intento de acceso con URL a la búsqueda de usuarios
	// desde
	// un usuario no
	// identificado. Debe producirse un acceso no permitido a vistas privadas.
	@Test
	public void PR08_BusUsrInVal() {
		driver.navigate().to("http://localhost:8090/user/list");
		PO_LoginView.checkLogIn(driver, PO_Properties.getSPANISH());
	}

	// PRO9 [InvVal] Enviar una invitación de amistad a un usuario de forma
	// valida.
	@Test
	public void PR09_InvVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
		List<WebElement> elementos;
		elementos = PO_View.checkElement(driver, "free",
				"//a[contains(@href,'/user/list')]");
		elementos.get(0).click();
		By enlace = By.xpath(
				"//td[contains(text(), 'Marcos')]/following-sibling::*[3]");
		SeleniumUtils.esperarSegundos(driver, 1);
		driver.findElement(enlace).click();
		// Comprobación que ahora aparece el botón Cancelar y no el de agregar
		// en la fila del usuario Marcos.
		PO_View.checkElement(driver, "id", "cancelRequestButton2");
	}

	// PR10 [InvInVal] Enviar una invitación de amistad a un usuario al que ya
	// le
	// habíamos invitado la invitación
	// previamente. No debería dejarnos enviar la invitación, se podría ocultar
	// el
	// botón de enviar invitación o
	// notificar que ya había sido enviada previamente.

	@Test
	public void PR10_InvInVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
		List<WebElement> elementos;
		elementos = PO_View.checkElement(driver, "free",
				"//a[contains(@href,'/user/list')]");
		elementos.get(0).click();
		By enlace = By.xpath(
				"//td[contains(text(), 'Martin')]/following-sibling::*[3]");
		SeleniumUtils.esperarSegundos(driver, 1);
		driver.findElement(enlace).click();
		// Comprobación que ahora aparece el botón Cancelar y no el de agregar
		// en la fila del usuario Marcos, y no nos permite volver a enviar una
		// petición.
		PO_View.checkElement(driver, "id", "cancelRequestButton2");
	}

	// PR11 [LisInvVal] Listar las invitaciones recibidas por un usuario,
	// realizar
	// la comprobación con una lista
	// que al menos tenga una invitación recibida.
	@Test
	public void PR11_LisInvVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
		List<WebElement> elementos;
		elementos = PO_View.checkElement(driver, "free",
				"//a[contains(@href,'/user/friendRequests')]");
		elementos.get(0).click();
		// Comprobamos que aparecen dos botones de aceptar, por lo tanto,
		// existen 2
		// peticiones de amistad pendientes.
		elementos = PO_View.checkElement(driver, "free",
				"//td/following-sibling::*[1]");
		assertTrue(elementos.size() == 2);
	}

	// PR12 [AcepInvVal] Aceptar una invitación recibida
	@Test
	public void PR12_AcepInvVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
		List<WebElement> elementos;
		elementos = PO_View.checkElement(driver, "free",
				"//a[contains(@href,'/user/friendRequests')]");
		elementos.get(0).click();
		// Hacemos click en el botón de Aceptar la invitación.
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'Nacho')]/following-sibling::*[1]");
		elementos.get(0).click();
		// Comprobamos que no aparece en la página de peticiones.
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, "Nacho",
				PO_View.getTimeout());
	}

	// PR13 [ListAmiVal] Listar los amigos de un usuario, realizar la
	// comprobación
	// con una lista que al menos
	// tenga un amigo.
	@Test
	public void PR13_ListAmiVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
		List<WebElement> elementos;
		elementos = PO_View.checkElement(driver, "free",
				"//a[contains(@href,'/user/friends')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'Nacho')]");
		assertTrue(elementos.size() == 1);
	}

	// PR14 [PubVal] Crear una publicación con datos válidos.
	@Test
	public void PR14_PubVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver,
				"id", "btnPubli", PO_View.getTimeout());
		elementos.get(0).click();
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id",
				"publiDropdownMenuButton", PO_View.getTimeout());
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "addPubli",
				PO_View.getTimeout());
		elementos.get(0).click();
		PO_View.checkElement(driver, "text", "Añade una nueva publicación");
		PO_AddPublicationView.fillForm(driver, "Titulo publicación válida",
				"Texto para una publicación válida");
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'Titulo publicación válida')]");
		assertTrue(elementos.size() == 1);
	}

	// PR15 [LisPubVal] Acceso al listado de publicaciones desde un usuario en
	// sesión
	@Test
	public void PR15_LisPubVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "anaponfe@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
		List<WebElement> elementos = PO_View.checkElement(driver, "free",
				"//li[contains(@id, 'publications-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free",
				"//a[contains(@href, 'publication/list')]");
		elementos.get(0).click();
		// Comprobamos que se lista la publicación
		PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'Prueba Publicación')]");
	}

	// PR16 [LisPubAmiVal] Listar las publicaciones de un usuario amigo
	@Test
	public void PR16_LisPubAmiVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
		List<WebElement> elementos = PO_View.checkElement(driver, "free",
				"//a[contains(@href,'/user/friends')]");
		elementos.get(0).click();
		// Hacemos click sobre el botón de las publicaciones del amigo con
		// nombre Ana
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'Ana')]/following-sibling::*[2]");
		elementos.get(0).click();
		// Comprobamos que se lista la publicación
		PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'Prueba Publicación')]");
	}

	// PR17 [LisPubAmiInVal] Utilizando un acceso vía URL tratar de listar las
	// publicaciones de un usuario que
	// no sea amigo del usuario identificado en sesión.
	@Test
	public void PR17_LisPubAmiInVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "pocito@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
		// Navegamos hacia la URL no permitida de visualización de las
		// publicaciones del
		// usuario 10, que no es amigo
		driver.navigate().to("http://localhost:8090/publication/10");
		// Comprobamos que te devuelve a la pagina home
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
	}

	// PR18 [PubFot1Val] Crear una publicación con datos cálidos y una foto
	// adjunta
	@Test
	public void PR18_PubFot1Val() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
		// Seleccionamos el menú de añadir publicación
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver,
				"id", "btnPubli", PO_View.getTimeout());
		elementos.get(0).click();
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id",
				"publiDropdownMenuButton", PO_View.getTimeout());
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "addPubli",
				PO_View.getTimeout());
		elementos.get(0).click();
		// Comprobamos que estamso en el formulario de añadir publicación
		PO_View.checkElement(driver, "text", "Añade una nueva publicación");
		// Creamos la publicación
		PO_AddPublicationView.fillForm(driver,
				"Titulo publicación válida con foto",
				"Texto para una publicación válida con foto",
				System.getProperty("user.dir") + "\\imagen.jpg");
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'Titulo publicación válida con foto')]");
		assertTrue(elementos.size() == 1);
	}

	// PR19 [PubFot2Val] Crear una publicación con datos cálidos y sin una foto
	// adjunta
	@Test
	public void PR19_PubFot2Val() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
		// Seleccionamos el menú de añadir publicación
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver,
				"id", "btnPubli", PO_View.getTimeout());
		elementos.get(0).click();
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id",
				"publiDropdownMenuButton", PO_View.getTimeout());
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "addPubli",
				PO_View.getTimeout());
		elementos.get(0).click();
		// Comprobamos que estamso en el formulario de añadir publicación
		PO_View.checkElement(driver, "text", "Añade una nueva publicación");
		// Creamos la publicación
		PO_AddPublicationView.fillForm(driver,
				"Titulo publicación válida sin foto",
				"Texto para una publicación válida sin foto");
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'Titulo publicación válida sin foto')]");
		assertTrue(elementos.size() == 1);
	}

	// PR20 [AdInVal] Inicio de sesión como administrador con datos válidos.
	@Test
	public void PR20_AdInVal() {
		// Navegamos hacia la URL de inicio de sesion como admin
		driver.navigate().to("http://localhost:8090/admin/login");
		// Logeamos con el Admin
		PO_AdminLoginView.fillForm(driver, "yeyas@gmail.com", "123456");
		// Comprobamos que accede correctamente a la vista de los usuarios.
		PO_AdminLoginView.checkLogIn(driver, PO_Properties.getSPANISH());
	}

	// PR21 [AdInInVal] Inicio de sesión como administrador con datos inválidos
	// (usar los datos de un usuario
	// que no tenga perfil administrador).
	@Test
	public void PR21_AdInInVal() {
		// Navegamos hacia la URL de inicio de sesion como admin
		driver.navigate().to("http://localhost:8090/admin/login");
		// Logeamos con el Admin
		PO_AdminLoginView.fillForm(driver, "nachas@gmail.com", "123456");
		// Comprobamos si se ha producido un error al acceder
		PO_AdminLoginView.checkInvalidLogIn(driver, PO_Properties.getSPANISH());
	}

	// PR22 [AdLisUsrVal] Desde un usuario identificado en sesión como
	// administrador
	// listar a todos los
	// usuarios de la aplicación.
	@Test
	public void PR22_AdListUsrVal() {
		// Navegamos hacia la URL de inicio de sesion como admin
		driver.navigate().to("http://localhost:8090/admin/login");
		// Logeamos con el Admin
		PO_AdminLoginView.fillForm(driver, "yeyas@gmail.com", "123456");
		// Comprobamos que accede correctamente a la vista de los usuarios de la
		// aplicación.
		PO_AdminLoginView.checkLogIn(driver, PO_Properties.getSPANISH());
		// Si queremos accedemos a la URL de Mostrar Usuarios.
		List<WebElement> elementos = PO_View.checkElement(driver, "free",
				"//li[contains(@id, 'admin-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free",
				"//a[contains(@href, 'admin/users')]");
		elementos.get(0).click();
		// Comprobamos que accede correctamente a la vista de los usuarios de la
		// aplicación.
		PO_AdminLoginView.checkLogIn(driver, PO_Properties.getSPANISH());
	}

	// PR23 [AdBorUsrVal] Desde un usuario identificado en sesión como
	// administrador
	// eliminar un usuario
	// existente en la aplicación.
	@Test
	public void PR23_AdBorUsrVal() {
		// Navegamos hacia la URL de inicio de sesion como admin
		driver.navigate().to("http://localhost:8090/admin/login");
		// Logeamos con el Admin
		PO_AdminLoginView.fillForm(driver, "yeyas@gmail.com", "123456");
		// Comprobamos que accede correctamente a la vista de los usuarios de la
		// aplicación.
		PO_AdminLoginView.checkLogIn(driver, PO_Properties.getSPANISH());
		// Accedemos al botón de borrado del usuario
		List<WebElement> elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'Martin')]/following-sibling::*[2]");
		elementos.get(0).click();
		// Comprobamos que no aparece el usuario Martin
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, "Martin",
				PO_View.getTimeout());
	}

	// PR24 [AdBorUsrInVal] Intento de acceso vía URL al borrado de un usuario
	// existente en la aplicación.
	// Debe utilizarse un usuario identificado en sesión pero que no tenga
	// perfil de
	// administrador.
	@Test
	public void PR24_AdBorUsrInVal() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "rulas@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada
		PO_JustLoggedInView.checkAuthenticated(driver,
				PO_Properties.getSPANISH());
		driver.navigate().to("http://localhost:8090/admin/deleteuser/9");
		PO_View.checkElement(driver, "text", "Whitelabel Error Page");
	}

}
