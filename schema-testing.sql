DELETE FROM reimbursement;
DELETE FROM users;

INSERT INTO users VALUES (
	DEFAULT,
	'bob',
	'123',
	'Bob',
	'Bobson',
	'bob@gmail.com',
	(SELECT ers_user_role_id FROM user_roles WHERE user_role = 'EMPLOYEE')
);

INSERT INTO users VALUES (
	DEFAULT,
	'emma',
	'123',
	'Emma',
	'Emerson',
	'emma@yahoo.com',
	(SELECT ers_user_role_id FROM user_roles WHERE user_role = 'EMPLOYEE')
);

INSERT INTO users VALUES (
	DEFAULT,
	'jbright',
	'123',
	'Jack',
	'Bright',
	'jbright@site19.net',
	(SELECT ers_user_role_id FROM user_roles WHERE user_role = 'FINANCEMAN')
);

INSERT INTO reimbursement VALUES (
	DEFAULT,
	19999,
	date '2000-01-01',
	date '2001-01-01',
	'There once was a man named Bright, who traveled much faster than light. He departed one day, in a reletive way, and arrived on the previous night.',
	NULL,
	(SELECT ers_users_id FROM users WHERE ers_username = 'bob'),
	(SELECT ers_users_id FROM users WHERE ers_username = 'jbright'),
	(SELECT reimb_status_id FROM reimbursement_status WHERE reimb_status = 'APPROVED'),
	(SELECT reimb_type_id FROM reimbursement_type WHERE reimb_type = 'TRAVEL')
);

INSERT INTO reimbursement VALUES (
	DEFAULT,
	999,
	current_timestamp,
	NULL,
	NULL,
	NULL,
	(SELECT ers_users_id FROM users WHERE ers_username = 'bob'),
	NULL,
	(SELECT reimb_status_id FROM reimbursement_status WHERE reimb_status = 'PENDING'),
	(SELECT reimb_type_id FROM reimbursement_type WHERE reimb_type = 'FOOD')
);
