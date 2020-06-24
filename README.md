# Book Project

[![Build Status](https://travis-ci.com/knjk04/book-project.svg?branch=master)](https://travis-ci.com/knjk04/book-project)
[![Gitter](https://badges.gitter.im/book-project-community/community.svg)](https://gitter.im/book-project-community/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

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
    
In IntelliJ, you may find lots of errors for things like the log statements and the entities not having constructors.
To remove the errors in IntelliJ, install the [Lombok plugin](https://plugins.jetbrains.com/plugin/6317-lombok) and enable annotation 
processing. This can be done either in the popup window that appears after installing the Lombok plugin or by checking the
'Enable annotation processing' checkbox in Settings > Build, Execution, Deployment > Compiler > Annotation Processors.
Otherwise, you can safely ignore these flagged errors.

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

## Acknowledgements

- [Project Lombok](https://projectlombok.org/) - [MIT License](http://www.opensource.org/licenses/mit-license.php)
