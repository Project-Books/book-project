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

  <a href="hhttps://opencollective.com/book-project">
    <img src="https://img.shields.io/badge/open%20collective-donate-2ecc71" alt="Slack" />
  </a>
</p>

Book tracker web app made with Spring Boot and React (Typescript).

![image](https://user-images.githubusercontent.com/11173328/112493885-739b0d80-8d7a-11eb-85a1-b4c500dc61ab.png)

*The image above is from our mockup designs, so this may look slightly different to the app. If major changes are made, we will upload a new image.*

# Getting started locally

Prerequisites:
- Docker with [Buildkit enabled](https://docs.docker.com/develop/develop-images/build_enhancements/#to-enable-buildkit-builds)
  - Windows or macOS: install [Docker Desktop](https://www.docker.com/products/docker-desktop)
  - Linux: install [Docker Engine](https://docs.docker.com/engine/) and [Docker Compose](https://docs.docker.com/compose/)

First, clone the repository (you'll need to fork first and then clone your fork if you're contributing). 

Next, start Docker engine (Linux) or Docker desktop (macOS or Windows). If you're using an Apple silicon chip, you'll need to uncomment [this line](https://github.com/Project-Books/book-project/blob/0.2.0/backend/docker-compose.yml#L6). 
 
## Running the frontend

1. Navigate to the [frontend/](https://github.com/Project-Books/book-project/tree/main/frontend) directory in a terminal
1. Run `docker-compose up --build` to build and start the frontend container
1. Open `localhost:3000` once the development server has started
1. When finished, run `docker-compose down` to stop and remove the frontend container

### Run Books API (optional)

You may want to also want to run our [Books API](https://github.com/Project-Books/books-api) to avoid seeing an error on the search page.

## Running the backend and database

1. Build the Docker images in the `backend/` directory using `docker-compose build`
1. Start the backend and database containers using `docker-compose up` 
1. When finished, run `docker-compose down` to stop and remove the containers

If you find this runs slowly (takes up too much of your computer's resources), you can try:
- Start the PostgreSQL database inside a container (`docker-compose up db`) but run the backend as a Maven app in your IDE. You will need to uncomment and comment the relevant lines in application-dev.properties [INSERT LINK]
   - note: if asking us for help, please ensure you use Docker
- Using something more lightweight than an IDE like IntelliJ, such as VS Code

> Note for backend contributors: Please ensure you run the unit tests manually (we supply the `-DskipTests` flag with Docker by default for convenience).


### Access database (optional)

Using your favourite SQL client, use the following settings:
- Host: `localhost`
- Port: `5433`
- User: `dbuser`
- Password: `dbpassword`
- Database name: `book_project_db`

For example, in DataGrip or IntelliJ Ultimate:

![image](https://user-images.githubusercontent.com/11173328/153755219-051627c5-f052-4db9-a223-091acb4b2e76.png)

## Log in with our test user

When running the frontend and backend, or only the backend, you can use the following test user:
- Email address: `user@user.user`
- Password: `password`

Note: If you're running the backend, you will need a JWT token for subsequent requests after logging in or creating an account; see our [connecting to the backend](https://github.com/Project-Books/book-project/wiki/Connecting-to-the-backend-via-Postman) wiki page.

# Contributing

If you wish to contribute (thanks!), please first see the [contributing document](https://github.com/knjk04/book-project/blob/master/CONTRIBUTING.md). 

We work hard to make our project approachable to everyone -- from those new to open-source looking to make their first contribution to seasoned developers.

## Backend: fixing Lombok errors

You may find lots of errors for things like the log statements, or the entities not having constructors. 
You can find instructions on fixing this for IntelliJ and Eclipse in our [troubleshooting wiki page](https://github.com/knjk04/book-project/wiki/Troubleshooting). 
Other common errors and solutions are also on the troubleshooting page.

## Help

If you need help with anything, we'll be happy to help you over a [GitHub Q&A discussion](https://github.com/Project-Books/book-project/discussions/categories/q-a). Alternatively, feel free to chat with us on the [#book-project](https://teambookproject.slack.com/archives/C01AGDC5X1S) channel on our [Slack workspace](https://teambookproject.slack.com/join/shared_invite/zt-punc8os7-Iz9PTCAkYcO_0S~XwtO5_A#/shared-invite/email).

When asking for help on Slack, we always recommend asking on our [#book-project](https://teambookproject.slack.com/archives/C01AGDC5X1S) channel, rather than contacting a maintainer directly. This is so that others can offer help and the answer may help someone else.

# Further information

For more information, such as a roadmap and the project's underlying principles, see the [Book Project wiki](https://github.com/knjk04/book-project/wiki).

To see a list of the open-source software we use, refer to our [Acknowledgements file](https://github.com/Project-Books/book-project/blob/master/ACKNOWLEDGEMENTS.md)

# Donations
<p align="center">	

  <a href="hhttps://opencollective.com/book-project">
    <img src="https://img.shields.io/badge/open%20collective-donate-2ecc71" alt="Slack" />
  </a>
</p>

If you are able and willing to support us financially, it will go a long way to help us achieve our goals and become more sustainable. We hate to ask for money, but running cloud server costs are not free.

We currently only accept donations through Open Collective.
