-- INSERT INTO login
INSERT INTO login (id, username, password, rollen)
VALUES
    ('30000000-0000-0000-0000-000000000000', 'admin', '{argon2id}$argon2id$v=19$m=16384,t=3,p=1$QHb5SxDhddjUiGboXTc9S9yCmRoPsBejIvW/dw50DKg$WXZDFJowwMX5xsOun2BT2R3hv2aA9TSpnx3hZ3px59sTW0ObtqBwX7Sem6ACdpycArUHfxmFfv9Z49e7I+TI/g', 'ADMIN,USER,ACTUATOR'),
    ('30000000-0000-0000-0000-000000000001', 'user', '{argon2id}$argon2id$v=19$m=16384,t=3,p=1$QHb5SxDhddjUiGboXTc9S9yCmRoPsBejIvW/dw50DKg$WXZDFJowwMX5xsOun2BT2R3hv2aA9TSpnx3hZ3px59sTW0ObtqBwX7Sem6ACdpycArUHfxmFfv9Z49e7I+TI/g', 'USER');

-- INSERT INTO adresse
INSERT INTO adresse (id, plz, ort)
VALUES
    ('20000000-0000-0000-0000-000000000000', '12345', 'Karlsruhe'),
    ('20000000-0000-0000-0000-000000000001', '23456', 'Rastatt'),
    ('20000000-0000-0000-0000-000000000002', '34567', 'Berlin');

-- Insert INTO universities
INSERT INTO universitaet (id, version, name, email, gruendungsdatum, homepage, adresse_id, erzeugt, aktualisiert, username)
VALUES
    ('00000000-0000-0000-0000-000000000000', 0, 'Universit├ñt A', 'uni_a@example.com', '1950-01-01', 'https://www.uni-a.example.com', '20000000-0000-0000-0000-000000000000', '2024-05-27 00:00:00', '2024-05-27 00:00:00', 'admin'),
    ('00000000-0000-0000-0000-000000000001', 0, 'Universit├ñt B', 'uni_b@example.com', '1960-01-01', 'https://www.uni-b.example.com', '20000000-0000-0000-0000-000000000001', '2024-05-27 00:00:00', '2024-05-27 00:00:00', 'user'),
    ('00000000-0000-0000-0000-000000000002', 0, 'Universit├ñt C', 'uni_c@example.com', '1970-01-01', 'https://www.uni-c.example.com', '20000000-0000-0000-0000-000000000002', '2024-05-27 00:00:00', '2024-05-27 00:00:00', 'user');

-- INSERT INTO fakultaet
INSERT INTO fakultaet (id, name, ansprechpartner, dekan, universitaet_id)
VALUES
    ('10000000-0000-0000-0000-000000000000', 'Informatik', 'Prof. Schmidt', 'Prof. Müller', '00000000-0000-0000-0000-000000000000'),
    ('10000000-0000-0000-0000-000000000001', 'Wirtschaftswissenschaften', 'Prof. Maier', 'Prof. Schmitz', '00000000-0000-0000-0000-000000000000'),
    ('10000000-0000-0000-0000-000000000002', 'Naturwissenschaften', 'Prof. Müller', 'Prof. Schmidt', '00000000-0000-0000-0000-000000000001'),
    ('10000000-0000-0000-0000-000000000003', 'Geisteswissenschaften', 'Prof. Schmitz', 'Prof. Maier', '00000000-0000-0000-0000-000000000001'),
    ('10000000-0000-0000-0000-000000000004', 'Medizin', 'Prof. Schmitz', 'Prof. Müller', '00000000-0000-0000-0000-000000000001'),
    ('10000000-0000-0000-0000-000000000005', 'Rechtswissenschaften', 'Prof. Schmitz', 'Prof. Maier', '00000000-0000-0000-0000-000000000002'),
    ('10000000-0000-0000-0000-000000000006', 'Design', 'Prof. Müller', 'Prof. Schmitz', '00000000-0000-0000-0000-000000000002');
