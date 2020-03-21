# Rest-Monolith-Template
a rest template for monolith architecture in Spring Boot

# How To Run (with docker):

1) in the folder containing pom.xml type command ```mvn install -DskipTest``` to build the project
2) note that the host names used in the project are related to docker containers. for instance you can use ```jdbc:mysql://localhost:3306/database_name``` if you are not going to run the project inside a docker container, otherwise you have to use the mysql container name instead of localhost.
3) go to ```target\app-docker-compose``` folder and type command ```docker-compose up``` to start the project
