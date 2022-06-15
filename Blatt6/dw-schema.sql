DROP TABLE IF EXISTS dw_branch CASCADE;
CREATE TABLE dw_branch (
    country varchar(255) NOT NULL,
    region varchar(255) NOT NULL ,
    city varchar(255) NOT NULL,
    shop varchar(255) NOT NULL,
    PRIMARY KEY (shop)
);

DROP TABLE IF EXISTS dw_product CASCADE;
CREATE TABLE dw_product (
    product_category varchar(255) NOT NULL,
    product_family varchar(255) NOT NULL ,
    product_group varchar(255) NOT NULL,
    article varchar(255) NOT NULL,
    price int NOT NULL,
    PRIMARY KEY (article)
);

DROP TABLE IF EXISTS dw_time CASCADE;
CREATE TABLE dw_time (
    date timestamp NOT NULL,
    year int NOT NULL,
    month int NOT NULL ,
    quarter int NOT NULL,
    day int NOT NULL,
    PRIMARY KEY (date)
);

DROP TABLE IF EXISTS dw_fact CASCADE;
CREATE TABLE dw_fact (
    article varchar(255) NOT NULL,
    shop varchar(255) NOT NULL,
    date timestamp NOT NULL,
    sales int NOT NULL,
    revenue float NOT NULL,
    FOREIGN KEY (article) references dw_product (article),
    FOREIGN KEY (shop) references dw_branch (shop),
    FOREIGN KEY (date) references dw_time (date)
);