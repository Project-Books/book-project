  <p align="center">
	<img src="/media/banner/book_project_newlogo_2x.png" alt="Logo"/>
  </p>

<p align="center">	
  <a href="https://github.com/Project-Books/book-project/actions/workflows/build.yml">
    <img src="https://github.com/Project-Books/book-project/actions/workflows/build.yml/badge.svg" alt="Build Status"/>
  </a>
	
  <a href="https://sonarcloud.io/dashboard?id=Project-Books_book-project">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=Project-Books_book-project&metric=coverage" alt="Code coverage" />
  </a>	
	
  <a href="https://join.slack.com/t/teambookproject/shared_invite/zt-punc8os7-Iz9PTCAkYcO_0S~XwtO5_A">
    <img src="https://img.shields.io/badge/slack-teambookproject-4A154B?logo=slack" alt="Slack" />
  </a>
</p>

Book tracker web app made with Spring Boot and React (Typescript).

![image](https://user-images.githubusercontent.com/11173328/112493885-739b0d80-8d7a-11eb-85a1-b4c500dc61ab.png)

*The image above is from our mockup designs, so this may look slightly different to the app. If major changes are made, we will upload a new image.*

# Getting started locally

Prerequisites:
- JDK 11
- Docker
  - Windows or macOS: install Docker Desktop
  - Linux: install Docker Engine and Docker Compose

First, clone the repository (you'll need to fork first and then clone your fork if you're contributing). 

Next, start Docker engine (Linux) or Docker desktop (macOS or Windows). If you're using an Apple silicon chip, you'll need to uncomment [this line](https://github.com/Project-Books/book-project/blob/0.2.0/backend/docker-compose.yml#L6). 

 
## Running the frontend

1. Navigate to the `frontend/` directory
1. Run `docker-compose up` to start the frontend container
   - If you encounter any errors, you can try rebuilding the image with `docker-compose up -d --build`
1. Open `localhost:3000` once the development server has started

## Running the database

1. Start the MySQL database using `docker-compose up -d mysql phpmyadmin`
   - May need to add sudo to this command
   - phpmyadmin is optional

## Running the backend

The backend depends on the database, so please ensure you have started our MySQL database with the instructions in the section above.

You can either run the `BookProjectApplication.java` main class from your IDE (import as a Maven project) or follow the approach below:

1. Build the project in the `backend/` directory using `./mvnw clean install` (Unix) or `mvnw.cmd clean install` (Windows)
1. Start the application using `java -jar target/book-project-0.2.0.jar` 

### Run Books API (optional)

If you are running the frontend, you may also want to run the [Books API](https://github.com/Project-Books/books-api). This is where we fetch the data from our own catalogue of books.

### Fixing Lombok errors

You may find lots of errors for things like the log statements, or the entities not having constructors. 
You can find instructions on fixing this for IntelliJ and Eclipse in our [troubleshooting wiki page](https://github.com/knjk04/book-project/wiki/Troubleshooting). 
Other common errors and solutions are also on the troubleshooting page.

### Test user

When running the frontend and backend, or only the backend, you can use the following test user:
- Email address: `user@user.user`
- Password: `password`

If you're running the backend, you will need a JWT token for subsequent requests after logging in or creating an account; see our [connecting to the backend](https://github.com/Project-Books/book-project/wiki/Connecting-to-the-backend-via-Postman) wiki page.

### Access database

To access the MySQL database when docker-compose is running with the phpmyadmin container:

1. Go to `http://localhost:8081/`
2. Log in with the details below:
    - Username: `root`
    - Password: `rootpassword`
3. Click on connect

Alternatively, you can access the database inside [IntelliJ Ultimate](https://project-books.github.io/docs/development/how-to/database-intellij/).

# Contributing

If you wish to contribute (thanks!), please first see the [contributing document](https://github.com/knjk04/book-project/blob/master/CONTRIBUTING.md).

## Help

If you need help with anything, we'll be happy to help you over a [GitHub Q&A discussion](https://github.com/Project-Books/book-project/discussions/categories/q-a). Alternatively, feel free to chat with us on the #book-project channel on our [Slack workspace](https://teambookproject.slack.com/join/shared_invite/zt-punc8os7-Iz9PTCAkYcO_0S~XwtO5_A#/shared-invite/email).

# Further information

For more information, such as a roadmap and the project's underlying principles, see the [Book Project wiki](https://github.com/knjk04/book-project/wiki).

To see a list of the open-source software we use, refer to our [Acknowledgements file](https://github.com/Project-Books/book-project/blob/master/ACKNOWLEDGEMENTS.md)
