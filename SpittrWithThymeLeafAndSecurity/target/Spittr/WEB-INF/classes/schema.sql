create table Spittle (
	id identity,
	message varchar(140) not null,
	created_at timestamp not null,
	latitude double,
	longitude double
);

create table Spitter (
	id identity,
	username varchar(20) unique not null,
	password varchar(20) not null,
	first_name varchar(30) not null,
	last_name varchar(30) not null,
	email varchar(30) not null,
	role varchar(30) not null default('ROLE_SPITTER')
);

insert into Spitter (id, username, password, first_name, last_name, email, role) VALUES (1, 'admin', 'stas', 'stas', 'vladyka', 'stavladyk@gmail.com', 'ROLE_SPITTER');