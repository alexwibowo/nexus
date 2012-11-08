CREATE SEQUENCE endpoint_seq AS BIGINT START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE service_seq AS BIGINT START WITH 1 INCREMENT BY 1;

CREATE TABLE endpoint (
      id								NUMERIC(32,0)	NOT NULL,
      version							NUMERIC(32,0)	NOT NULL,
      created_date_time					TIMESTAMP		NOT NULL,
      updated_date_time					TIMESTAMP		NOT NULL,
      status							VARCHAR(20)		NOT NULL,
      uri								VARCHAR(30)NOT NULL,
      protocol							VARCHAR(20) NOT NULL
);
ALTER TABLE endpoint ADD CONSTRAINT endpoint_pk PRIMARY KEY (id);
ALTER TABLE endpoint ADD CONSTRAINT endpoint_uniqueness UNIQUE (uri);


CREATE TABLE service (
       id								NUMERIC(32,0)	NOT NULL,
      version							NUMERIC(32,0)	NOT NULL,
      created_date_time					TIMESTAMP		NOT NULL,
      updated_date_time					TIMESTAMP		NOT NULL,
      status							VARCHAR(20)		NOT NULL,
      description						VARCHAR(1024),
      namespace							VARCHAR(50)		NOT NULL,
      local_name						VARCHAR(30)		NOT NULL
);
ALTER TABLE service ADD CONSTRAINT service_pk PRIMARY KEY (id);
ALTER TABLE service ADD CONSTRAINT service_uniqueness UNIQUE (namespace,local_name) ;

CREATE TABLE service_endpoint (
    service_id							NUMERIC(32,0) 	NOT NULL,
    endpoint_id							NUMERIC(32,0) 	NOT NULL,
    status								VARCHAR(20)  	NOT NULL,
    created_date_time					TIMESTAMP		NOT NULL,
    updated_date_time					TIMESTAMP		NOT NULL
);
ALTER TABLE service_endpoint ADD CONSTRAINT service_endpoint_fk1 FOREIGN KEY (service_id)
            REFERENCES service(id) ON DELETE CASCADE;
ALTER TABLE service_endpoint ADD CONSTRAINT service_endpoint_fk2 FOREIGN KEY (endpoint_id)
            REFERENCES endpoint(id) ON DELETE CASCADE;





