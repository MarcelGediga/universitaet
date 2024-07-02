CREATE TABLE IF NOT EXISTS login (
                                     id       UUID PRIMARY KEY,
                                     username VARCHAR(20) NOT NULL UNIQUE,
                                     password VARCHAR(180) NOT NULL,
                                     rollen   VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS adresse (
                                       id   UUID PRIMARY KEY,
                                       plz  CHAR(5) NOT NULL CHECK (plz ~ '\d{5}'),
                                       ort  VARCHAR(40) NOT NULL
);
CREATE INDEX IF NOT EXISTS adresse_plz_idx ON adresse(plz);

CREATE TABLE IF NOT EXISTS universitaet (
                                            id               UUID PRIMARY KEY,
                                            version          INTEGER NOT NULL DEFAULT 0,
                                            name             VARCHAR(40) NOT NULL,
                                            email            VARCHAR(40) NOT NULL UNIQUE,
                                            gruendungsdatum  DATE CHECK (gruendungsdatum < CURRENT_DATE),
                                            homepage         VARCHAR(40),
                                            adresse_id       UUID NOT NULL UNIQUE REFERENCES adresse(id),
                                            username         VARCHAR(20) NOT NULL REFERENCES login(username),
                                            erzeugt          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            aktualisiert     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS universitaet_name_idx ON universitaet(name);

CREATE TABLE IF NOT EXISTS fakultaet (
                                         id               UUID PRIMARY KEY,
                                         name             VARCHAR(40) NOT NULL,
                                         ansprechpartner  VARCHAR(40) NOT NULL,
                                         dekan            VARCHAR(40) NOT NULL,
                                         universitaet_id  UUID REFERENCES universitaet(id),
                                         idx              INTEGER NOT NULL DEFAULT 0
);
CREATE INDEX IF NOT EXISTS fakultaet_universitaet_id_idx ON fakultaet(universitaet_id);
