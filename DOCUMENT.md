# Application Documentation

The given task was to create a RESTful API that utilizes money transactions between bank customers. For the sake of simplicity the database should be in-memory. So a localhost server was needed in order to achieve connection between the client, the server and the database.

For this reason, my approach was to use **Jetty server** which combines an _Http server_ and _Http client_ along with _servlet engines_. For the communication between the server and the client, **Jersey** was used which is a _Java_ Framework that provides RESTful Web Services and it is compatible with Jetty.

The in-memory database used was the H2 database, which except from being embedded, it is lightweight and has a really small footprint at the extracted _.jar_ files.

## Approach

The general idea of the application is the ability to store the database entities in _cache memory_, in order to be easily accessible by the application anytime. You can check the speed of retrieving the objects after you send a request to the server for the first time.
Every time a response is back, then along with the database entries, the _cache memory_ is also updated. There are cache memories for all of the three basic entities _(User, Account, Transaction)_

## Architecture
The biggest caution was given on the architecture of the application.

It consists of:
* **Models**: User, Account, Transaction - _represent the entries of the database and the actual entities of the application._
* **Repositories**: UserRepository, AccountRepository, TransactionRepository - _contain the aggregated entities for each model separately, stored in cache for easy access._
* **Routers**: UserRouter, AccountRouter, TransactionRouter - _the controllers that are responsible for the communication between the client and the server._

## Procedure

The procedure is as follows:

1. The **database is generated** and stored in memory.
2. The **repositories** are then filled with the entries of the database and stored in cache.
3. The **Jetty Server** starts and the **Jersey servlets** are also set, to include the right controllers that are required for the proper communication.
4. The server waits for a request to come.
5. Assume that a GET request comes to server at the following URL: `http://localhost:8080/accounts/getAll` . Then it searches in the cache to check if there are already accounts stored and gives back the aggregated accounts in a JSON format.
6. For the POST, PUT and DELETE requests, the server interprets the request to SQL queries and makes changes in the database. When these changes are made, the objects in the cache also change.
7. The application terminates when the server is manually terminated.

## Concurrency

In terms of concurrency, the methods were transactions or account updates were made,  were handled to be synchronized. That means that each time a transaction is about to be made, it will synchronously made and no other transaction can be made in parallel.
## Things to improve

1. *Concurrency testing*: No concurrency testing exists in the test classes. There is space for improvement on this, although testing for concurrency doesn't always discover bugs and may lead to the opposite results.
2. *CI/CD*: CI/CD pipelining is necessary when a RESTful API is implemented. Since the storage happened in-memory and the server was a localhost, this couldn't be properly implemented.
3. *Better java exceptions handling*: In the whole application, many exceptions occur. That leads to many _try-catch_ cases, as well as many exceptions thrown in methods which are in need of better handling instead of just printing a stack trace.