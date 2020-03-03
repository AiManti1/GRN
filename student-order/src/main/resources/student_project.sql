DROP TABLE IF EXISTS j_student_child;
DROP TABLE IF EXISTS j_student_order;
DROP TABLE IF EXISTS j_passport_office;
DROP TABLE IF EXISTS j_register_office;
DROP TABLE IF EXISTS j_country_struct;
DROP TABLE IF EXISTS j_university;
DROP TABLE IF EXISTS j_street;

CREATE TABLE j_street
(
    street_code integer not null,
    street_name varchar(300),
    PRIMARY KEY (street_code)
);

CREATE TABLE j_university
(
    university_id integer not null,
    university_name varchar(300),
    PRIMARY KEY (university_id)
);

CREATE TABLE j_country_struct
(
    area_id char(12) not null,
    area_name varchar(200),
    PRIMARY KEY (area_id)
);

CREATE TABLE j_passport_office
(
    p_office_id integer not null,
    p_office_area_id char(12) not null,
    p_office_name varchar(200),
    PRIMARY KEY (p_office_id),
    FOREIGN KEY (p_office_area_id) REFERENCES j_country_struct(area_id) ON DELETE RESTRICT
);

CREATE TABLE j_register_office
(
    r_office_id integer not null,
    r_office_area_id char(12) not null,
    r_office_name varchar(200),
    PRIMARY KEY (r_office_id),
    FOREIGN KEY (r_office_area_id) REFERENCES j_country_struct(area_id) ON DELETE RESTRICT
);


CREATE TABLE j_student_order
(
    student_order_id SERIAL,
    student_order_status integer not null,
    student_order_date timestamp not null,
    h_sur_name varchar(100) not null,
    h_given_name varchar(100) not null,
    h_patronymic varchar(100) not null,
    h_date_of_birth date not null,
    h_passport_seria varchar(10) not null,
    h_passport_number varchar(10) not null,
    h_passport_date date not null,
h_passport_office_id integer not null,
h_post_index varchar(10),
h_street_code integer not null,
h_building varchar(10) not null,
h_extension varchar(10),
h_apartment varchar(10),
h_university_id integer not null,
h_student_number varchar(30) not null,
w_sur_name varchar(100) not null,
w_given_name varchar(100) not null,
w_patronymic varchar(100) not null,
w_date_of_birth date not null,
w_passport_seria varchar(10) not null,
w_passport_number varchar(10) not null,
w_passport_date date not null,
w_passport_office_id integer not null,
w_post_index varchar(10),
w_street_code integer not null,
w_building varchar(10) not null,
w_extension varchar(10),
w_apartment varchar(10),
w_university_id integer not null,
w_student_number varchar(30) not null,
certificate_id varchar(20) not null,
register_office_id integer not null,
marriage_date date not null,
PRIMARY KEY (student_order_id),
FOREIGN KEY (h_street_code) REFERENCES j_street(street_code) ON DELETE RESTRICT,
FOREIGN KEY (h_passport_office_id) REFERENCES j_passport_office(p_office_id) ON DELETE RESTRICT,
FOREIGN KEY (h_university_id) REFERENCES j_university(university_id) ON DELETE RESTRICT,
FOREIGN KEY (w_street_code) REFERENCES j_street(street_code) ON DELETE RESTRICT,
FOREIGN KEY (w_passport_office_id) REFERENCES j_passport_office(p_office_id) ON DELETE RESTRICT,
FOREIGN KEY (w_university_id) REFERENCES j_university(university_id) ON DELETE RESTRICT,
FOREIGN KEY (register_office_id) REFERENCES j_register_office(r_office_id) ON DELETE RESTRICT
);

CREATE TABLE j_student_child
(
  student_child_id SERIAL,
  student_order_id integer not null,
  c_sur_name varchar(100) not null,
  c_given_name varchar(100) not null,
  c_patronymic varchar(100) not null,
  c_date_of_birth date not null,
  c_certificate_number varchar(10) not null,
  c_certificate_date date not null,
  c_register_office_id integer not null,
  c_post_index varchar(10),
  c_street_code integer not null,
  c_building varchar(10) not null,
  c_extension varchar(10),
  c_apartment varchar(10),
  PRIMARY KEY (student_child_id),
  FOREIGN KEY (student_order_id) REFERENCES j_student_order(student_order_id) ON DELETE RESTRICT,
  FOREIGN KEY (c_street_code) REFERENCES j_street(street_code) ON DELETE RESTRICT,
  FOREIGN KEY (c_register_office_id) REFERENCES j_register_office(r_office_id) ON DELETE RESTRICT
);

CREATE INDEX idx_student_order_status ON j_student_order(student_order_status);

CREATE INDEX idx_student_order_id ON j_student_child(student_order_id);

