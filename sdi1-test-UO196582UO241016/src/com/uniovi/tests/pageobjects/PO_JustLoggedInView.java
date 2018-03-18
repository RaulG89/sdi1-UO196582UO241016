package com.uniovi.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.uniovi.tests.util.SeleniumUtils;

public class PO_JustLoggedInView extends PO_NavView {
	
	static public void checkAuthenticated(WebDriver driver, int language) {
		SeleniumUtils.EsperaCargaPagina(driver, "text", p.getString("authenticateduser.message", language), getTimeout());
	}
	
	public static void fillSearchText(WebDriver driver, String searchTextp) {
		// TODO Auto-generated method stub
		WebElement searchText = driver.findElement(By.name("searchText"));
		searchText.click();
		searchText.clear();
		searchText.sendKeys(searchTextp);
		// Pulsar el boton de Alta.
		By boton = By.className("btn");
		driver.findElement(boton).click();
	}

}
