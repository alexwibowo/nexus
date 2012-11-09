CREATE ROLE nexus LOGIN
  ENCRYPTED PASSWORD 'md52cd86dc7365246100ec4cfcb8450cbe4'
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;

 CREATE DATABASE nexus
 WITH OWNER = nexus
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'en_AU.utf8'
       LC_CTYPE = 'en_AU.utf8'
       CONNECTION LIMIT = -1;
GRANT CONNECT, TEMPORARY ON DATABASE nexus TO public;
GRANT ALL ON DATABASE nexus TO nexus;


CREATE SEQUENCE endpoint_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE endpoint_seq OWNER TO nexus;


CREATE SEQUENCE service_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE endpoint_seq OWNER TO nexus;


CREATE TABLE endpoint(
      id                                                  NUMERIC(32,0) NOT NULL PRIMARY KEY,
      version                                       NUMERIC(32,0) NOT NULL,
      created_date_time               TIMESTAMP  with time zone  NOT NULL,
      updated_date_time             TIMESTAMP  with time zone   NOT NULL,
      status                                        VARCHAR(20)  NOT NULL,
      uri                                                 VARCHAR(30) NOT NULL,
      protocol                                    VARCHAR(20) NOT NULL,
      UNIQUE(uri)
);

CREATE TABLE service (
       id                                                  NUMERIC(32,0) NOT NULL PRIMARY KEY,
      version                                       NUMERIC(32,0) NOT NULL,
      created_date_time               TIMESTAMP   with time zone   NOT NULL,
      updated_date_time             TIMESTAMP    with time zone  NOT NULL,
      status                                        VARCHAR(20)  NOT NULL,
      description                              VARCHAR(1024),
      namespace                               VARCHAR(50) NOT NULL,
      local_name                               VARCHAR(30) NOT NULL ,
      UNIQUE(namespace, local_name)
);

CREATE TABLE service_endpoint(
    service_id                                  NUMERIC(32,0) NOT NULL REFERENCES service(id) PRIMARY KEY,
    endpoint_id                             NUMERIC(32,0) NOT NULL REFERENCES endpoint(id) PRIMARY KEY,
    status                                        VARCHAR(20)  NOT NULL,
    created_date_time               TIMESTAMP    with time zone    NOT NULL,
    updated_date_time             TIMESTAMP     with time zone    NOT NULL,
    UNIQUE(service_id, endpoint_id)
);






