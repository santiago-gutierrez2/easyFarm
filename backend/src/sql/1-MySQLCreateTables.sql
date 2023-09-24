DROP TABLE User;
Drop TABLE Farm;

Create TABLE Farm (
    id BiGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) COLLATE latin1_bin NOT NULL,
    address VARCHAR(500) NOT NULL,
    sizeHectares INTEGER NOT NULL,
    creationDate DATETIME NOT NULL,
    CONSTRAINT  FarmPk PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE User (
    id BIGINT NOT NULL AUTO_INCREMENT,
    userName VARCHAR(60) COLLATE latin1_bin NOT NULL,
    password VARCHAR(60) NOT NULL, 
    firstName VARCHAR(60) NOT NULL,
    lastName VARCHAR(60) NOT NULL, 
    email VARCHAR(60) NOT NULL,
    role TINYINT NOT NULL,
    farmId BIGINT NOT NULL,
    CONSTRAINT UserPK PRIMARY KEY (id),
    CONSTRAINT UserNameUniqueKey UNIQUE (userName),
    CONSTRAINT UserFarmFK FOREIGN KEY (farmId) REFERENCES Farm(id) On DELETE CASCADE
) ENGINE = InnoDB;

CREATE INDEX UserIndexByUserName ON User (userName);
