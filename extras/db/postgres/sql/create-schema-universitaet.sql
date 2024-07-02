-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS universitaet AUTHORIZATION universitaet;

-- Alter role to set search path
ALTER ROLE universitaet SET search_path TO universitaet, public;
