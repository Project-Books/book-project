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
	
  <a href="https://join.slack.com/t/teambookproject/shared_invite/zt-kss928q8-zT73FmmlV6HmXE1rhourbw">
    <img src="https://img.shields.io/badge/chat%20on-slack-%233f0e40" alt="Slack" />
  </a>
	
  <a href="https://app.codacy.com/manual/knjk04/book-project?utm_source=github.com&utm_medium=referral&utm_content=knjk04/book-project&utm_campaign=Badge_Grade_Dashboard">
    <img src="https://api.codacy.com/project/badge/Grade/595ed2c299d7429e9938894c385b9cab" alt="Code quality" />
  </a>
</p>

Book tracker web app made in Spring Boot. We are currently migrating from Vaadin 14 to React (with Typescript).

See a [live demo](http://bookprojectv010-env.eba-22zuiphf.eu-west-2.elasticbeanstalk.com/login). Instead of creating an
account, you can use the test credentials shown in the [Access site](https://github.com/Project-Books/book-project#access-site) section.
This is only for demonstration purposes, so please do not rely on any saved data persisting.

**The images, feature list and demo are v0.1.0. v0.2.0 will look different.**

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

1. Navigate to the `frontend/` directory
2. Run `yarn install` to install the dependencies
3. Run `yarn start` to start the development server
3. Open `localhost:3000`

### Running the backend

1. Import the `backend` directory as a Maven project into your favourite IDE (or run Maven on the terminal)
2. Start Docker engine (Linux) or Docker desktop (Windows or macOS)
  
Next, follow one of the approaches below:

#### 1. Start locally with only MySQL running in docker

3. Build the project in the `backend/` directory using `./mvnw clean install` (Unix) or `mvnw.cmd clean install` (Windows)
4. Start the MySQL database using `docker-compose up -d mysql phpmyadmin`
    - May need to add `sudo` to this command on Unix
5. Start the application using `java -jar target/book-project-0.1.0.jar` 

#### 2. Start using docker-compose in production mode

3. In the `backend/` directory, build the project in production mode using one of the following commands
    - `./mvnw clean package -Pproduction` (Unix), or 
    - `mvnw.cmd clean package -Pproduction` (Windows)
4. Start the MySQL Database and book project app using `docker-compose up --build`
    - May need to add `sudo` to this command on Unix
    
#### 3. Start locally in your IDE

3. Start the MySQL database using `docker-compose up -d mysql phpmyadmin`
    - May need to add `sudo` to this command on Unix
4. Run the project from your IDE

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

If you need help with anything, we'll be happy to help you in our [Slack workspace](https://join.slack.com/t/teambookproject/shared_invite/zt-kss928q8-zT73FmmlV6HmXE1rhourbw).

## Further information

For more information, such as a roadmap and the underlying principles of the project, see the [Book Project wiki](https://github.com/knjk04/book-project/wiki).

To see the open source software we use, refer to our [Acknowledgements file](https://github.com/Project-Books/book-project/blob/master/ACKNOWLEDGEMENTS.md)
