CREATE DATABASE bdHoteles;
USE bdHoteles;
CREATE TABLE hoteles (
	codHotel CHARACTER(6),
    nomHotel VARCHAR(60),
    CONSTRAINT pk_hoteles PRIMARY KEY (codHotel)
);

CREATE TABLE habitaciones (
	codHotel CHARACTER(6),
    numHabitacion CHARACTER(4),
    capacidad TINYINT DEFAULT 2,
    preciodia INTEGER,
    activa BOOLEAN DEFAULT 0,
    CONSTRAINT pk_habitaciones PRIMARY KEY (numHabitacion, codHotel),
    CONSTRAINT fk_habitaciones_codHotel FOREIGN KEY (codHotel) REFERENCES hoteles(codHotel)
);

CREATE TABLE regimenes (
	codregimen INTEGER AUTO_INCREMENT,
    codHotel CHARACTER(6),
    tipo CHARACTER(2) CHECK (tipo = 'DY' OR tipo = 'MD' OR tipo = 'PC' OR tipo = 'TD'),
    precio INTEGER,
    CONSTRAINT pk_regimenes PRIMARY KEY (codregimen),
    CONSTRAINT fk_regimenes_codHotel FOREIGN KEY (codHotel) REFERENCES hoteles(codHotel)
);

CREATE TABLE clientes (
	coddnionie CHARACTER (9),
    nombre VARCHAR(50),
    nacionalidad VARCHAR(40),
    CONSTRAINT pk_clientes PRIMARY KEY (coddnionie)
);

CREATE TABLE estancias (
    codestancia INTEGER AUTO_INCREMENT,
	coddnionie CHARACTER (9),
    codHotel CHARACTER(6),
    numHabitacion CHARACTER(4),
    fechaInicio DATE,
    fechaFin DATE,
    codregimen INTEGER,
    ocupantes TINYINT DEFAULT 2,
    precioestancia INTEGER,
    pagado BOOLEAN DEFAULT 1,
    CONSTRAINT pk_estancias PRIMARY KEY (codEstancia),
    CONSTRAINT fk_estancias_coddnieonie FOREIGN KEY (coddnionie) REFERENCES clientes(coddnionie),
    CONSTRAINT fk_estancias_codHotel FOREIGN KEY (codHotel) REFERENCES habitaciones(codHotel)
);

INSERT INTO hoteles VALUES ("A00", "Mariachi");
INSERT INTO hoteles VALUES ("B01", "El arbol");
INSERT INTO hoteles VALUES ("C02", "Sueños");
INSERT INTO hoteles VALUES ("D03", "VIP");
INSERT INTO hoteles VALUES ("E04", "Residente");

INSERT INTO hoteles VALUES ("Camb00", "El resplandor");
INSERT INTO hoteles VALUES ("Camb01", "Ibis");
INSERT INTO hoteles VALUES ("Camb02", "Frenesí");
INSERT INTO hoteles VALUES ("Camb03", "Tritón");
INSERT INTO hoteles VALUES ("Camb41", "Malva");

INSERT INTO habitaciones VALUES ("A00", "500", default, 50, default);
INSERT INTO habitaciones VALUES ("A00", "501", 4, 60, 1);
INSERT INTO habitaciones VALUES ("B01", "402", 5, 30, default);
INSERT INTO habitaciones VALUES ("B01", "403", default, 80, 1);
INSERT INTO habitaciones VALUES ("C02", "304", 1, 70, default);
INSERT INTO habitaciones VALUES ("C02", "305", default, 40, 1);
INSERT INTO habitaciones VALUES ("D03", "206", 4, 90, default);
INSERT INTO habitaciones VALUES ("D03", "207", default, 30, 1);
INSERT INTO habitaciones VALUES ("E04", "108", 3, 20, default);
INSERT INTO habitaciones VALUES ("E04", "109", default, 100, 1);

INSERT INTO regimenes(codHotel, tipo, precio) VALUES ("A00", "DY", 500);

INSERT INTO clientes VALUES ("20304050G", "Alejandro", "Española");
INSERT INTO clientes VALUES ("87468463H", "Cristina", "Española");
INSERT INTO clientes VALUES ("47987454J", "Yulisa", "Rumana");
INSERT INTO clientes VALUES ("98798649Y", "Kevin", "Rumana");
INSERT INTO clientes VALUES ("87987468P", "Hector", "Cubana");
INSERT INTO clientes VALUES ("74987788G", "Mariola", "Cubana");
INSERT INTO clientes VALUES ("25496879L", "Marlen", "Angola");
INSERT INTO clientes VALUES ("56498779T", "Heidi", "Angola");
INSERT INTO clientes VALUES ("65496879R", "Sara", "Argentina");
INSERT INTO clientes VALUES ("87986748B", "Felix", "Argentina");

INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ("20304050G", "A00", "501", "2021/01/01", "2021/01/20", 1, default, 500, default);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ("20304050G", "A00", "500", "2021/01/20", "2021/01/30", 2, default, 600, default);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ("87468463H", "B01", "402", "2021/02/01", "2021/02/20", 1, default, 300, 0);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ("87468463H", "B01", "403", "2021/03/01", "2021/03/15", 1, default, 420, 0);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ("87468463H", "C02", "304", "2021/03/16", "2021/03/27", 1, default, 530, default);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ("87987468P", "C02", "305", "2021/04/01", "2021/04/15", 1, default, 690, default);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ("74987788G", "D03", "206", "2021/04/16", "2021/04/28", 1, default, 730, 0);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ("65496879R", "D03", "207", "2021/05/01", "2021/05/20", 1, default, 455, default);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ("98798649Y", "E04", "108", "2021/06/15", "2021/06/25", 1, default, 322, default);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ("56498779T", "E04", "109", "2021/07/10", "2021/07/22", 1, default, 800, default);

