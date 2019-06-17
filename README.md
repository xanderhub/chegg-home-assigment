# chegg-home-assigment
Q-Fetcher - service for gathering students questions from various sources

## Description
The service fetches data from three different file types: *JSON*, *CSV* and *IMAGE* (.png, .jpg etc) and loads it into <br /> 
__H2 SQL__ database (I used in-memory mode for simplicity). This approach allows basic pagination and sorting by <br /> 
using __Spring Data__ abstraction (see [PagingAndSortingRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html)).

The data (students questions) is stored in `questions` table as following:

![DataFromH2](https://user-images.githubusercontent.com/33380175/59254944-d40a9a80-8c39-11e9-86c4-499e7660c622.PNG)

<br /> 
<br /> 

## Server

### Technologies / libraries  used in this project:
* Java 8
* Spring Boot + Spring Data
* H2 database (in memory mode)
* Gson
* OpenCSV
* Google Cloud Vision API

### Running the server
To run the server, cd into the `chegg-home-assigment` folder and run:<br /> 
```
./mvnw spring-boot:run
```
Here are some examples of HTTP GET requests that you can run:<br /> 

`localhost:8080/api/questions?page=0&size=10`     -     retrieves first 10 questions (all sources)<br />
`localhost:8080/api/questions?source=CSV&page=0&size=3`     -     retrieves first 3 questions extracted from CSV file only<br />

Example of response:
```
{
    "questions": [
        {
            "value": "To be or not to be?",
            "source": "JSON"
        },
        {
            "value": "What's the gist, physicist?",
            "source": "JSON"
        },
        {
            "value": " Among 150 math students 42 have taken Discrete Math 32 have taken History of Math. There are 12 have taken both. (a.) How many students have take only Discrete Math; but not History of Math? (b.) How many students have taken only History of Math; but not Discrete Math?",
            "source": "CSV"
        }
    ],
    "pagination": {
        "next": "/api/questions?page=1&size=3"
    }
}
```

Example of GET request logs:

![Logs2](https://user-images.githubusercontent.com/33380175/59260788-eb02ba00-8c44-11e9-83df-b4d59f66a8ca.PNG)

While running the app you can log in to H2 Console and see the data loaded (no need for a password):
[http://localhost:8080/h2-console](http://localhost:8080/h2-console)

### Data Loaders
In this project there are three implpementation classes for extracting and loading questions data (each per source):<br />
`CsvQuestionLoaderImpl`, `JsonQuestionLoaderImpl`, `ImageQuestionLoaderImpl`<br />

Since json loader and csv loader classes are using readers (JsonReader and CSVReader) they can parse and load really large files.
This because there is no need to load the whole file into memory but just read it line by line or node by node:

```
JsonReader reader = new JsonReader(new StringReader(...);
                        while(reader.hasNext()) {
                            Question question = gson.fromJson(reader, Question.class);
                            ...
                        }
                        reader.close();
```

Each loader class shares instanse of `AsyncHttpClient` provided by `QFetcherBootsrap` class on project startup.
This allows each loader to asynchronious fetch its data and load it to repository (`QuestionRepository`) injected by Spring framework

Here are the logs that can be seen on project startup:

![Logs](https://user-images.githubusercontent.com/33380175/59260577-8c3d4080-8c44-11e9-95a9-88dd24a6355a.PNG)

### Question entity
The `Question` class configured as persistent entity of JPA in order to mark it as DAO - data access object to `questions` table. <br />
`source` field is indexed in terms of performance. Fetching questions by source is being quicker with index:

```
@Entity
@Table(name = "questions", indexes = {@Index(name = "i_source", columnList = "source")})
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Expose
    @Type(type = "text")
    private String text;

    @Enumerated(value = EnumType.STRING)
    private DataSource source;
    .
    .
    .
}
```

Here is how the table looks in H2 database after index definition:<br />
![H2db_questions_table](https://user-images.githubusercontent.com/33380175/59262525-0d4a0700-8c48-11e9-899d-1465af0da963.PNG)

`Question` entity has its DTO (data transfer object) representation defioned in `QuestionDTO` class.
This just wraps the `Question` class and represents it according to API defined in [assigment task](https://bitbucket.org/cheggil/fullstack-home-assignment/src/master/)

<br /> 
<br /> 
<br /> 

## Client

The following screenshot describes the client side app.<br /> 
Questions organized in table with __source__ and __question__ columns. <br /> 
There is an option to filter questions by source using dropdown list.

![Client1](https://user-images.githubusercontent.com/33380175/59617318-c9b33980-912e-11e9-972f-9669b00bf1d4.PNG)

The __Next__ button gets next result page with questions. <br /> 
It's disabled if there are no additional questions that can be presented.<br />  

### Technologies / libraries  used in this project:
* React
* Bootstrap
* JavaScript ES6

### Running the client
To run the client app please insure you have Node 8.10.0 or later on your machine.<br /> 
cd into `client/q-fetcher-app` and run `npm start` or `yarn start`<br /> 
Open `http://localhost:3000` to view the app running in the browser.


