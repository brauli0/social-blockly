# Social Blockly

Degree final project: Angular web app + springboot + mysql, using blockly library

## Requirements

- MySQL 5+.
- Java SE 8+.
- Maven 3+.
- Node 8+.
- Npm 3+.
- Angular CLI 7+ `sudo npm install -g @angular/cli`


## Database
Start MySQL server with `mysqld`
```
$ mysqladmin -u root create socialblockly
$ mysqladmin -u root create socialblocklytest


$ mysql -u root
    CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
    GRANT ALL PRIVILEGES ON socialblockly.* to 'admin'@'localhost' WITH GRANT OPTION;
    GRANT ALL PRIVILEGES ON socialblocklytest.* to 'admin'@'localhost' WITH GRANT OPTION;
    exit
```


## Run
### MySQL
```
$ mysqld
```
### Backend
```
$ cd backend
$ mvn sql:execute (first time only)
$ mvn spring-boot:run
```
### Frontend
```
$ cd frontend
$ npm i (first time only)
$ ng serve -o
```

#### Browser opens automatically in http://localhost:4200