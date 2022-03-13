package com.karankumar.bookproject.util;

import lombok.SneakyThrows;
import org.testcontainers.containers.PostgreSQLContainer;

public class BookPostgreSQLContainer extends PostgreSQLContainer<BookPostgreSQLContainer> {
	private static final String IMAGE_VERSION = "postgres:14";
	private static BookPostgreSQLContainer container;

	private BookPostgreSQLContainer() {
		super(IMAGE_VERSION);
	}

	@SneakyThrows
	public static BookPostgreSQLContainer getInstance() {
		if (container == null) {
			container = new BookPostgreSQLContainer()
					.withDatabaseName("book_project_db");
		}
		return container;
	}

	@Override
	public void start() {
		super.start();
	}

	@Override
	public void stop() {
		//do nothing, JVM handles shut down
	}
}