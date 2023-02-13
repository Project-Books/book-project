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

![image](https://user-images.githubusercontent.com/11173328/112493885-739b0d80-8d7a-11eb-85a1-b4c500dc61ab.png)

<p align="center">
  <i>
    This layout is based off our mockup design, which may differ from the app display. 
    Subject to change once significant modifications are made.
  </i>
</p>

---
<a name="table-of-contents"></a>
# **Table of Contents**

* [**Overview**](#overview)
   * [Description](#description)
   * [Next Steps](#next-steps)
* [**Getting started locally**](#getting-started-locally)
   * [Prerequisites](#prerequisites)
   * [Log in with our test user](#log-in-with-our-test-user)
   * [Access database (optional)](#access-database-optional)
* [**Contributing**](#contributing)
   * [Backend: Fixing Lombok Errors](#backend-fixing-lombok-errors)
   * [Docker Running Slowly: Windows](#docker-running-slowly-windows)
   * [Help](#help)
* [**Further Information**](#further-information)
* [**Donations**](#donations)

---
<a name="overview"></a>
# **Overview**

<a name="decription"></a>
## Description

  &nbsp;&nbsp;&nbsp;
  The **Book Project** is a book tracker web app, made with [***Spring Boot***](https://spring.io/) and [***React***](https://reactjs.org/), that allows its users to track books in 4 ways:  

  * books you *would like to read*
  * books you *are currently reading*
  * books you *have read*
  * books you *left unfinished*

[⏏ table-of-contents](#table-of-contents)

<a name="next-steps"></a>
## Next Steps

&nbsp;&nbsp;&nbsp;
We are looking to expand the **Book Project** to become an interactive book recommendation platform for readers.

[⏏ table-of-contents](#table-of-contents)

---
<a name="getting-started-locally"></a>
# **Getting started locally** 

<a name="prerequisites"></a>
## Prerequisites 

> ### Docker with [Buildkit](https://docs.docker.com/develop/develop-images/build_enhancements/#to-enable-buildkit-builds) enabled
> #### &nbsp;&nbsp;&nbsp; **Windows or macOS**
  > * Install [Docker Desktop](https://www.docker.com/products/docker-desktop)
> #### &nbsp;&nbsp;&nbsp; **Linux**
  > * Install [Docker Engine](https://docs.docker.com/engine/) and [Docker Compose](https://docs.docker.com/compose/)
  > * Follow the [post installation guide](https://docs.docker.com/engine/install/linux-postinstall/) to add your user to the `docker` group

1. Clone the repository.
   #### **Non-contributors**
   ```
   $ git clone https://github.com/Project-Books/book-project
   ```
   #### **Contributors**
   Fork this repository then clone your fork
   ```
   $ git clone https://github.com/YOUR-USERNAME/book-project
   ```

2. Start Docker Engine (Linux) or Docker Desktop (macOS or Windows).
   #### **Apple Silicon Chip Users**
   Uncomment [this line](https://github.com/Project-Books/book-project/blob/0.2.0/backend/docker-compose.yml#L6)

   Terminal navigation:
   ```
   $ cd PATH/TO/REPO/backend/docker-compose.yml
   ```

3. In the root of the project, run `docker-compose build` to build the database, backend and frontend services.
   ```
   $ cd PATH/TO/REPO
   $ docker-compose build
   ```
4. In the root of the project, run `docker-compose --env-file .env up` to start the containers.
   ```
   $ cd PATH/TO/REPO
   $ docker-compose --env-file .env up
   ```
5. Once development server has started (notified in the output of `docker-compose up`), to access the frontend, go to `localhost:3000` in your web browser

6. When finished, run `docker-compose down` in the root of the project to stop and remove the containers.
   ```
   $ cd PATH/TO/REPO
   $ docker-compose down
   ```

> #### **NOTES**
> * To avoid error display on search page, consider running our [Books API](https://github.com/Project-Books/books-api)
> * **Backend Contributors** 
>   * Please ensure you run the unit tests manually
>   * We supply the `-DskipTests` flag with Docker by default for convenience

[⏏ table-of-contents](#table-of-contents)

<a name="log-in-test-user"></a>
## Log in with our test user 

When running the frontend and backend, or only the backend, you can use the following test user:
- Email: `user@user.user`
- Password: `password`

> #### **NOTES**
> *   If running the backend, a JWT token will be needed for subsequent requests after logging in or creating and account
>   * Please refer to our [connecting to the backend guide](https://project-books.github.io/development/how-to/backend-postman/)

[⏏ table-of-contents](#table-of-contents)
 
<a name="access-db"></a>
## Access database (optional)

Using your favourite SQL client, use the following settings:
- Host: `localhost`
- Port: `5433`
- User: `dbuser`
- Password: `dbpassword`
- Database name: `book_project_db`

For example, in DataGrip or IntelliJ Ultimate:

![image](https://user-images.githubusercontent.com/11173328/153755219-051627c5-f052-4db9-a223-091acb4b2e76.png)

[⏏ table-of-contents](#table-of-contents)

---
<a name="contributing"></a>
# **Contributing**

If you wish to contribute (thanks!), please review our [contribution guidelines](https://project-books.github.io/development/contributing/). 

We work hard to make our project approachable to everyone -- from those new to open-source looking to make their first contribution to seasoned developers.

<a name="backend-fix-lombok-err"></a>
## Backend: Fixing Lombok Errors 

You may find lots of errors for things like the log statements, or the entities not having constructors. 
You can find instructions on fixing this for IntelliJ and Eclipse in our [troubleshooting page](https://project-books.github.io/development/how-to/troubleshoot/). 
Other common errors and solutions are also on the troubleshooting page.

[⏏ table-of-contents](#table-of-contents)

<a name="docker-runs-slow-windows"></a>
## Docker Running Slowly: Windows

If you are notice that the Vmmem process is consuming too much of your CPU and RAM, you can adjust the maximum limit that Docker can use.

![image](https://user-images.githubusercontent.com/11173328/154207932-d7ffaf70-0d1a-4362-bba8-ca23cb147692.png)

If using the WSL 2 backend (see the image above: go to Docker Desktop > Settings > Resources), create a `.wslconfig` file at the root of your user folder: `C:\Users\<your-username>`:

```
[wsl2]
memory=4GB   # Limits VM memory in WSL 2 up to 4GB
processors=2# Makes the WSL 2 VM use two virtual processors
```

Update the values as appropriate for your system. See the [documentation](https://docs.microsoft.com/en-us/windows/wsl/wsl-config#configure-global-options-with-wslconfig) for more information

<a name="help"></a>

[⏏ table-of-contents](#table-of-contents)

## Help

If you need help with anything, we'll be happy to help you over a [GitHub Q&A discussion](https://github.com/Project-Books/book-project/discussions/categories/q-a). Alternatively, feel free to chat with us on the [#book-project](https://teambookproject.slack.com/archives/C01AGDC5X1S) channel on our [Slack workspace](https://teambookproject.slack.com/join/shared_invite/zt-punc8os7-Iz9PTCAkYcO_0S~XwtO5_A#/shared-invite/email).

When asking for help on Slack, we always recommend asking on our [#book-project](https://teambookproject.slack.com/archives/C01AGDC5X1S) channel, rather than contacting a maintainer directly. This is so that others can offer help and the answer may help someone else.

[⏏ table-of-contents](#table-of-contents)

---
<a name="further-info"></a>
# **Further Information** 

For more information, such as a roadmap and the project's underlying principles, see our [documentation site](https://project-books.github.io).

To see a list of the open-source software we use, refer to our [Acknowledgements file](https://github.com/Project-Books/book-project/blob/master/ACKNOWLEDGEMENTS.md)

[⏏ table-of-contents](#table-of-contents)

---
<a name="donations"></a>
# **Donations** 
<p align="center">	

  <a href="https://opencollective.com/book-project">
    <img src="https://img.shields.io/badge/open%20collective-donate-2ecc71" alt="Slack" />
  </a>
</p>

If you are able and willing to support us financially, it will go a long way to help us achieve our goals and become more sustainable. We hate to ask for money, but running cloud server costs are not free.

We currently only accept donations through Open Collective.

[⏏ table-of-contents](#table-of-contents)