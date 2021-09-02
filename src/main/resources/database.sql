#table users
CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    email VARCHAR(255) NOT NULL,
    pasword VARCHAR(255)
)
ENGINE = InnoDB;
#table roles
CREATE TABLE roles(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    name VARCHAR(100) NOT NULL
)
ENGINE =InnoDB;
#table user_roles
CREATE TABLE user_roles(
    user_id INT NOT NULL,
    role_id INT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id),

    UNIQUE (user_id, role_id)
)ENGINE = InnoDB;
#insert data
#INSERT INTO users VALUE (1, 'Ilya', 23, 'iewq@mail.ru', '$2a$12$VHPNLZrsv0hGzijSJee1mey.uTsmm24IE1D56Y9ghMK1iGSZfFhNC');
#INSERT INTO roles VALUE (1, 'ROLE_USER');
#INSERT INTO roles VALUE (2, 'ROLE_ADMIN');
#INSERT INTO user_roles VALUE (1, 2);