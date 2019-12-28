CREATE TABLE book (
   id INT NOT NULL AUTO_INCREMENT,
   uid VARCHAR(50) NOT NULL,
   rack_number VARCHAR(50) NOT NULL,
   book_id VARCHAR(50) NOT NULL,
   title VARCHAR(50) NOT NULL,
   author VARCHAR(50) NOT NULL,
   subject_category VARCHAR(50) NOT NULL,
   publication_date DATE NOT NULL,
   primary key (uid)
);

CREATE TABLE member (
   id INT NOT NULL AUTO_INCREMENT,
   user_name VARCHAR(50) NOT NULL,
   name VARCHAR(50) NOT NULL,
   email VARCHAR(50) NOT NULL,
   join_date DATE NOT NULL,
   password VARCHAR(60) NOT NULL,
   primary key (user_name)
);

CREATE TABLE book_checkout (
   id INT NOT NULL AUTO_INCREMENT,
   book_uid VARCHAR(50) NOT NULL,
   user_name VARCHAR(50) NOT NULL,
   checkout_date DATE NOT NULL,
   return_date DATE,
   primary key (id),
   foreign key (book_uid) references book(uid),
   foreign key (user_name) references member(user_name)
);

CREATE TABLE fine (
   id INT NOT NULL AUTO_INCREMENT,
   amount INT NOT NULL,
   imposed_date DATE NOT NULL,
   payment_date DATE,
   book_checkout_id INT NOT NULL,
   primary key (book_checkout_id),
   foreign key (book_checkout_id) references book_checkout(id)
);
