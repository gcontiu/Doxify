### How To
``` https://github.com/gcontiu/helloworld-springboot.git```

```docker build -t doxify .``` 

```docker run -p 8585:8585 -d --name=Doxify doxify``` 


### Access Main Dashboard
 - http://localhost:8585/dashboard.html
 
### Connect to Database
 - http://localhost:8585/h2-console
 - change the JDBC URL: ```jdbc:h2:file:~/testdb```