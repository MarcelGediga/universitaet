-- noinspection SqlNoDataSourceInspectionForFile

-- Copyright (C) 2022 - present Marcel Gediga, Hochschule Karlsruhe
--
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with this program.  If not, see <https://www.gnu.org/licenses/>.

-- docker compose exec postgres bash
-- psql --dbname=universitaet --username=universitaet [--file=/sql/V1.0__Create.sql]

-- Indexe mit pgAdmin auflisten: "Query Tool" verwenden mit
--  SELECT   tablename, indexname, indexdef, tablespace
--  FROM     pg_indexes
--  WHERE    schemaname = 'universitaet'
--  ORDER BY tablename, indexname;

-- https://www.postgresql.org/docs/current/sql-createtable.html
-- https://www.postgresql.org/docs/current/datatype.html
-- BEACHTE: user ist ein Schluesselwort
CREATE TABLE IF NOT EXISTS login (
             -- https://www.postgresql.org/docs/current/datatype-uuid.html
             -- https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-PRIMARY-KEYS
             -- impliziter Index fuer Primary Key
    id       uuid PRIMARY KEY USING INDEX TABLESPACE universitaetspace,
             -- generierte Sequenz "login_id_seq":
    -- id    integer GENERATED ALWAYS AS IDENTITY(START WITH 1000) PRIMARY KEY USING INDEX TABLESPACE universitaetspace,
    username varchar(20) NOT NULL UNIQUE USING INDEX TABLESPACE universitaetspace,
    password varchar(180) NOT NULL,
    rollen   varchar(32)
) TABLESPACE universitaetspace;

-- https://www.postgresql.org/docs/current/sql-createtype.html
-- https://www.postgresql.org/docs/current/datatype-enum.html
-- CREATE TYPE geschlecht AS ENUM ('MAENNLICH', 'WEIBLICH', 'DIVERS');

CREATE TABLE IF NOT EXISTS adresse (
    id        uuid PRIMARY KEY USING INDEX TABLESPACE universitaetspace,
              -- https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-CHECK-CONSTRAINTS
    plz       char(5) NOT NULL CHECK (plz ~ '\d{5}'),
    ort       varchar(40) NOT NULL
) TABLESPACE universitaetspace;
CREATE INDEX IF NOT EXISTS adresse_plz_idx ON adresse(plz) TABLESPACE universitaetspace;

CREATE TABLE IF NOT EXISTS universitaet (
    id               uuid PRIMARY KEY USING INDEX TABLESPACE universitaetspace,
                     -- https://www.postgresql.org/docs/current/datatype-numeric.html#DATATYPE-INT
    version          integer NOT NULL DEFAULT 0,
    name             varchar(40) NOT NULL,
                     -- impliziter Index als B-Baum durch UNIQUE
                     -- https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-UNIQUE-CONSTRAINTS
    email            varchar(40) NOT NULL UNIQUE USING INDEX TABLESPACE universitaetspace,
                     -- https://www.postgresql.org/docs/current/datatype-datetime.html
    gruendungsdatum  date CHECK (gruendungsdatum < current_date),
    homepage         varchar(40),
    adresse_id       uuid NOT NULL UNIQUE USING INDEX TABLESPACE universitaetspace REFERENCES adresse,
    username         varchar(20) NOT NULL REFERENCES login(username),
                     -- https://www.postgresql.org/docs/current/datatype-datetime.html
    erzeugt          timestamp NOT NULL,
    aktualisiert     timestamp NOT NULL
) TABLESPACE universitaetspace;

-- default: btree
-- https://www.postgresql.org/docs/current/sql-createindex.html
CREATE INDEX IF NOT EXISTS universitaet_name_idx ON universitaet(name) TABLESPACE universitaetspace;

CREATE TABLE IF NOT EXISTS fakultaet (
    id               uuid PRIMARY KEY USING INDEX TABLESPACE universitaetspace,
                     -- https://www.postgresql.org/docs/current/datatype-numeric.html#DATATYPE-NUMERIC-DECIMAL
                     -- https://www.postgresql.org/docs/current/datatype-money.html
                     -- 10 Stellen, davon 2 Nachkommastellen
    name             varchar(40) NOT NULL,
    ansprechpartner  varchar(40) NOT NULL,
    dekan            varchar(40) NOT NULL,
    universitaet_id  uuid REFERENCES universitaet,
    idx              integer NOT NULL DEFAULT 0
) TABLESPACE universitaetspace;
CREATE INDEX IF NOT EXISTS fakultaet_universitaet_id_idx ON fakultaet(universitaet_id) TABLESPACE universitaetspace;
