### How To
``` https://github.com/gcontiu/helloworld-springboot.git```

```docker build -t nbittich/helloworld-springboot helloworld-springboot``` 

```docker run -p 8585:8585 -d --name=HelloWorldSpringBootApp nbittich/helloworld-springboot``` 

### Cloc
- Java: 195 lines of code
- Javascript: 152 lines of code

### Example
 - http://localhost:8585/ 
    - Websocket example: Random quote pushed from the server
    - CRUD example
 - http://localhost:8585/greeting/jean => Hello, jean!

### Access Main Dashboard
 - http://localhost:8585/dashboard.html
 
### Connect to Database
 - http://localhost:8585/h2-console
 - change the JDBC URL: ```jdbc:h2:file:~/testdb```