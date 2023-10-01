-- ----------------------------------------------------------------------------
-- Put here INSERT statements for inserting data required by the application
-- in the "easyfarm" database.
-- -----------------------------------------------------------------------------

/* ----------------------- FARM ----------------------*/
INSERT into Farm (name, address, sizeHectares, creationDate)
    VALUES ('Granxa Nova', 'Arzúa, Lugo', 25, curdate());

/* --------------- Admin and employees --------------*/
/* ADMIN (Santiago)*/
INSERT into User (userName, password, firstName, lastName, email, role, farmId, isEliminated)
    VALUES ('admin', '$2a$10$zZnDjiu51rH1SeuL6HqH9ORE1ZHRaJGLBzNi.vjYfhLJ5ZVYF0RMC',
            'Santiago', 'Gutiérrez', 'santiago.gutierrez@udc.es', 1, 1, false);
/* employee 1 (Paulo)*/
INSERT into User (userName, password, firstName, lastName, email, role, farmId, isEliminated)
    VALUES ('employee1', '$2a$10$zZnDjiu51rH1SeuL6HqH9ORE1ZHRaJGLBzNi.vjYfhLJ5ZVYF0RMC',
            'Paulo', 'Gutiérrez', 'paulo.gutierrez@gmail.com', 0, 1, false);
/* employee 2 (Ale)*/
INSERT into User (userName, password, firstName, lastName, email, role, farmId, isEliminated)
    VALUES ('employee2', '$2a$10$zZnDjiu51rH1SeuL6HqH9ORE1ZHRaJGLBzNi.vjYfhLJ5ZVYF0RMC',
            'Alexandra', 'Gutiérrez', 'ale.gutierrez@gmail.com', 0, 1, false);