/* Crear un procedimiento almacenado que liste todas las habitaciones de cierto hotel pasando por
parámetro de entrada el nombre del hotel, ordenados por preciodia y capacidad en orden ascendente.
Los datos a visualizar serán: número de habitacion, capacidad, precio y activo.*/

DELIMITER $$
CREATE PROCEDURE lista_habitaciones_nomHotel(IN nomHotel VARCHAR(60))
BEGIN
	SELECT numHabitacion, capacidad, preciodia, activa FROM habitaciones 
	INNER JOIN hoteles on hoteles.codHotel=habitaciones.codHotel
	WHERE hoteles.nomHotel = nomHotel 
	ORDER BY preciodia, capacidad ASC;
END;$$
DELIMITER ;

/*Crear un procedimiento almacenado que inserte una habitacion, de modo que se le pase como
parámetros todos los datos.
Comprobar que el código de hotel pasado exista en la tabla hoteles. En caso de que no exista el hotel
que no se inserte.
Devolver en un parámetro de salida: 0 si el hotel no existe y 1 si el hotel existe.
Devolver en otro parámetro de salida: 0 si la habitacion no se insertó y 1 si la inserción fue correcta.*/

DELIMITER $$
CREATE PROCEDURE insertarHabitacion(IN codHotel CHARACTER(6), IN numHabitacion CHARACTER (4), 
	IN capacidad TINYINT, IN preciodia INTEGER, IN activa BOOLEAN, OUT salida1 INTEGER, OUT salida2 INTEGER)
BEGIN
	IF ((SELECT COUNT(*) from hoteles WHERE hoteles.codHotel = codHotel) > 0) THEN
		INSERT INTO habitaciones VALUES (codHotel, numHabitacion, capacidad, preciodia, activa);
		SET salida1=1;
		SET salida2=1;
	ELSE 
		SET salida1=0;
		SET salida2=0;
	END IF;
END$$
DELIMITER ;

/*Crear un procedimiento almacenado que indicándole un nombre de hotel y un preciodia, devuelva
dos parámetros. En un parámetro de salida la cantidad de habitaciones total que tiene ese hotel. En
otro parámetro de salida la cantidad de habitaciones por debajo del preciodia y sean activas.*/

DELIMITER $$
CREATE PROCEDURE cantidadHabitaciones(IN codHotel CHARACTER(6), IN nombreHotel VARCHAR(60), 
									  IN preciod INTEGER, OUT salida1 INTEGER, OUT salida2 INTEGER)
BEGIN
	SET salida1=(SELECT COUNT(numHabitacion) from habitaciones
	INNER JOIN hoteles on hoteles.codHotel=habitaciones.codHotel
	WHERE  hoteles.codHotel=codHotel and hoteles.nomHotel=nombreHotel);
	
	SET salida2 = (SELECT COUNT(numHabitacion) from habitaciones 
	INNER JOIN hoteles ON hoteles.codHotel = habitaciones.codHotel
	WHERE hoteles.nomHotel = nombreHotel AND habitaciones.preciodia < preciod and activa = 0);
END $$
DELIMITER ;

/* Crear una función que dándole un dni o nie de un cliente como parámetro de entrada nos devuelva la
 suma total pagada por dicho cliente.*/
 
DELIMITER $$
CREATE FUNCTION sumaTotalEstancias(coddni CHARACTER(9)) 
RETURNS INTEGER
BEGIN
  DECLARE sumaTotal INTEGER;
  SELECT sum(precioestancia) INTO sumaTotal FROM estancias WHERE coddnionie = coddni;
  RETURN sumaTotal;
END; $$
DELIMITER ;


-- Hacer un Trigger en la tabla estancias que al insertar o modificar el numero de ocupantes no supere la capacidad de la habitación.

DELIMITER $$
CREATE TRIGGER tr_insertUpdate_numOcupantesEstancias
BEFORE INSERT
ON ESTANCIAS
FOR EACH ROW
	BEGIN
		DECLARE capacidad1 INTEGER;
		SET capacidad1 = (SELECT capacidad FROM habitaciones WHERE new.codHotel=habitaciones.codHotel and new.numHabitacion=habitaciones.numHabitacion);
		IF(new.ocupantes > capacidad1) THEN
	         SIGNAL sqlstate '45000' set message_text = 'No se hizo la inserción';
		END IF;
	END $$
DELIMITER ;


/*Hacer un Trigger en la tabla estancias que al insertar calcule el precio de la estancia en función del número de días de la estancia, precio día habitación, ocupantes y precio 
del régimen (precioestancia=(númerodedias*(preciodiahabitacion+ocupantes*precioregimen))). */
DELIMITER $$
CREATE TRIGGER tr_insert_precioEstancia
BEFORE INSERT
ON ESTANCIAS
FOR EACH ROW
	BEGIN
		DECLARE precioEstanciaTotal INTEGER;
		SET precioEstanciaTotal =  (SELECT DATEDIFF(new.fechaInicio, new.fechaFin)*
								   (SELECT preciodia FROM habitaciones WHERE new.codHotel=habitaciones.codHotel and new.numHabitacion=habitaciones.numHabitacion)+
								   (SELECT precio from regimenes WHERE new.codregimen=regimenes.codregimen)*
								   (new.ocupantes));
		set new.precioestancia = precioEstanciaTotal;
	END $$
DELIMITER ;


-- Hacer un Trigger que no permita eliminar ningún hotel.DELIMITER $$

DELIMITER $$
CREATE TRIGGER tr_delete_hoteles
BEFORE DELETE
ON hoteles
FOR EACH ROW
	BEGIN
	    SIGNAL sqlstate '45000' set message_text = "No se puede eliminar";
	END $$
DELIMITER ;