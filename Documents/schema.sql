CREATE DATABASE isuview;

CREATE TABLE Users 
(
    name VARCHAR(255),
    id INT (7) PRIMARY KEY
);

CREATE TABLE Intersection
(
    id VARCHAR(16) PRIMARY KEY,
    x FLOAT,
    y FLOAT
);

CREATE TABLE Building
(
	id VARCHAR(16) PRIMARY KEY,
    buildingName VARCHAR(32),
    longitude FLOAT,
    latitude FLOAT
);

CREATE TABLE Room
(
    buildingId VARCHAR(16),
    intersectionId VARCHAR(16) REFERENCES Intersection(id),
    roomNumber INT(4),
    CONSTRAINT PK_Room PRIMARY KEY (buildingId, roomNumber),
    CONSTRAINT FK_RoomBuilding FOREIGN KEY (buildingId) references Building(id),
    CONSTRAINT FK_RoomIntersection FOREIGN KEY (intersectionId) REFERENCES Intersection(id)
);

CREATE TABLE Edge
(
    intersection1Id VARCHAR(16) REFERENCES Intersection(id),
    intersection2Id VARCHAR(16) REFERENCES Intersection(id),
    CONSTRAINT PK_Edge PRIMARY KEY (intersection1Id, intersection2Id),
    CONSTRAINT FK_EdgeIntersection1 FOREIGN KEY (intersection1Id) REFERENCES Intersection(id),
    CONSTRAINT FK_EdgeIntersection2 FOREIGN KEY (intersection2Id) REFERENCES Intersection(id)
);

CREATE TABLE Box
(
    intersectionId VARCHAR(16),
    startX FLOAT,
    startY FLOAT,
    endX FLOAT,
    endY FLOAT,
    CONSTRAINT FK_BoxIntersection FOREIGN KEY (intersectionId) REFERENCES Intersection(id)
);


/**
CREATE TABLE Shape
(
    intersectionID INT (2),

);

CREATE TABLE POI
(
    location Building,
    comments VARCHAR(255)
);

CREATE TABLE Comment
(
    posterName CHAR(255),
    location Building,
    text VARCHAR(140)
);

CREATE TABLE Bus
(
    stops VARCHAR(255)
);
**/