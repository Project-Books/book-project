# Book project summary

Book tracker web app made using Spring Boot and Vaadin 14 (only the free components will be used).

This app will let you:
- Add books that you would have read to a 'read' shelf
- Add books that you are currently reading to a 'currently reading' shelf
- Add books that you would like to read to a 'want to read' shelf

- View books in your different shelves and make changes

Currently, a H2 database is being used, but a MySQL database will later be used instead.

![New book form](media/book-form.png)

# Setup

This uses Java version 11, so ensure you first have the JDK 11 installed.

1. Clone the repository
2. Import the project as a maven project into your favourite IDE
3. Run `BookProjectApplication.java`
4. Go to localhost:8080