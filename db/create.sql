create database aptu_students2;
use aptu_students2;

create table student (
	id 			MEDIUMINT NOT NULL AUTO_INCREMENT,
	name 		VARCHAR(45),
	department 	VARCHAR(45),
	PRIMARY KEY (id)
);