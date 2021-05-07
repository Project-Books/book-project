  <p align="center">
	<img src="/media/banner/book_project_newlogo_2x.png" alt="Logo"/>
  </p>

<p align="center">
	
   <a href="https://dev.azure.com/project-books/Book%20Project/_build/latest?definitionId=2&branchName=0.2.0">
    <img src="https://dev.azure.com/project-books/Book%20Project/_apis/build/status/Project-Books.book-project?branchName=0.2.0" alt="Build Status"/>
  </a>

  <a href="https://codecov.io/gh/Project-Books/book-project">
    <img src="https://codecov.io/gh/Project-Books/book-project/branch/master/graph/badge.svg" alt="Code coverage"/>
  </a>
	
  <a href="https://join.slack.com/t/teambookproject/shared_invite/zt-punc8os7-Iz9PTCAkYcO_0S~XwtO5_A">
    <img src="https://img.shields.io/badge/slack-teambookproject-4A154B?logo=slack" alt="Slack" />
  </a>
	
  <a href="https://app.codacy.com/manual/knjk04/book-project?utm_source=github.com&utm_medium=referral&utm_content=knjk04/book-project&utm_campaign=Badge_Grade_Dashboard">
    <img src="https://api.codacy.com/project/badge/Grade/595ed2c299d7429e9938894c385b9cab" alt="Code quality" />
  </a>
</p>

Book tracker web app made with Spring Boot and React (Typescript).

![image](https://user-images.githubusercontent.com/11173328/112493885-739b0d80-8d7a-11eb-85a1-b4c500dc61ab.png)

*The image above is from our mockup designs, so this may look slightly different to the app. If major changes are made, we will upload a new image.*

## Setup

Prerequisites:
- JDK 11
- Node >= 10 for React
- MySQL 8.0.* or (better) Docker
  - Windows or macOS: install Docker Desktop
  - Linux: install Docker Engine and Docker Compose

As Docker is our recommended approach, our instructions will assume you're following this route.

First, clone the repository.
 
### Running the frontend

1. Install yarn: `npm install -g yarn`
2. Navigate to the `frontend/` directory
3. Run `yarn install` to install the dependencies
4. Run `yarn start` to start the development server
5. Open `localhost:3000`

### Running the backend

1. Import the `backend` directory as a Maven project into your favourite IDE (or run Maven on the terminal)
2. Start Docker engine (Linux) or Docker desktop (Windows or macOS)
  
Next, follow one of the approaches below:

#### 1. Start locally with MySQL and phpMyAdmin running in docker

3. Build the project in the `backend/` directory using `./mvnw clean install` (Unix) or `mvnw.cmd clean install` (Windows)
4. Start the MySQL database using `docker-compose up -d mysql phpmyadmin`
    - May need to add `sudo` to this command
5. Start the application using `java -jar target/book-project-0.2.0.jar` 
    
#### 2. Start locally in your IDE

3. Start the MySQL database using `docker-compose up -d mysql phpmyadmin`
    - May need to add `sudo` to this command
    - phpmyadmin is optional
4. Run the project from your IDE

### Fixing Lombok errors

You may find lots of errors for things like the log statements, or the entities not having constructors. 
You can find instructions on fixing this for IntelliJ and Eclipse in our [troubleshooting wiki page](https://github.com/knjk04/book-project/wiki/Troubleshooting). 
Other common errors and solutions are also on the troubleshooting page.

### Access database

To access the MySQL database when docker-compose is running:

1. Go to `http://localhost:8081/`
2. Log in with the details below:
    - Username: `root`
    - Password: `rootpassword`
3. Click on connect

Alternatively, you can access the database inside [IntelliJ Ultimate](https://github.com/Project-Books/book-project/wiki/Connect-to-database-inside-IntelliJ-Ultimate).

## Contributing

If you wish to contribute (thanks!), please first see the [contributing document](https://github.com/knjk04/book-project/blob/master/CONTRIBUTING.md).

### Help

If you need help with anything, we'll be happy to help you over a [GitHub Q&A discussion](https://github.com/Project-Books/book-project/discussions/categories/q-a).

## Further information

For more information, such as a roadmap and the project's underlying principles, see the [Book Project wiki](https://github.com/knjk04/book-project/wiki).

To see a list of the open-source software we use, refer to our [Acknowledgements file](https://github.com/Project-Books/book-project/blob/master/ACKNOWLEDGEMENTS.md)
