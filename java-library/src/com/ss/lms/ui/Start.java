package com.ss.lms.ui;

import java.sql.SQLException;

public class Start {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Menus menu = new Menus();
		//menu.showBooks();
		menu.MainMenu();
	}
}