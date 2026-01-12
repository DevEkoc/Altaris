DROP DATABASE IF EXISTS altaris;
CREATE DATABASE altaris;
USE altaris;

CREATE TABLE chaplain (
    chaplainId   int auto_increment primary key,
    name         varchar(50) not null,
    surname      varchar(50) null,
    phone        char(9) not null,
    priestlyRank enum ('DIACRE', 'EVEQUE', 'PRETRE') not null
);

CREATE TABLE unit (
    unitId     int auto_increment primary key,
    name    varchar(50) not null,
    description text not null,
    locality    varchar(255) null,
    saintPatron varchar(255) null,
    chaplain    int null,
    image       varchar(255) null,
    foreign key (chaplain) references chaplain (chaplainId)
);

CREATE TABLE province (
    archbishop  varchar(50) not null,
    headquarter varchar(50) not null,
    unitId        int not null  primary key,
    foreign key (unitId) references unit (unitId)
);

CREATE TABLE diocese (
    bishop          varchar(50) not null,
    retiredBishop   varchar(50) null,
    type            enum ('ARCHIDIOCESE', 'SUFFRAGANT') not null,
    unitId          int not null primary key,
    provinceId      int not null,
    foreign key (unitId) references unit (unitId),
    foreign key (provinceId) references province (unitId)
);

CREATE TABLE zone (
    episcopalVicar varchar(50) not null,
    unitId         int not null primary key,
    dioceseId      int not null,
    foreign key (unitId) references unit (unitId),
    foreign key (dioceseId) references diocese (unitId)
);

CREATE TABLE parish (
    priest  varchar(50) not null,
    type    enum ('BASILIQUE', 'CATHEDRALE', 'CENTRE_EUCHARISTIQUE', 'PAROISSE', 'PAROISSE_UNIVERSITAIRE', 'QUASI_PAROISSE', 'SANCTUAIRE') not null,
    unitId  int primary key,
    zoneId  int not null,
    foreign key (unitId) references unit (unitId),
    foreign key (zoneId) references zone (unitId)
);

create table office (
    id                      int auto_increment primary key,
    active                  boolean default false,
    creationDate            date not null,
    description             text not null,
    ecclesiasticalLevel     enum ('DIOCESE', 'PARISH', 'PROVINCE', 'ZONE') not null,
    unitId                  int not null,
    unique (unitId),
    foreign key (unitId) references unit (unitId)
);

CREATE TABLE servant (
    id              int auto_increment primary key,
    serialNumber    varchar(255) null,
    name            varchar(50) not null,
    surname         varchar(50) null,
    birthDate       date not null,
    entryDate       date null,
    gender          enum ('F', 'M') not null,
    grade           enum ('ACOLYTE', 'ASSISTANT', 'CEREMONIAIRE', 'CEROFERAIRE', 'ENCADRANT', 'NOVICE', 'THURIFERAIRE') null,
    image           varchar(255) null,
    phone           varchar(255) not null,
    parishId        int not null,
    unique (serialNumber),
    foreign key (parishId) references parish (unitId)
);

CREATE TABLE assignment (
    id          int auto_increment primary key,
    missions    text not null,
    position    varchar(50) not null,
    officeId    int not null,
    servantId   int not null,
    foreign key (officeId) references office (id),
    foreign key (servantId) references servant (id)
);