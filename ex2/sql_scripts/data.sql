INSERT INTO
    estate_agent(id, name, address, login, password) VALUES
    (1, 'Rainer Reihenhaus', 'Reihe 5', 'rreihenhaus', 'pw');
INSERT INTO
    estate_agent(id, name, address, login, password)
VALUES (2, 'Anton Anwesen', 'Am Strand 7', 'aanwesen', 'pw');
INSERT INTO
    estate_agent(id, name, address, login, password)
VALUES (3, 'Volker Racho', 'Löfflerstraße 2', 'vracho', 'pw');
SELECT setval('estate_agent_id_seq', (SELECT MAX(id) from estate_agent));

INSERT INTO
    person(id, first_name, name, address)
VALUES (1, 'Claire', 'Grube', 'Neben der Stadtreinigung 6');
INSERT INTO
    person(id, first_name, name, address)
VALUES (2, 'Meinhart', 'Esrohr', 'Reeperbahn 111');
INSERT INTO
    person(id, first_name, name, address)
VALUES (3, 'Simon', 'Unge', 'Madeira');
SELECT setval('person_id_seq', (SELECT MAX(id) from person));

INSERT INTO
    apartment(id, city, postal_code, street, street_nr, square_area, agent_id, renter_id, floor, rent, rooms, balcony, built_in_kitchen)
VALUES (1, 'O´beck city', '27711', 'Hinter der Loge', '7', 99.5, 1, 2, 1, 600, 2, false, true);
INSERT INTO
    apartment(id, city, postal_code, street, street_nr, square_area, agent_id, renter_id, floor, rent, rooms, balcony, built_in_kitchen)
VALUES (2, 'Hamburg', '20537', 'Borgfelder Straße', '16', 12.5, 2, 3, 0, 1800.5, 4, true, false);
INSERT INTO
    apartment(id, city, postal_code, street, street_nr, square_area, agent_id, renter_id, floor, rent, rooms, balcony, built_in_kitchen)
VALUES (3, 'Zaun', '876543', 'Dicke Gasse', '1', 33, 1, 1, 15, 1400, 1, false, false);

INSERT INTO
    house(id, city, postal_code, street, street_nr, square_area, agent_id, seller_id, floors, price, garden)
VALUES (4, 'Hamburg', '20251', 'Reeperbahn', '111', 54.43, 3, 2, 4, 200000.34, false);
INSERT INTO
    house(id, city, postal_code, street, street_nr, square_area, agent_id, seller_id, floors, price, garden)
VALUES (5, 'Osterholz Scharmbek', '23432', 'Feldstraße', '1', 154.43, 1, 1, 1, 199999.34, true);
INSERT INTO
    house(id, city, postal_code, street, street_nr, square_area, agent_id, seller_id, floors, price, garden)
VALUES (6, 'New York', '99999', 'Boulevard of broken dreams', '133', 9.99, 2, 2, 1, 922922.34, false);
SELECT setval('estate_id_seq', (SELECT MAX(id) from estate));

INSERT INTO
    tenancy_contract(contract_number, date, place, start_date, duration_months, additional_costs, renter_id, apartment_id)
VALUES (1, TO_DATE('17/12/2015', 'DD/MM/YYYY'), 'Hamburg', TO_DATE('17/12/2016', 'DD/MM/YYYY'), 12, 0, 1, 3);

INSERT INTO
    purchase_contract(contract_number, date, place, number_of_installments, interest_rate, buyer_id, house_id)
VALUES (2, to_date('13/04/2022', 'DD/MM/YYYY'), 'New York', 80, 1.5, 2, 5);

SELECT setval('contract_contract_number_seq', (SELECT MAX(contract_number) from contract));