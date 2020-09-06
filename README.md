  <p align="center">
	<img src="/media/banner/book_project_banner_dark.png" alt="Logo"/>
  </p>
  
[![Build Status](https://travis-ci.com/knjk04/book-project.svg?branch=master)](https://travis-ci.com/knjk04/book-project)
[![codecov](https://codecov.io/gh/knjk04/book-project/branch/master/graph/badge.svg)](https://codecov.io/gh/knjk04/book-project)
[![Gitter](https://badges.gitter.im/book-project-community/community.svg)](https://gitter.im/book-project-community/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/595ed2c299d7429e9938894c385b9cab)](https://app.codacy.com/manual/knjk04/book-project?utm_source=github.com&utm_medium=referral&utm_content=knjk04/book-project&utm_campaign=Badge_Grade_Dashboard)

Book tracker web app made using Spring Boot and Vaadin 14 (only the free components will be used).

*Features:*
- Add books that you have read to a 'to read', 'currently reading', 'read' or 'did not finish' shelf
- View books in your different shelves and make changes
- A rating scale from 0-10 that that goes up in steps of 0.5
- Track your progress towards a reading goal: the number of books or pages you hope to read by the end of the year
- Statistics about your reading habits
- Add your own shelves

*Coming soon:*
- User registration and accounts
- And much more!

<p align="center">
    <img src="/media/readme/book_form.png" alt="New book form"/>
</p>

![Books in shelf](/media/readme/books_in_shelf.png)
        
![Reading goal](/media/readme/reading_goal.png)

<p align="center">
    <img src="/media/readme/statistics.png" alt="Reading statistics"/>
</p>


*The images above may look slightly different to the app. If major changes are made, new images will be uploaded to 
reflect this.*

## Setup

Prerequisites:
- JDK 11 (or higher) 
- If you don't have Node.js installed globally, it is not needed as Vaadin will install it automatically
  - If you do have Node.js installed, please ensure it is at least version 10.0
- MySQL 8.0.* or (better) Docker
 
### Running the app
  
If you want to use Docker, follow one of the two appoaches (if you use Windows, follow the first approach):

#### 1. Start locally with only MySQL running in docker

1. Clone the repository
2. Import the project as a maven project into your favourite IDE (or run maven on the terminal)
3. Build the project using `mvn clean install`
4. Start MySQL database using `docker-compose up -d mysql`
5. Start the application using `java -jar target/book-project-0.0.1-SNAPSHOT.jar` 
6. Log in with the details below:
    - Username: `user`
    - Password: `password`

#### 2. Start using docker-compose in production mode
1. Clone the repository
2. Import the project as a maven project into your favourite IDE (or run maven on the terminal)
3. Build the project in production mode using `mvn clean package -Pproduction`. In production mode all UI components are packaged in jar file.
4. Start the MySQL Database and book project app using docker compose `docker-compose up --build`
5. Go to `localhost:8080`
6. Log in with the details below:
    - Username: `user`
    - Password: `password`

    
You may find lots of errors for things like the log statements, or the entities not having constructors. You can find instructions on how to fix this for IntelliJ and Eclipse in our [troubleshooting wiki page](https://github.com/knjk04/book-project/wiki/Troubleshooting). Other common errors and solutions are also detailed in the troubleshooting page.

### Access database

To access the MySQL database when docker-compose is running:

1. Go to `http://localhost:8081/`
2. Log in with the settings below.
    - User Name: `root`
    - Password: `rootpassword`
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

[Mockito](https://github.com/mockito/mockito) - [MIT License](http://www.opensource.org/licenses/mit-license.php)

[Java Hamcrest](https://github.com/hamcrest/JavaHamcrest) - [BSD-3 clause](http://opensource.org/licenses/BSD-3-Clause)

[zxcvbn4j](https://github.com/nulab/zxcvbn4j) - [MIT License](https://opensource.org/licenses/mit-license.php)

[paper-toggle-button](https://www.webcomponents.org/element/@polymer/paper-toggle-button) - [BSD-3 clause](https://spdx.org/licenses/BSD-3-Clause)

[jackson](https://github.com/FasterXML/jackson) - [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0)
