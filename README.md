# Book Project

[![Gitter chat](https://badges.gitter.im/gitterHQ/gitter.png)](gitter.im/book-project-community/)

## Summary

Book tracker web app made using Spring Boot and Vaadin 14 (only the free components will be used).

*Features:*
- Add books that you have read to a 'to read', 'currently reading', 'read' or 'did not finish' shelf
- View books in your different shelves and make changes

*Coming soon:*
- Reading challenge: set yourself a goal of how many books you to read this year and track your progress towards accomplishing it
- Add your own shelves
- User registration and accounts
- And much more!

<p align="center">
    <img src="/media/book_form.png" alt="New book form"/>
</p>

![Books in shelf](/media/books_in_shelf.png)

*The images above may look slightly different to the app. If major changes are made, new images will be uploaded to 
reflect this.*

## Setup

Prerequisites: JDK 11 (or higher), Node.js and npm

1. Clone the repository
2. Import the project as a maven project into your favourite IDE (or run maven on the terminal)
3. Run `BookProjectApplication.java`
4. Go to `localhost:8080`
5. Log in with the details below:
    - Username: `user`
    - Password: `password`

To access the h2 database:

1. Go to `http://localhost:8080/h2-console`
2. Ensure you log in with the settings below. The password field is intentionally left blank.
    - Saved settings: `Generic H2 (Embedded)`
    - Setting Name: `Generic H2 (Embedded)`
    - Driver class: `org.h2.Driver`
    - JDBC URL: `jdbc:h2:mem:testdb`
    - User Name: `sa`
    - Password: 
3. Click on connect

## Contributing

If you wish to contribute (thanks!), please first see the [contributing document](https://github.com/knjk04/book-project/blob/master/CONTRIBUTING.md).

## Further information

For more information, such as a roadmap and the underlying principles of the project, see the [Book Project wiki](https://github.com/knjk04/book-project/wiki).
