  <p align="center">
	<img src="/media/banner/book_project_newlogo_2x.png" alt="Logo"/>
  </p>

<p align="center">
  <a href="https://travis-ci.com/github/Project-Books/book-project">
    <img src="https://travis-ci.com/Project-Books/book-project.svg?branch=master" alt="Build Status"/>
  </a>
  <a href="https://codecov.io/gh/Project-Books/book-project">
    <img src="https://codecov.io/gh/Project-Books/book-project/branch/master/graph/badge.svg" alt="Code coverage"/>
  </a>
  <a href="https://join.slack.com/t/teambookproject/shared_invite/zt-kss928q8-zT73FmmlV6HmXE1rhourbw">
    <img src="https://img.shields.io/badge/chat%20on-slack-%233f0e40" alt="Slack" />
  </a>
  <a href="https://app.codacy.com/manual/knjk04/book-project?utm_source=github.com&utm_medium=referral&utm_content=knjk04/book-project&utm_campaign=Badge_Grade_Dashboard">
    <img src="https://api.codacy.com/project/badge/Grade/595ed2c299d7429e9938894c385b9cab" alt="Code quality" />
  </a>
  <a href="https://sonarcloud.io/dashboard?id=com.karankumar%3Abook-project">
     <img src="https://sonarcloud.io/api/project_badges/measure?project=com.karankumar%3Abook-project&metric=alert_status" alt="SonarCloud quality gate status" />
  </a>
</p>

Book tracker web app made using Spring Boot and Vaadin 14 (only the free components will be used).

See a [live demo](http://bookprojectv010-env.eba-22zuiphf.eu-west-2.elasticbeanstalk.com/login). Instead of creating an
account, you can use the test credentials shown in the [Access site](https://github.com/Project-Books/book-project#access-site) section.
This is only for demonstration purposes, so please do not rely on any saved data persisting.

*Features:*
- Add books that you have read to a 'to read', 'currently reading', 'read' or 'did not finish' shelf (or a custom shelf)
- A rating scale from 0-10 that that goes up in steps of 0.5
- Track your progress towards a reading goal: the number of books or pages you hope to read by the end of the year
- Statistics about your reading habits
- Export your saved data to JSON
- User registration and accounts
- Import books from Goodreads
- And much more!

*Coming soon:*
- Import books from LibraryThing
- Search from a catalogue of books
- 2-factor authentication
- Magic links
- And much more!

<p align="center">
    <img src="/media/docs/readme/book_form.png" alt="New book form"/>
</p>

![Books in shelf](/media/docs/readme/books_in_shelf.png)
        
![Reading goal](/media/docs/readme/reading_goal.png)

<p align="center">
    <img src="/media/docs/readme/statistics.png" alt="Reading statistics"/>
</p>


*The images above may look slightly different to the app. If major changes are made, new images will be uploaded to 
reflect this.*

## Getting Started

Prerequisites:
- JDK 11 (the app currently won't work on later versions due to an issue with one of the libraries -- Karibu testing -- not being compatible with later versions) 
- If you don't have Node.js installed globally, it is not needed as Vaadin will install it automatically
  - If you do have Node.js installed, please ensure it is at least version 10.0
- MySQL 8.0.* or (better) Docker
  - Windows or macOS: install Docker Desktop
  - Linux: install Docker Engine and Docker Compose
 
### Running the app

1. Clone the repository
2. Import the project as a maven project into your favourite IDE (or run maven on the terminal)
3. Start Docker engine (Linux) or Docker desktop (Windows or macOS)
  
Then, if you want to use Docker, follow one of the approaches below:

#### 1. Start locally with only MySQL running in docker

4. Build the project at the root using `./mvnw clean install` (Unix) or `mvnw.cmd clean install` (Windows)
5. Start the MySQL database using `docker-compose up -d mysql phpmyadmin`
    - May need to add `sudo` to this command on Unix
6. Start the application using `java -jar target/book-project-0.0.1-SNAPSHOT.jar` 

#### 2. Start using docker-compose in production mode

4. At the root of the project, build the project in production mode using one of the following commands. In production mode all UI components are packaged in a jar file.
    - `./mvnw clean package -Pproduction` (Unix), or 
    - `mvnw.cmd clean package -Pproduction` (Windows)
5. Start the MySQL Database and book project app using `docker-compose up --build`
    - May need to add `sudo` to this command on Unix
    
#### 3. Start locally with Vaadin live reload (the tests do not run)

We recommended this approach if you need to work on the frontend with Vaadin. This approach allows you to use 
Vaadin Live reload which prevents you from needing to re-run the server every time or manually refresh your browser 
(i.e. it's a lot quicker). 

4. Start the MySQL database using `docker-compose up -d mysql phpmyadmin`
    - May need to add `sudo` to this command on Unix
5. Run the project from your IDE or with Maven:
    - `./mvnw spring-boot:run` on Unix
    - `mvnw.cmd spring-boot:run` on Windows
   
If you're running the app from your IDE, after making a Vaadin-related change, build the app from your IDE after making
any changes.

### Access site

After following the instructions for running the app above, go to `localhost:8080`. Then, log in with the details below:
- Username: `user`
- Password: `password`

### Fixing Lombok errors

You may find lots of errors for things like the log statements, or the entities not having constructors. 
You can find instructions on how to fix this for IntelliJ and Eclipse in our [troubleshooting wiki page](https://github.com/knjk04/book-project/wiki/Troubleshooting). 
Other common errors and solutions are also in the troubleshooting page.

### Access database

To access the MySQL database when docker-compose is running:

1. Go to `http://localhost:8081/`
2. Log in with the settings below.
    - User Name: `root`
    - Password: `rootpassword`
3. Click on connect

## Contributing

If you wish to contribute (thanks!), please first see the [contributing document](https://github.com/knjk04/book-project/blob/master/CONTRIBUTING.md).

### Help

If you need help with anything, we'll be happy to help you in our [Slack workspace](https://join.slack.com/t/teambookproject/shared_invite/zt-kss928q8-zT73FmmlV6HmXE1rhourbw). Alternatively, you can start a new discussion on our [GitHub Q&A](https://github.com/Project-Books/book-project/discussions/categories/q-a).

## Further information

For more information, such as a roadmap and the underlying principles of the project, see the [Book Project wiki](https://github.com/knjk04/book-project/wiki).

To see the open source software we use, refer to our [Acknowledgements file](https://github.com/Project-Books/book-project/blob/master/ACKNOWLEDGEMENTS.md)
