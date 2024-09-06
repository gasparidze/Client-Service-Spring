INSERT INTO client_service.client(id, fio, birth_date, login, password, phone_numbers, emails)
VALUES (1, 'Тест1', '1991-01-10', 'test1@mail.ru', '123', ARRAY['89111111111'], ARRAY['test1@mail.ru']),
       (2, 'Тест2', '1992-01-10', 'test2@mail.ru', '123', ARRAY['89111111112'], ARRAY['test2@mail.ru']),
       (3, 'Тест3', '1993-01-10', 'test3@mail.ru', '123', ARRAY['89111111113'], ARRAY['test3@mail.ru']),
       (4, 'Тест4', '1994-01-10', 'test4@mail.ru', '123', ARRAY['89111111114'], ARRAY['test4@mail.ru']),
       (5, 'Тест5', '1995-01-10', 'test5@mail.ru', '123', ARRAY['89111111115'], ARRAY['test5@mail.ru']),
       (6, 'Тест6', '1996-01-10', 'test6@mail.ru', '123', ARRAY['89111111116'], ARRAY['test6@mail.ru']),
       (7, 'Тест7', '1997-01-10', 'test7@mail.ru', '123', ARRAY['89111111117'], ARRAY['test7@mail.ru']),
       (8, 'Тест8', '1998-01-10', 'test8@mail.ru', '123', ARRAY['89111111118'], ARRAY['test8@mail.ru']),
       (9, 'Тест9', '1999-01-10', 'test9@mail.ru', '123', ARRAY['89111111119'], ARRAY['test9@mail.ru']),
       (10, 'Тест10', '1999-01-10', 'test10@mail.ru', '123', ARRAY['89111111110'], ARRAY['test10@mail.ru']);

SELECT SETVAL('client_service.client_id_seq', (SELECT MAX(id) FROM client_service.client));

INSERT INTO client_service.client_account(balance, client_id)
VALUES (100, (SELECT id FROM client_service.client WHERE fio = 'Тест1')),
       (100, (SELECT id FROM client_service.client WHERE fio = 'Тест2')),
       (100, (SELECT id FROM client_service.client WHERE fio = 'Тест3')),
       (100, (SELECT id FROM client_service.client WHERE fio = 'Тест4')),
       (100, (SELECT id FROM client_service.client WHERE fio = 'Тест5')),
       (100, (SELECT id FROM client_service.client WHERE fio = 'Тест6')),
       (100, (SELECT id FROM client_service.client WHERE fio = 'Тест7')),
       (100, (SELECT id FROM client_service.client WHERE fio = 'Тест8')),
       (100, (SELECT id FROM client_service.client WHERE fio = 'Тест9')),
       (100, (SELECT id FROM client_service.client WHERE fio = 'Тест10'));