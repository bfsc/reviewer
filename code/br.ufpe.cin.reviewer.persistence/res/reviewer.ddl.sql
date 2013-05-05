--------------------------------------------------------------------------------
--									DDL SCRIPT							      --
--------------------------------------------------------------------------------


-- --------------------------------  SCHEMAS -------------------------------- --

CREATE SCHEMA reviewer AUTHORIZATION sa;


-- --------------------------------  TABLES --------------------------------- --

CREATE TABLE reviewer.study(
	study_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	code VARCHAR(1000),
	title VARCHAR(5000),
	source VARCHAR(1000),
	abstract VARCHAR(50000),
	year VARCHAR(4),
	url VARCHAR(10000),
);

CREATE TABLE reviewer.study_authors(
	study_id INTEGER,
	author VARCHAR(10000),
	CONSTRAINT authors_study_fk FOREIGN KEY(study_id) REFERENCES reviewer.study(study_id)
);

CREATE TABLE reviewer.study_institutions(
	study_id INTEGER,
	institution VARCHAR(10000),
	CONSTRAINT institutions_study_fk FOREIGN KEY(study_id) REFERENCES reviewer.study(study_id)
);

CREATE TABLE reviewer.study_countries(
	study_id INTEGER,
	country VARCHAR(10000),
	CONSTRAINT countries_study_fk FOREIGN KEY(study_id) REFERENCES reviewer.study(study_id)
);

CREATE TABLE reviewer.literature_review(
	literature_review_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	title VARCHAR(5000)
);

CREATE TABLE reviewer.literature_review_studies(
	study_id INTEGER,
	literature_review_id INTEGER,
	CONSTRAINT lrs_study_fk FOREIGN KEY(study_id) REFERENCES reviewer.study(study_id),
	CONSTRAINT lrs_literature_review_fk FOREIGN KEY(literature_review_id) REFERENCES reviewer.literature_review(literature_review_id)
);

--CREATE TABLE reviewer.literature_review_source_data(
--	source_data_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
--	title VARCHAR(5000)
--);
