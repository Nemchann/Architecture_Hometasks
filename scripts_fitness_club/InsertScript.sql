insert into booking_statuses 
(booking_status_name)
values 
	('ACCEPTED'),
	('CANCELLED'),
	('PROCESSING');

select * from booking_statuses;

insert into payment_statuses 
(payment_status_name)
values 
	('ACCEPTED'),
	('CANCELLED'),
	('PROCESSING'); 
	
insert into roles 
(role_name)
values 
	('CLIENT'),
	('TRAINER'),
	('ADMINISTRATOR');

insert into workout_types (type_name) values 
('Mind & Body'),
('Strength'),
('Cardio'),
('Dance'),
('Womens Health')



INSERT INTO users (role_id, login, password_hash) VALUES
(1, 'anna_client111', 'hash123'),  -- клиент Анна
(2, 'elena_trainer222', 'hash456'),  -- тренер Елена
(1, 'maria_client333', 'hash789'),  -- клиент Мария
(2, 'dmitry_trainer444', 'hash012'),  -- тренер Дмитрий
(3, 'admin5_1', 'hash999');  -- admin

select * from users;

INSERT INTO profiles (user_id, selfname, surname, birthday, phone, email) VALUES
((select id from users where login = 'anna_client111'), 'Анна', 'Петрова', '1995-03-12', '+79161234567', 'annaPetr@gmail.com');

select * from profiles

INSERT INTO profiles (user_id, selfname, surname, birthday, phone, email) VALUES
((select id from users where login = 'elena_trainer222'), 'Елена', 'Соколова', '1988-07-24', '+79162345678', 'ElenaSokol@mail.ru'),
((select id from users where login = 'maria_client333'), 'Мария', 'Иванова', '1992-11-05', '+79163456789', 'MariaIvan@ya.ru'),
((select id from users where login = 'dmitry_trainer444'), 'Дмитрий', 'Смирнов', '1985-09-18', '+79164567890', 'DmitrySmir@emal.edu'),
((select id from users where login = 'admin5_1'), 'Ольга', 'Админова', '1990-01-01', '+79165678901', 'admin1@mail.ru');


INSERT INTO workouts (workout_name, description, workout_type_id) VALUES
('Йога', 'Щадящие упражнения на силу и растяжку мышц с дыхательными упражнениями', 1),
('Пилатес', 'Укрепление мышц кора, осанка и гибкость', 1),
('Стретчинг', 'Растяжка всего тела, развитие гибкости', 1),
('Силовая', 'Тренировка с отягощениями для тонуса мышц', 2),
('Zumba', 'Танцевальная фитнес-программа под зажигательную музыку', 4);

TRUNCATE TABLE workouts RESTART IDENTITY CASCADE;

select * from workouts

INSERT INTO rooms (room_name, capacity) VALUES
('GREEN', 15),
('BLUE', 17),
('ORANGE', 20),
('PINK', 10);

select * from rooms


INSERT INTO schedule (
    workout_id, room_id, trainer_id, 
    schedule_date, start_time, end_time, 
    max_participants, current_participants, created_at
) VALUES
-- Йога с Еленой
(1, 1, (select id from users where login = 'elena_trainer222'), 
 '2026-03-02', '2026-03-02 10:00:00', '2026-03-02 11:00:00', 
 15, 3, '2026-02-20');  -- 3 записалось

 INSERT INTO schedule (
    workout_id, room_id, trainer_id, 
    schedule_date, start_time, end_time, 
    max_participants, current_participants, created_at
) VALUES
-- Пилатес с Еленой  
(2, 4, (select id from users where login = 'elena_trainer222'),
 '2026-03-02', '2026-03-02 18:00:00', '2026-03-02 19:00:00',
 10, 2, '2026-02-20'),

-- Силовая с Дмитрием
(4, 2, (select id from users where login = 'dmitry_trainer444'),
 '2026-03-03', '2026-03-03 19:00:00', '2026-03-03 20:30:00',
 12, 5, '2026-02-20'),

-- Йога с Еленой (завтра)
(1, 1, (select id from users where login = 'elena_trainer222'),
 '2026-03-03', '2026-03-03 09:00:00', '2026-03-03 10:00:00',
 15, 8, '2026-02-20'),

-- Zumba с Еленой
(5, 3, (select id from users where login = 'elena_trainer222'),
 '2026-03-04', '2026-03-04 19:00:00', '2026-03-04 20:00:00',
 20, 12, '2026-02-20'),

-- Пилатес с Дмитрием (Дмитрий тоже ведет пилатес)
(2, 4, (select id from users where login = 'dmitry_trainer444'),
 '2026-03-05', '2026-03-05 11:00:00', '2026-03-05 12:00:00',
 10, 0, '2026-02-20'),  -- никого нет 

-- Стретчинг с Еленой
(3, 1, (select id from users where login = 'elena_trainer222'),
 '2026-03-06', '2026-03-06 17:00:00', '2026-03-06 18:00:00',
 15, 10, '2026-02-20'),

-- Йога с Еленой (утренняя)
(1, 1, (select id from users where login = 'elena_trainer222'),
 '2026-03-07', '2026-03-07 08:00:00', '2026-03-07 09:00:00',
 15, 15, '2026-02-20'),
 
-- Силовая с Дмитрием
(4, 2, (select id from users where login = 'dmitry_trainer444'),
 '2026-03-08', '2026-03-08 20:00:00', '2026-03-08 21:00:00',
 12, 1, '2026-02-20'),

-- Йога с Еленой (вечерняя)
(1, 1, (select id from users where login = 'elena_trainer222'),
 '2026-03-09', '2026-03-09 19:00:00', '2026-03-09 20:00:00',
 15, 4, '2026-02-20');

select * from schedule s 

--Еще не вставлено

INSERT INTO bookings (schedule_id, client_id, status_id, created_at) VALUES
-- Анна записалась на йогу 2 марта
(1, '11111111-1111-1111-1111-111111111111', 1, '2026-02-26 10:00:00'),

-- Мария записалась на йогу 2 марта
(1, '33333333-3333-3333-3333-333333333333', 1, '2026-02-26 11:30:00'),

-- Анна записалась на вечерний пилатес 2 марта
(2, '11111111-1111-1111-1111-111111111111', 1, '2026-02-27 09:15:00'),

-- Анна записалась на утреннюю йогу 3 марта
(4, '11111111-1111-1111-1111-111111111111', 1, '2026-02-28 14:20:00'),

-- Мария записалась на утреннюю йогу 3 марта
(4, '33333333-3333-3333-3333-333333333333', 1, '2026-02-28 15:00:00'),

-- Анна записалась на Zumba
(5, '11111111-1111-1111-1111-111111111111', 1, '2026-02-28 08:30:00');

