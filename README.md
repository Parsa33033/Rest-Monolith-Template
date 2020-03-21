# Rest-Monolith-Template
a rest template for monolith architecture in Spring Boot

# How To Run (with docker):

1) In the folder containing pom.xml type command ```mvn install -DskipTest``` to build the project
2) Note that the host names used in the project are related to docker containers. for instance you can use ```jdbc:mysql://localhost:3306/database_name``` if you are not going to run the project inside a docker container, otherwise you have to use the mysql container name instead of localhost.
3) Go to ```target\app-docker-compose``` folder and type command ```docker-compose up``` to start the project
4) You can add your out controller functions in MainController class

note: you can use application.yml in ```src\main\resource``` folder to change settings for the project

note: all passwords are ```changeit```

# Paths:
1) ```https://localhost:8080/register``` for registering a user by having a json body as below:

```
{
	"username": "user",
	"password": "pass",
	"email": "youremail@outlook.com",
	"roles": [
		{ "type": "USER" }
	]
}
```

note: you can add more info such as firstName and lastName. the inputs must match User class in the project.

2) ```https://localhost:8080/authenticate``` for authenticating a user by having a json body as below:

```
{
	"username": "user",
	"password": "pass"
}
```

note: the output to this request will be user's JWT which has to be used in every other path Authorization Header. for instace:

```Authorization=Bearer JWTTOKEN```


3) ```https://localhost:8080/log-out``` for logging out a user by having a json body as below:

```
{
	"username": "user",
	"jwt": "JWTTOKEN"
}
```

note: this URL is not an exception and needs JWT for Authorization Header.

