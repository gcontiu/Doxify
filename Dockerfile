FROM maven
WORKDIR /tmp
COPY . .
RUN mvn package -DskipTests
EXPOSE 8585
CMD ["mvn", "spring-boot:run"]