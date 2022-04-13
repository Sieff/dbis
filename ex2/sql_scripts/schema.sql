drop table if exists estate_agent cascade;
create table estate_agent (
    id serial primary key,
    name varchar (255),
    address varchar (255),
    login varchar (255) unique,
    password varchar (255)
);

drop table if exists estate cascade;
create table estate (
    id serial primary key,
    city varchar (255),
    postal_code integer,
    street varchar (255),
    street_nr integer,
    square_area decimal,
    agent_id integer references estate_agent(id)
);

-- vertical partitioning

drop table if exists person cascade;
create table person (
    id serial primary key,
    first_name varchar (255),
    name varchar (255),
    address varchar (255)
);

drop table if exists house cascade;
create table house (
    seller_id integer references person(id),
    floors integer,
    price decimal,
    garden boolean,
    primary key (id)
) inherits (estate);

drop table if exists apartment cascade;
create table apartment (
    renter_id integer references person(id),
    floor integer,
    rent decimal,
    rooms integer,
    balcony boolean,
    built_in_kitchen boolean,
    primary key (id)
) inherits (estate);

drop table if exists contract cascade;
create table contract (
    contract_number serial primary key,
    date date,
    place varchar (255)
);

drop table if exists tenancy_contract cascade;
create table tenancy_contract (
    start_date date,
    duration_months integer,
    additional_costs decimal,
    primary key (contract_number)
) inherits (contract);

drop table if exists purchase_contract cascade;
create table purchase_contract (
    number_of_installments integer,
    interest_rate decimal,
    primary key (contract_number)
) inherits (contract);

drop table if exists rent cascade;
create table rent (
    contract_number integer references tenancy_contract(contract_number),
    renter_id integer references person(id),
    apartment_id integer references apartment(id),
    primary key (contract_number, apartment_id)
);

drop table if exists sell cascade;
create table sell (
    contract_number integer references purchase_contract(contract_number),
    buyer_id integer references person(id),
    house_id integer references house(id),
    primary key (contract_number, house_id)
);