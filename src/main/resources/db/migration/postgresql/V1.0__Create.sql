CREATE TABLE IF NOT EXISTS login (
    id       uuid PRIMARY KEY USING INDEX TABLESPACE universitaetspace,
    username varchar(20) NOT NULL UNIQUE USING INDEX TABLESPACE universitaetspace,
    password varchar(180) NOT NULL,
    rollen   varchar(32)
) TABLESPACE universitaetspace;

CREATE TABLE IF NOT EXISTS adresse (
    id        uuid PRIMARY KEY USING INDEX TABLESPACE universitaetspace,
    plz       char(5) NOT NULL CHECK (plz ~ '\d{5}'),
    ort       varchar(40) NOT NULL
) TABLESPACE universitaetspace;
CREATE INDEX IF NOT EXISTS adresse_plz_idx ON adresse(plz) TABLESPACE universitaetspace;

CREATE TABLE IF NOT EXISTS universitaet (
    id               uuid PRIMARY KEY USING INDEX TABLESPACE universitaetspace,
    version          integer NOT NULL DEFAULT 0,
    name             varchar(40) NOT NULL,
    email            varchar(40) NOT NULL UNIQUE USING INDEX TABLESPACE universitaetspace,
    gruendungsdatum  date CHECK (gruendungsdatum < current_date),
    homepage         varchar(40),
    adresse_id       uuid NOT NULL UNIQUE USING INDEX TABLESPACE universitaetspace REFERENCES adresse,
    username         varchar(20) NOT NULL REFERENCES login(username),
    erzeugt          timestamp NOT NULL,
    aktualisiert     timestamp NOT NULL
) TABLESPACE universitaetspace;

CREATE INDEX IF NOT EXISTS universitaet_name_idx ON universitaet(name) TABLESPACE universitaetspace;

CREATE TABLE IF NOT EXISTS fakultaet (
    id               uuid PRIMARY KEY USING INDEX TABLESPACE universitaetspace,
    name             varchar(40) NOT NULL,
    ansprechpartner  varchar(40) NOT NULL,
    dekan            varchar(40) NOT NULL,
    universitaet_id  uuid REFERENCES universitaet,
    idx              integer NOT NULL DEFAULT 0
) TABLESPACE universitaetspace;
CREATE INDEX IF NOT EXISTS fakultaet_universitaet_id_idx ON fakultaet(universitaet_id) TABLESPACE universitaetspace;
