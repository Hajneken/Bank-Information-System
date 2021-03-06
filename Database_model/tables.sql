
CREATE TABLE Klient (
	ID INT PRIMARY KEY
);

CREATE TABLE Ucet ( 
	ID INT PRIMARY KEY,
	ID_user INT REFERENCES Klient(ID)
);

CREATE TABLE Konto (
	ID_ucet INT PRIMARY KEY REFERENCES Ucet(ID),
	Typ VARCHAR (20) NOT NULL,
	Zustatek DECIMAL NOT NULL
);

CREATE TABLE Uver (
	ID_ucet INT PRIMARY KEY REFERENCES Ucet(ID),
	Hodnota INT NOT NULL,
	Typ VARCHAR(20) NOT NULL,
	Urok DECIMAL NOT NULL
);

CREATE TABLE Transakce (
	ID INT PRIMARY KEY,
	Castka DECIMAL NOT NULL,
	Datum_podani DATE NOT NULL,
	Datum_prevedeni DATE NOT NULL,
	ID_Platce INT REFERENCES Ucet(ID),
	ID_Prijemce INT REFERENCES Ucet(ID),
	Provedeno BOOLEAN NOT NULL
);

CREATE TABLE Fyzicka_osoba (
	ID_klient INT PRIMARY KEY REFERENCES Klient(ID),
	Jmeno VARCHAR(50)  NOT NULL,
	Prijmeni VARCHAR(50) NOT NULL,
	Age INT NOT NULL,
	CONSTRAINT CHK_Person CHECK (Age>=18),  
	Bydliste VARCHAR(100) NOT NULL
	
);


CREATE TABLE Pravnicka_osoba (
	ID_klient INT PRIMARY KEY REFERENCES Klient(ID),
	ICO INT NOT NULL,
	Nazev_firmy VARCHAR(50) NOT NULL,
	Sidlo VARCHAR(100) NOT NULL
);

CREATE TABLE Platebni_karta (
	ID INT PRIMARY KEY,
	ID_user INT REFERENCES Klient(ID),
	Cislo INT NOT NULL,
	CVV INT NOT NULL,
	Datum_vydani DATE NOT NULL, 
	Datum_vyprseni DATE NOT NULL
);

CREATE TABLE Debitni_karta (
	ID_platebni_karta INT PRIMARY KEY REFERENCES Platebni_karta(ID),
	ID_ucet INT REFERENCES Ucet(ID)
);

CREATE TABLE Kreditni_karta (
	ID_platebni_karta INT PRIMARY KEY REFERENCES Platebni_karta(ID),
	Limit_cerpani INT NOT NULL
);


