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
    agent_id integer references estate_agent(id) on delete cascade
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
    primary key (id),
    foreign key (agent_id) references estate_agent(id) on delete cascade
) inherits (estate);

drop table if exists apartment cascade;
create table apartment (
    renter_id integer references person(id),
    floor integer,
    rent decimal,
    rooms integer,
    balcony boolean,
    built_in_kitchen boolean,
    primary key (id),
    foreign key (agent_id) references estate_agent(id) on delete cascade
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
    renter_id integer references person(id) on delete cascade,
    apartment_id integer references apartment(id) on delete cascade,
    primary key (contract_number)
) inherits (contract);

drop table if exists purchase_contract cascade;
create table purchase_contract (
    number_of_installments integer,
    interest_rate decimal,
    buyer_id integer references person(id) on delete cascade,
    house_id integer references house(id) on delete cascade,
    primary key (contract_number)
) inherits (contract);