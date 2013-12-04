create table tbl_user(

    id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255)

) Engine=InnoDB;

insert into tbl_user (id,username,password,email) values 
('abc123', 'endy', '123', 'endy.muhardin@gmail.com'),
('abc456', 'hamzah', '123', 'hamzah.radjab@gmail.com'),
('abc789', 'john', '123', 'john.gay@gmail.com');