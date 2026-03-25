
create table roles
(
	id serial primary key not null,
	role_name character varying (50) not null
);

create table subscription_statuses
(
	id serial primary key not null,
	subscription_status_name character varying (50) not null
);

create table payment_statuses
(
	id serial primary key not null,
	payment_status_name character varying (50) not null
);

create table booking_statuses
(
	id serial primary key not null,
	booking_status_name character varying (50) not null
);

create table workouts
(
	id serial primary key not null,
	workout_name character varying (70) not null,
	description character varying (200)
);

create table rooms
(
	id serial primary key not null,
	room_name character varying (50) not null,
	capacity int not null
);

create table subscriptions
(
	id serial primary key not null,
	subscription_name character varying (50) not null,
	price money not null,
	duration_days int,
	visits_count int,
	constraint check_visits_count check (visits_count > 0)
);

create table users
(
	id uuid primary key DEFAULT gen_random_uuid() not null,
	role_id int not null,
	login char varying (200) unique not null,
	password_hash char varying (200) unique,
	create_at date not null,
	is_active bool not null,
	CONSTRAINT fk_users_role 
        FOREIGN KEY (role_id) REFERENCES roles(id) 
        ON UPDATE CASCADE 
        ON DELETE RESTRICT
);

create table profiles
(
	id serial primary key,
	user_id uuid not null,
	constraint fk_profiles_user
	foreign key (user_id) references users(id)
		ON UPDATE CASCADE 
        ON DELETE CASCADE,
	surname char varying (100) not null,
	selfname char varying (100) not null,
	patronymic char varying (100),
	birthday date not null,
	phone char varying (20) not null,
	email char varying (100) unique not null,
	constraint check_age 
	check (birthday < CURRENT_DATE - INTERVAL '12 years'),
	constraint check_phone_format 
	check(phone ~ '^\+?[0-9]{10,15}$'),
	constraint check_email_format 
	check (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

create table payments
(
	id serial primary key,
	client_id uuid not null,
	constraint fk_payments_users
	foreign key (client_id) references users (id)
		ON UPDATE CASCADE 
        ON DELETE set null,
	amount money not null,
	payment_status int not null,
	constraint fk_payments_pay_statuses
	foreign key (payment_status) references payment_statuses (id)
		ON UPDATE CASCADE 
        ON DELETE restrict,
	transaction_date date not null
	
);

create table shedule
(
	id serial primary key not null,
	workout_id int not null,
	room_id int not null,
	trainer_id uuid not null,
	shedule_date date not null,
	start_time timestamp not null,
	end_time timestamp not null,
	constraint check_times
	check (end_time > start_time),
	max_participants int not null,
	constraint fk_shedule_workout
	foreign key (workout_id) references workouts (id)
		ON UPDATE CASCADE 
        ON DELETE cascade,
    constraint fk_shedule_rooms
    foreign key (room_id) references rooms (id)
		ON UPDATE CASCADE 
        ON DELETE cascade,
    constraint fk_shedule_users
    foreign key (trainer_id) references users (id)
		ON UPDATE CASCADE 
        ON DELETE restrict	
);

alter table shedule 
add column current_participants int not null;

alter table shedule 
add column created_at date not null;

alter table shedule
add constraint check_current_participants
check (current_participants <= max_participants);

create table client_subscriptions
(
	id serial primary key not null,
	client_id uuid,
	subscription_id int not null,
	start_date date not null,
	end_date date not null,
	constraint check_dates_format
	check (end_date > start_date),
	remaining_visits int not null,
	subscription_status int,
	
	constraint fk_client_subscriptions_users
	foreign key (client_id) references users(id)
		ON UPDATE CASCADE 
        ON DELETE set null,
	constraint fk_client_subscriptions_subscriptions
	foreign key (subscription_id) references subscriptions(id)
		ON UPDATE CASCADE 
        ON DELETE restrict,
	constraint fk_client_subscriptions_subscription_statuses
	foreign key (subscription_status) references subscription_statuses(id)
		ON UPDATE CASCADE 
        ON DELETE restrict
);

create table bookings 
(
	id serial not null,
	shedule_id int not null,
	client_id uuid,
	status_id int not null,
	created_at date not null,
	constraint check_creation_date
	check (created_at <= CURRENT_DATE),
	
	constraint fk_bookings_shedule
	foreign key (shedule_id) references shedule (id)
		ON UPDATE CASCADE 
        ON DELETE cascade,
    constraint fk_bookings_users
	foreign key (client_id) references users (id)
		ON UPDATE CASCADE 
        ON DELETE set null,
    constraint fk_bookings_booking_statuses
	foreign key (status_id) references booking_statuses (id)
		ON UPDATE CASCADE 
        ON DELETE restrict
	
);

--alter table bookings
--drop constraint check_creation_date


select * from bookings




