## This is a Java Spring Boot application, which allows to manage images and slideshows

### Core features:
1. Application works on a reactive stack: **WebFlux + R2DBC**
2. Database is **PostgreSQL**
3. Database migration is done using **Liquibase**
4. Messaging is done using **Kafka**
5. Application can be run in **Docker** with all dependant services

### API
1. **Add image:** POST api/images/addImage
   
   Returns a newly created images and HTTP status 201 Created.
   
   **Validations:**
   - name, url and playDuration **must** be specified, otherwise corresponding error will be returned with HTTP status 400 Bad Request
   - url must contain an actual image, otherwise corresponding error will be returned with HTTP status 400 Bad Request
  
   Example of request body:
   ```
   {
    "name" : "cat1",
    "url" : "https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_2x3.jpg",
    "playDuration" : "PT10S"
   }
   ```
   Example of response:
   ```
   {
    "id": 1,
    "name": "cat1",
    "url": "https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_2x3.jpg",
    "playDuration": "PT10S",
    "additionDateTime": "2025-01-12T16:46:40.815782628"
   }
   ```
3. **Delete image by id:** DELETE api/deleteImage/{id}
   
   Returns empty response with HTTP status 200 Ok if image was deleted.
   
   **Validations:**
   - Image with specified id **must exist**, otherwise corresponding error will be returned with HTTP status 404 Not Found

5. **Add slideshow:** POST /api/addSlideshow
   
   Returns newly creaeted slideshow and HTTP status 201 Created.
   
   **Validations:**
   - images **must** be specified, otherwise corresponding error will be returned with HTTP status 400 Bad Request
   - images.id **must** contain the id of an existing image, otherwise corresponding error will be returned with HTTP status 400 Bad Request
   
   Example of request body:
   ```
   {
    "name" : "slideshow1",
    "images" : [
        {
            "id" : "1",
            "playDuration" : "PT5S"
        }
    ]
   }
   ```
   Example of response:
   ```
   {
    "id": 1,
    "name": "slideshow1",
    "images": [
        {
            "id": 1,
            "name": "cat1",
            "url": "https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_2x3.jpg",
            "playDuration": "PT5S",
            "additionDateTime": "2025-01-12T16:46:40.815783"
        }
    ]
   }
   ```
6. **Delete slideshow by id:** DELETE /api/deleteSlideshow/{id}

   Returns empty response with HTTP status 200 Ok if slideshow was deleted.
   
   **Validations:**
   - Slideshow with specified id **must exist**, otherwise corresponding error will be returned with HTTP status 404 Not Found

7. **Search images by url keyword or duration:** GET api/images/search

   Optional request params: urlKeyword=url, playDuration=PT1S

   Returns list of images according to filtering params with HTTP status 200 Ok. Only one can be specified, either **urlKeyword** or **playDuration**. If both specified, **playDuration** will be ignored.

   Example of request: GET /api/images/search?playDuration=PT5S

   Example of response:
   ```
   [
    {
        "id": 1,
        "name": "cat1",
        "url": "https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_2x3.jpg",
        "playDuration": "PT5S",
        "additionDateTime": "2025-01-12T16:46:40.815783",
        "slideshows": [
            {
                "id": 1,
                "name": "slideshow1"
            }
        ]
    }
   ]
   ```
6. **Get slideshow with images, ordered by addition date:** GET /api/slideshow/{slideshowId}/slideshowOrder

   Returns slideshows with ordered images and HTTP status 200 Ok

   **Validations:**
   - Slideshow with specified id **must exist**, otherwise corresponding error will be returned with HTTP status 404 Not Found

   Example of response:
   ```
   {
    "id": 1,
    "name": "slideshow1",
    "images": [
        {
            "id": 1,
            "name": "cat1",
            "url": "https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_2x3.jpg",
            "playDuration": "PT5S",
            "additionDateTime": "2025-01-12T16:46:40.815783"
        }
    ]
   }
   ```
7. **Record a Kafka event when an image of a slideshow is replaced by the next one:** POST /api/slideshow/{slideshowId}/proof-of-play/{imageId}

   Returns an empty response with HTTP status 200 Ok

### Implementation details

App can be started by running ```docker-compose up -d ```. App will be exposed to port 8080, Kafka server - to port 29092 

Kafka topics are created during initialization of containers and are called **image** (holds messages about actions with images) and **slideshow** (holds messages about actions with slideshows)
