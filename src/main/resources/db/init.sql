CREATE TABLE IF NOT EXISTS todos (
   id int PRIMARY KEY auto_increment,
   name VARCHAR,
   completed BOOLEAN DEFAULT 0
);