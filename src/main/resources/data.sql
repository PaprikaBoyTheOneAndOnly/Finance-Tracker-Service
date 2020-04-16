CREATE TABLE IF NOT EXISTS user (
    id       INTEGER     NOT NULL AUTO_INCREMENT,
    email    VARCHAR(40) NOT NULL,
    password CHAR(64)    NOT NULL, -- stored as a sha-256 hash
    PRIMARY KEY (id)
);

INSERT INTO user (id, email, password)
VALUES (1, 'janhasler01@gmail.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),
       (2, 'jeremy.senn20801@gmail.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92')
       ON DUPLICATE KEY UPDATE id = id;

CREATE TABLE IF NOT EXISTS spending (
    id          INTEGER     NOT NULL AUTO_INCREMENT,
    user_id     INTEGER     NOT NULL,
    description VARCHAR(40) NOT NULL,
    amount      DOUBLE      NOT NULL,
    date        DATE        NOT NULL,
    type        SMALLINT DEFAULT (0),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
);

INSERT INTO spending (id, user_id, description, amount, date, type)
VALUES (1, 1, 'Salt bill', -30, '2020-02-10', 1),
       (2, 1, 'Salary', 4500, '2019-10-25', 1),
       (3, 1, 'Skillspark', -25, '2020-04-14', 0),
       (4, 1, 'Coop Lunch', -12.50, '2020-04-14', 0),
       (5, 1, 'Coop Lunch', -9.50, '2020-04-13', 0),
       (6, 2, 'Coop Lunch', -5.50, '2020-04-13', 0),
       (7, 2, 'Sunrise bill', -60, '2020-04-25', 1),
       (8, 2, 'Salary', 4500, '2019-10-25', 1),
       (9, 2, 'Cinema', -19.90, '2020-04-10', 0),
       (10, 2, 'Pizza', -19.90, '2020-04-10', 0)
ON DUPLICATE KEY UPDATE id = id;
