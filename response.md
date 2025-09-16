# Response
> The Current content is an **example template**; please edit it to fit your style and content.
## A. Required Information
### A.1. Requirement Completion Rate
- [x] List all pharmacies open at a specific time and on a day of the week if requested.
  - Implemented at `GET /pharmacies` API.
- [x] List all masks sold by a given pharmacy, sorted by mask name or price.
  - Implemented at `GET /pharmacies/masks` API.
- [x] List all pharmacies with more or less than x mask products within a price range.
  - Implemented at `GET /pharmacies/filter` API.
- [x] The top x users by total transaction amount of masks within a date range.
  - Implemented at `GET /users/top` API.
- [x] The total number of masks and dollar value of transactions within a date range.
  - Implemented at `GET /purchaseHistory/summary` API.
- [x] Search for pharmacies or masks by name, ranked by relevance to the search term.
  - Implemented at `GET /pharmacies/search` API.
- [x] Process a user purchases a mask from a pharmacy, and handle all relevant data changes in an atomic transaction.
  - Implemented at `GET /masks/purchase` API.
### A.2. API Document
> Please describe how to use the API in the API documentation. You can edit by any format (e.g., Markdown or OpenAPI) or free tools (e.g., [hackMD](https://hackmd.io/), [postman](https://www.postman.com/), [google docs](https://docs.google.com/document/u/0/), or  [swagger](https://swagger.io/specification/)).

Once the application has started, navigate to `GET /swagger-ui/index.html` to access the Swagger UI.

### A.3. Import Data Commands
When the application starts, the initial data will be automatically imported into the in-memory H2 database.  
The data files are located in the project resources and are loaded by a `CommandLineRunner` implementation.

No manual command execution is required.

## B. Bonus Information

>  If you completed the bonus requirements, please fill in your task below.
### B.1. Dockerized

On the local machine, please follow the commands below to build it.

```bash
#build image
$ ./gradlew bootBuildImage

#push to docker hub
$ docker tag phantom_mask:0.0.1-SNAPSHOT gamer2925666/phantom_mask:0.0.1-SNAPSHOT
$ docker push gamer2925666/phantom_mask:0.0.1-SNAPSHOT 

#pull image
$ docker pull gamer2925666/phantom_mask:0.0.1-SNAPSHOT

#Use `docker run` to start the container and map port 8080 to the host machine.
$ docker run -d -p 8080:8080  gamer2925666/phantom_mask:0.0.1-SNAPSHOT
```

### B.2. Demo Site Url

The demo site is ready on [my azure app service site](https://phantom-mask-exbfdhezbqekdpev.canadacentral-01.azurewebsites.net/swagger-ui/index.html
) you can try any APIs on this demo site.
## C. Other Information

### C.1. ERD

![ERD](./ERD.png)
- --
