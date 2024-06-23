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

--  docker compose exec postgres bash
--  psql --dbname=universitaet --username=universitaet [--file=/sql/V1.1__Insert.sql]

-- COPY mit CSV-Dateien erfordert den Pfad src/main/resources/...
-- Dieser Pfad existiert aber nicht im Docker-Image
-- https://www.postgresql.org/docs/current/sql-copy.html

-- Insert addresses
INSERT INTO adresse (id, plz, ort)
VALUES
    ('20000000-0000-0000-0000-000000000000', '12345', 'Stadt A'),
    ('20000000-0000-0000-0000-000000000001', '23456', 'Stadt B'),
    ('20000000-0000-0000-0000-000000000002', '34567', 'Stadt C');

-- Insert universities
INSERT INTO universitaet (id, version, name, email, gruendungsdatum, homepage, adresse_id, erzeugt, aktualisiert)
VALUES
    ('00000000-0000-0000-0000-000000000000', 0, 'Universität A', 'uni_a@example.com', '1950-01-01', 'https://www.uni-a.example.com', '20000000-0000-0000-0000-000000000000', '2024-05-27 00:00:00', '2024-05-27 00:00:00'),
    ('00000000-0000-0000-0000-000000000001', 0, 'Universität B', 'uni_b@example.com', '1960-01-01', 'https://www.uni-b.example.com', '20000000-0000-0000-0000-000000000001', '2024-05-27 00:00:00', '2024-05-27 00:00:00'),
    ('00000000-0000-0000-0000-000000000002', 0, 'Universität C', 'uni_c@example.com', '1970-01-01', 'https://www.uni-c.example.com', '20000000-0000-0000-0000-000000000002', '2024-05-27 00:00:00', '2024-05-27 00:00:00');

-- Insert faculties
INSERT INTO fakultaet (id, name, ansprechpartner, dekan, universitaet_id)
VALUES
    ('10000000-0000-0000-0000-000000000000', 'Informatik', 'Prof. Schmidt', 'Prof. Müller', '00000000-0000-0000-0000-000000000000'),
    ('10000000-0000-0000-0000-000000000001', 'Wirtschaftswissenschaften', 'Prof. Maier', 'Prof. Schmitz', '00000000-0000-0000-0000-000000000000'),
    ('10000000-0000-0000-0000-000000000002', 'Naturwissenschaften', 'Prof. Müller', 'Prof. Schmidt', '00000000-0000-0000-0000-000000000001'),
    ('10000000-0000-0000-0000-000000000003', 'Geisteswissenschaften', 'Prof. Schmitz', 'Prof. Maier', '00000000-0000-0000-0000-000000000001'),
    ('10000000-0000-0000-0000-000000000004', 'Medizin', 'Prof. Schmitz', 'Prof. Müller', '00000000-0000-0000-0000-000000000001'),
    ('10000000-0000-0000-0000-000000000005', 'Rechtswissenschaften', 'Prof. Schmitz', 'Prof. Maier', '00000000-0000-0000-0000-000000000002'),
    ('10000000-0000-0000-0000-000000000006', 'Design', 'Prof. Müller', 'Prof. Schmitz', '00000000-0000-0000-0000-000000000002');
