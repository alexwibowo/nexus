CREATE TABLE service (
  id                                              NUMBER(32) NOT NULL,
  uri                                            VARCHAR2(500) NOT NULL,
  description                          VARCHAR2(1024),
  updated_date_time         TIMESTAMP(6),
  version                                   NUMBER(10)
);

CREATE SEQUENCE service_seq;
ALTER TABLE service ADD CONSTRAINT service_pk PRIMARY KEY(id);


CREATE TABLE endpoint(
  id                                              NUMBER(32) NOT NULL,
  uri                                             VARCHAR2(512) NOT NULL,
  description                           VARCHAR2(1024),
  updated_date_time         TIMESTAMP(6),
  version                                   NUMBER(10)
);

CREATE SEQUENCE endpoint_seq;
ALTER TABLE endpoint ADD CONSTRAINT endpoint_pk PRIMARY KEY(id);