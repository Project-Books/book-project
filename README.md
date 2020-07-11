  <p align="center">
	<img src="/media/book_project_logo_banner.png" alt="Logo"/>
  </p>
  
[![Build Status](https://travis-ci.com/knjk04/book-project.svg?branch=master)](https://travis-ci.com/knjk04/book-project)
[![Gitter](https://badges.gitter.im/book-project-community/community.svg)](https://gitter.im/book-project-community/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

Book tracker web app made using Spring Boot and Vaadin 14 (only the free components will be used).

*Features:*
- Add books that you have read to a 'to read', 'currently reading', 'read' or 'did not finish' shelf
- View books in your different shelves and make changes
- A rating scale from 0-10 that that goes up in steps of 0.5
- Track your progress towards a reading goal: the number of books or pages you hope to read by the end of the year

*Coming soon:*
- Add your own shelves
- User registration and accounts
- And much more!

<p align="center">
    <img src="/media/book_form.png" alt="New book form"/>
</p>

![Books in shelf](/media/books_in_shelf.png)
        
![Reading goal](/media/reading_goal.png)

*The images above may look slightly different to the app. If major changes are made, new images will be uploaded to 
reflect this.*

## Setup

- Prerequisites: JDK 11 (or higher) 
- If you don't have Node.js installed globally, it is not needed as Vaadin will install it automatically
  - If you do have Node.js installed, please ensure it is at least version 10.0


1. Clone the repository
2. Import the project as a maven project into your favourite IDE (or run maven on the terminal)
3. Run `BookProjectApplication.java`
4. Go to `localhost:8080`
5. Log in with the details below:
    - Username: `user`
    - Password: `password`
    
You may find lots of errors for things like the log statements, or the entities not having constructors. Below, you can find instructions on how to fix this for IntelliJ and Eclipse.

### IntelliJ
    
  <p align="center">
	<img src="/media/intellij_annotation_processing.png" alt="Enable IntelliJ annotation processing"/>
  </p>
    
To remove the errors in IntelliJ, install the [Lombok plugin](https://plugins.jetbrains.com/plugin/6317-lombok) and enable annotation 
processing. This can be done either in the popup window that appears after installing the Lombok plugin or by checking the
'Enable annotation processing' checkbox in Settings > Build, Execution, Deployment > Compiler > Annotation Processors.

### Eclipse

In Eclipse, you will need to run Maven install before running the project (right click anywhere in the pom.xml and select Run as > Maven install).

### Access database

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

### Help

If you need help with anything, we'll be happy to help you in our [Gitter Channel](https://gitter.im/book-project-community/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge).

## Acknowledgements

[Project Lombok](https://projectlombok.org/) - [MIT License](http://www.opensource.org/licenses/mit-license.php)

[Karibu Testing](https://github.com/mvysny/karibu-testing) - [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0.html)
