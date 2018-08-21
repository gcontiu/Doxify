### How To
``` git clone https://github.com/naterivah/helloworld-springboot.git```

```docker build -t nbittich/helloworld-springboot helloworld-springboot``` 

```docker run -p 8585:8585 -d --name=HelloWorldSpringBootApp nbittich/helloworld-springboot``` 

### Cloc
- Java: 195 lines of code
- Javascript: 152 lines of code

### Example
 - http://localhost:8585/ 
    - Websocket example: Random quote pushed from the server
    - CRUD example
 - http://localhost:8585/greeting/jean => Hello, Jean!
