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
   name VARCHAR(50) NOT NULL,
   primary key (id)
);

CREATE TABLE book_checkout (
   id INT NOT NULL AUTO_INCREMENT,
   book_uid VARCHAR(50) NOT NULL,
   member_id VARCHAR(50) NOT NULL,
   checkout_date DATE NOT NULL,
   return_date DATE,
   primary key (id),
   foreign key (book_uid) references book(uid)
);