CREATE EXTENSION citext;

create table prez_group_user (
 		user_id int NOT NULL,
		username varchar(50) UNIQUE NOT NULL,
		password_hash varchar(200) NOT NULL,
	 	first_name varchar(50) NOT NULL,
	 	last_name varchar(50) NOT NULL,
	 	email citext UNIQUE NOT NULL,
		CONSTRAINT PK_prez_group_user PRIMARY KEY(user_id)
 )