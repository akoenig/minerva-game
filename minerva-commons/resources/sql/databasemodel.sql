CREATE TABLE world (
	"id" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	"token" VARCHAR(10) NOT NULL,
	"name" VARCHAR(64) UNIQUE NOT NULL,
	"description" VARCHAR(1024) NOT NULL,
	"map" VARCHAR(37) NOT NULL,
	"map_underlay" VARCHAR(37) NOT NULL,
	"thumbnail" VARCHAR(37) NOT NULL,
	"author" VARCHAR(256) NOT NULL,
	"version" VARCHAR(10) NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE country (
	"id" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	"token" VARCHAR(5) NOT NULL,
	"name" VARCHAR(25) NOT NULL,
	"color" VARCHAR(8) NOT NULL,
	"world" INT NOT NULL,
	"continent" INT NOT NULL,
	PRIMARY KEY ("id"),
	UNIQUE("token", "name", "world")
);

CREATE TABLE continent (
	"id" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
	"name" VARCHAR(25) UNIQUE NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE neighbour (
	"id" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
	"country" INT NOT NULL, 
	"neighbour_country" INT NOT NULL,
	PRIMARY KEY ("id"),
	UNIQUE ("country", "neighbour_country")
);

CREATE TABLE player (
	"id" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	"username" VARCHAR(25) UNIQUE NOT NULL,
	"password" VARCHAR(32) NOT NULL,
	"last_name" VARCHAR(25) NOT NULL,
	"first_name" VARCHAR(25) NOT NULL,
	"email" VARCHAR(320) UNIQUE NOT NULL,
	"logged_in" SMALLINT NOT NULL DEFAULT 0,
	PRIMARY KEY ("id")
);

-- ## Player ############################################### (Password is: 1234)
insert into player ("username", "password", "last_name", "first_name", "email") values ('Takeru', 'dcfd7127776e1994fea695a7f31a6381', 'Bollmann', 'Christian', 'cbollmann@stud.hs-bremen.de');
insert into player ("username", "password", "last_name", "first_name", "email") values ('cstrempel', 'f144c9a649cb9abeb68e4323dc15e14c', 'Strempel', 'Carina', 'cstrempel@stud.hs-bremen.de');