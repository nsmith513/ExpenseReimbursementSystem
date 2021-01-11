-- lookup tables
CREATE TABLE reimbursement_status (
	reimb_status_id serial PRIMARY KEY,
	reimb_status varchar(10) NOT NULL
);

CREATE TABLE reimbursement_type (
	reimb_type_id serial PRIMARY KEY,
	reimb_type varchar(10) NOT NULL
);

CREATE TABLE user_roles (
	ers_user_role_id serial PRIMARY KEY,
	user_role varchar(10) NOT NULL
);

-- values for lookup tables, these will not change
INSERT INTO reimbursement_status VALUES (0, 'PENDING');
INSERT INTO reimbursement_status VALUES (1, 'APPROVED');
INSERT INTO reimbursement_status VALUES (2, 'DENIED');

INSERT INTO reimbursement_type VALUES (0, 'LODGING');
INSERT INTO reimbursement_type VALUES (1, 'TRAVEL');
INSERT INTO reimbursement_type VALUES (2, 'FOOD');
INSERT INTO reimbursement_type VALUES (3, 'OTHER');

INSERT INTO user_roles VALUES (0, 'EMPLOYEE');
INSERT INTO user_roles VALUES (1, 'FINANCEMAN');

-- users table
CREATE TABLE users (
	ers_users_id serial PRIMARY KEY,
	ers_username varchar(50) UNIQUE NOT NULL,
	ers_password varchar(50) NOT NULL,
	user_first_name varchar(100) NOT NULL,
	user_last_name varchar(100) NOT NULL,
	user_email varchar(150) UNIQUE NOT NULL,
	user_role_id integer NOT NULL,
	FOREIGN KEY (user_role_id) REFERENCES user_roles(ers_user_role_id)
);
CREATE INDEX users__unv1
ON users (ers_username, user_email);

-- reimbursment table
CREATE TABLE reimbursement (
	reimb_id serial PRIMARY KEY,
	reimb_amount bigint NOT NULL, -- bigint as opposed to int because it will represnt amount in cents
	reimb_submitted timestamp NOT NULL,
	reimb_resolved timestamp,
	reimb_description varchar(250), -- when getting this clientside, enforce char limit
	reimb_receipt bytea, -- this is optional, only put null into this until mvp is done
	reimb_author integer NOT NULL,
	reimb_resolver integer,
	reimb_status_id integer NOT NULL,
	reimb_type_id integer NOT NULL,
	FOREIGN KEY (reimb_author) REFERENCES users(ers_users_id),
	FOREIGN KEY (reimb_resolver) REFERENCES users(ers_users_id),
	FOREIGN KEY (reimb_status_id) REFERENCES reimbursement_status(reimb_status_id),
	FOREIGN KEY (reimb_type_id) REFERENCES reimbursement_type(reimb_type_id)
);

-- reimbursment view with author and resolver names
CREATE VIEW reimb_detailed AS
SELECT
	r.reimb_id AS id,
	r.reimb_amount AS amount,
	r.reimb_submitted AS submitted,
	r.reimb_resolved AS resloved,
	r.reimb_description AS description,
	r.reimb_receipt AS receipt,
	r.reimb_author AS author,
	u1.user_first_name AS author_first_name,
	u1.user_last_name AS author_last_name,
	r.reimb_resolver AS resolver,
	u2.user_first_name AS resolver_first_name,
	u2.user_last_name AS resolver_last_name,
	r.reimb_status_id AS status_id,
	r.reimb_type_id AS type_id
FROM reimbursement r
LEFT JOIN users u1
ON r.reimb_author = u1.ers_users_id
LEFT JOIN users u2
ON r.reimb_resolver = u2.ers_users_id;
