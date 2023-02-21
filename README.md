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
  - Linux: install [Docker Engine](https://docs.docker.com/engine/) and [Docker Compose](https://docs.docker.com/compose/). Follow the [post installation guide](https://docs.docker.com/engine/install/linux-postinstall/) to add your user to the `docker` group

1. Clone the repository (if you're contributing, you'll need to first fork the repository and then clone your fork)
1. Start Docker engine (Linux) or Docker desktop (macOS or Windows). 
   - If you're using an Apple silicon chip (e.g. M1), you'll need to uncomment [this line](https://github.com/Project-Books/book-project/blob/0.2.0/backend/docker-compose.yml#L6). 
1. In the root of the project, run `docker-compose build` to build the database, backend and frontend services
1. Run `docker-compose --env-file .env up` to start the containers
1. Once the development server has started (you'll get notified in the output of `docker-compose up`), go to `localhost:3000` in your web browser to access the frontend
1. When finished, run `docker-compose down` to stop and remove the containers

You may want to also want to run our [Books API](https://github.com/Project-Books/books-api) to avoid seeing an error on the search page on the frontend.

> Note for backend contributors: Please ensure you run the unit tests manually (we supply the `-DskipTests` flag with Docker by default for convenience).

## Log in with our test user

When running the frontend and backend, or only the backend, you can use the following test user:
- Email address: `user@user.user`
- Password: `password`

Note: If you're running the backend, you will need a JWT token for subsequent requests after logging in or creating an account; see our [connecting to the backend](https://project-books.github.io/development/how-to/backend-postman/) wiki page.
 
## Access database (optional)

Using your favourite SQL client, use the following settings:
- Host: `localhost`
- Port: `5433`
- User: `dbuser`
- Password: `dbpassword`
- Database name: `book_project_db`

For example, in DataGrip or IntelliJ Ultimate:

![image](https://user-images.githubusercontent.com/11173328/153755219-051627c5-f052-4db9-a223-091acb4b2e76.png)

# Contributing

If you wish to contribute (thanks!), please first see the [contributing document](https://github.com/knjk04/book-project/blob/master/CONTRIBUTING.md). 

We work hard to make our project approachable to everyone -- from those new to open-source looking to make their first contribution to seasoned developers.

## Backend: fixing Lombok errors

You may find lots of errors for things like the log statements, or the entities not having constructors. 
You can find instructions on fixing this for IntelliJ and Eclipse in our [troubleshooting page](https://project-books.github.io/development/how-to/troubleshoot/). 
Other common errors and solutions are also on the troubleshooting page.

## Docker running slowly: Windows users

If you are notice that the Vmmem process is consuming too much of your CPU and RAM, you can adjust the maximum limit that Docker can use.

![image](https://user-images.githubusercontent.com/11173328/154207932-d7ffaf70-0d1a-4362-bba8-ca23cb147692.png)

If using the WSL 2 backend (see the image above: go to Docker Desktop > Settings > Resources), create a `.wslconfig` file at the root of your user folder: `C:\Users\<your-username>`:

```
[wsl2]
memory=4GB   # Limits VM memory in WSL 2 up to 4GB
processors=2# Makes the WSL 2 VM use two virtual processors
```

Update the values as appropriate for your system. See the [documentation](https://docs.microsoft.com/en-us/windows/wsl/wsl-config#configure-global-options-with-wslconfig) for more information

## Help

If you need help with anything, we'll be happy to help you over a [GitHub Q&A discussion](https://github.com/Project-Books/book-project/discussions/categories/q-a). Alternatively, feel free to chat with us on the [#book-project](https://teambookproject.slack.com/archives/C01AGDC5X1S) channel on our [Slack workspace](https://teambookproject.slack.com/join/shared_invite/zt-punc8os7-Iz9PTCAkYcO_0S~XwtO5_A#/shared-invite/email).

When asking for help on Slack, we always recommend asking on our [#book-project](https://teambookproject.slack.com/archives/C01AGDC5X1S) channel, rather than contacting a maintainer directly. This is so that others can offer help and the answer may help someone else.

# Further information

For more information, such as a roadmap and the project's underlying principles, see our [documentation site](https://project-books.github.io).

To see a list of the open-source software we use, refer to our [Acknowledgements file](https://github.com/Project-Books/book-project/blob/master/ACKNOWLEDGEMENTS.md)

# Donations
<p align="center">	

  <a href="hhttps://opencollective.com/book-project">
    <img src="https://img.shields.io/badge/open%20collective-donate-2ecc71" alt="Slack" />
  </a>
</p>

If you are able and willing to support us financially, it will go a long way to help us achieve our goals and become more sustainable. We hate to ask for money, but running cloud server costs are not free.

We currently only accept donations through Open Collective.
