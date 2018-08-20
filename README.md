### How To
``` git clone https://github.com/naterivah/helloworld-springboot.git```

```docker build -t nbittich/helloworld-springboot helloworld-springboot``` 

```docker run -p 8585:8585 -d --name=HelloWorldSpringBootApp nbittich/helloworld-springboot``` 

### Cloc
Java: 136 lines of code
Javascript: 61 lines of code

### Example
 - http://localhost:8585/ => Websocket example: Random quote pushed from the server
 - http://localhost:8585/greeting/jean => Hello, Jean!
