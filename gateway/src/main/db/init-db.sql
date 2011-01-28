CREATE TABLESPACE nexus_config DATAFILE '/usr/lib/oracle/xe/oradata/XE/nexus/nexus_config.dbf' SIZE 50M;

CREATE USER nexus
IDENTIFIED BY partnering
DEFAULT TABLESPACE nexus_config;
GRANT CONNECT TO nexus;

-- QUOTAS
ALTER USER nexus QUOTA UNLIMITED ON nexus_config;


CREATE SEQUENCE endpoint_seq MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 CACHE 10 NOORDER NOCYCLE;
CREATE SEQUENCE service_seq MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 CACHE 10 NOORDER NOCYCLE;

CREATE TABLE endpoint (
      id                                                  NUMBER(32,0) NOT NULL,
      version                                       NUMBER(32,0) NOT NULL,
      created_date_time               TIMESTAMP    NOT NULL,
      updated_date_time             TIMESTAMP     NOT NULL,
      status                                        VARCHAR2(20)  NOT NULL,
      uri                                                 VARCHAR2(30) NOT NULL,
      protocol                                    VARCHAR2(20) NOT NULL
) NOCOMPRESS NOLOGGING TABLESPACE nexus_config;
ALTER TABLE endpoint ADD CONSTRAINT endpoint_pk PRIMARY KEY (id)
                            USING INDEX PCTFREE 10
                            INITRANS 2
                            MAXTRANS 255
                            NOLOGGING COMPUTE STATISTICS TABLESPACE nexus_config;
ALTER TABLE endpoint ADD CONSTRAINT endpoint_uniqueness UNIQUE (uri);


CREATE TABLE service (
       id                                                  NUMBER(32,0) NOT NULL,
      version                                       NUMBER(32,0) NOT NULL,
      created_date_time               TIMESTAMP    NOT NULL,
      updated_date_time             TIMESTAMP     NOT NULL,
      status                                        VARCHAR2(20)  NOT NULL,
      description                              VARCHAR(1024),
      namespace                               VARCHAR2(50) NOT NULL,
      local_name                               VARCHAR2(30) NOT NULL
) NOCOMPRESS NOLOGGING TABLESPACE nexus_config;
ALTER TABLE service ADD CONSTRAINT service_pk PRIMARY KEY (id)
                            USING INDEX PCTFREE 10
                            INITRANS 2
                            MAXTRANS 255
                            NOLOGGING COMPUTE STATISTICS TABLESPACE nexus_config ENABLE;
ALTER TABLE service ADD CONSTRAINT service_uniqueness UNIQUE (namespace,local_name) ;

CREATE TABLE service_endpoint (
    service_id                                  NUMBER(32,0) NOT NULL,
    endpoint_id                             NUMBER(32,0) NOT NULL,
    status                                        VARCHAR2(20)  NOT NULL,
    created_date_time               TIMESTAMP    NOT NULL,
    updated_date_time             TIMESTAMP     NOT NULL
)  NOCOMPRESS NOLOGGING TABLESPACE nexus_config;
ALTER TABLE service_endpoint ADD CONSTRAINT service_endpoint_fk1 FOREIGN KEY (service_id)
            REFERENCES service(id) ;
ALTER TABLE service_endpoint ADD CONSTRAINT service_endpoint_fk2 FOREIGN KEY (endpoint_id)
            REFERENCES endpoint(id);





