# EasyFarm 

## Requirements

- Node 14.
- Java 11 (tested with AdoptOpenJDK 11).
- Maven 3.8+.
- MySQL 8.

## Database creation

```
Start Mysql server if not running (e.g. mysqld).

mysqladmin -u root create easyfarm -p
mysqladmin -u root create easyfarm -p

mysql -u root -p
    CREATE USER 'easyfarm'@'localhost' IDENTIFIED BY 'easyfarm';
    GRANT ALL PRIVILEGES ON easyfarm.* to 'easyfarm'@'localhost' WITH GRANT OPTION;
    GRANT ALL PRIVILEGES ON easyfarm.* to 'easyfarm'@'localhost' WITH GRANT OPTION;
    exit
```

## Run

```
cd backend
mvn sql:execute (only first time to create tables)
mvn spring-boot:run

cd frontend
npm install (only first time to download libraries)
npm start
```
