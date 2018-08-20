### How To
``` git clone https://github.com/naterivah/helloworld-springboot.git```

```docker build -t nbittich/helloworld-springboot helloworld-springboot``` 

```docker run -p 8585:8585 -d --name=HelloWorldSpringBootApp nbittich/helloworld-springboot``` 

### Cloc
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
XML                             47              0              0           1319
JSON                             1              0              0            410
Java                             7             35              4            136
JavaScript                       1              6              0             61
HTML                             1              0              0             51
Maven                            1              8              0             45
Markdown                         1              4              0              7
Dockerfile                       1              0              0              6
-------------------------------------------------------------------------------
SUM:                            60             53              4           2035
-------------------------------------------------------------------------------

### Example
 - http://localhost:8585/ => Websocket example: Random quote pushed from the server
 - http://localhost:8585/greeting/jean => Hello, Jean!
