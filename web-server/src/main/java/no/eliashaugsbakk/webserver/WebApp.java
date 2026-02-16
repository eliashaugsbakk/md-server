package no.eliashaugsbakk.webserver;


import no.eliashaugsbakk.webserver.db.DatabaseManager;

import java.io.IOException;

class WebApp {

	public static void main(String[] args) throws Exception {
		String dbPath = System.getenv().getOrDefault("DB_PATH", "pages.db");
		DatabaseManager db = new DatabaseManager(dbPath);

		db.initialize();
		db.seed();

		try {
			Server server = new Server();
			server.start(db);
		} catch (IOException e) {
			System.err.println("Could not start server: " + e.getMessage());
		}
	}
}