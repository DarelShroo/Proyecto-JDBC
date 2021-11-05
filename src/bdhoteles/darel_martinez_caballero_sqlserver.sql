DROP DATABASE bdHoteles;
CREATE DATABASE bdHoteles;
GO
USE bdHoteles;
GO
CREATE TABLE hoteles (
	codHotel CHARACTER(6),
    nomHotel VARCHAR(60),
    CONSTRAINT pk_hoteles PRIMARY KEY (codHotel)
);
GO
CREATE TABLE habitaciones (
	codHotel CHARACTER(6),
    numHabitacion CHARACTER(4),
    capacidad TINYINT DEFAULT 2,
    preciodia INTEGER,
    activa bit DEFAULT 0,
    CONSTRAINT pk_habitaciones PRIMARY KEY (numHabitacion),
    CONSTRAINT fk_habitaciones_codHotel FOREIGN KEY (codHotel) REFERENCES hoteles(codHotel)
);
GO
CREATE TABLE regimenes (
	codregimen INTEGER IDENTITY(1,1),
    codHotel CHARACTER(6),
    tipo CHARACTER(2) CHECK (tipo = 'DY' OR tipo = 'MD' OR tipo = 'PC' OR tipo = 'TD'),
    precio INTEGER,
    CONSTRAINT pk_regimenes PRIMARY KEY (codregimen),
    CONSTRAINT fk_regimenes_codHotel FOREIGN KEY (codHotel) REFERENCES hoteles(codHotel)
);
GO
CREATE TABLE clientes (
	coddnionie CHARACTER (9),
    nombre VARCHAR(50),
    nacionalidad VARCHAR(40),
    CONSTRAINT pk_clientes PRIMARY KEY (coddnionie)
);
GO
CREATE TABLE estancias (
    codestancia INTEGER IDENTITY(1,1),
	coddnionie CHARACTER (9),
    codHotel CHARACTER(6),
    numHabitacion CHARACTER(4),
    fechaInicio DATE,
    fechaFin DATE,
    codregimen INTEGER,
    ocupantes TINYINT DEFAULT 2,
    precioestancia INTEGER,
    pagado BIT DEFAULT 1,
    CONSTRAINT pk_estancias PRIMARY KEY (codEstancia),
    CONSTRAINT fk_estancias_coddnieonie FOREIGN KEY (coddnionie) REFERENCES clientes(coddnionie),
    CONSTRAINT fk_estancias_codHotel FOREIGN KEY (codHotel) REFERENCES hoteles(codHotel)
);
GO
INSERT INTO hoteles VALUES ('A00', 'Mariachi');
INSERT INTO hoteles VALUES ('B01', 'El arbol');
INSERT INTO hoteles VALUES ('C02', 'Sueños');
INSERT INTO hoteles VALUES ('D03', 'VIP');
INSERT INTO hoteles VALUES ('E04', 'Residente');

GO
INSERT INTO habitaciones VALUES ('A00', '500', default, 50, default);
INSERT INTO habitaciones VALUES ('A00', '501', 4, 60, 1);
INSERT INTO habitaciones VALUES ('B01', '402', 5, 30, default);
INSERT INTO habitaciones VALUES ('B01', '403', default, 80, 1);
INSERT INTO habitaciones VALUES ('C02', '304', 1, 70, default);
INSERT INTO habitaciones VALUES ('C02', '305', default, 40, 1);
INSERT INTO habitaciones VALUES ('D03', '206', 4, 90, default);
INSERT INTO habitaciones VALUES ('D03', '207', default, 30, 1);
INSERT INTO habitaciones VALUES ('E04', '108', 3, 20, default);
INSERT INTO habitaciones VALUES ('E04', '109', default, 100, 1);

GO
INSERT INTO regimenes(codHotel, tipo, precio) VALUES ('A00', 'DY', 500);

GO
INSERT INTO clientes VALUES ('20304050G', 'Alejandro', 'Española');
INSERT INTO clientes VALUES ('87468463H', 'Cristina', 'Española');
INSERT INTO clientes VALUES ('47987454J', 'Yulisa', 'Rumana');
INSERT INTO clientes VALUES ('98798649Y', 'Kevin', 'Rumana');
INSERT INTO clientes VALUES ('87987468P', 'Hector', 'Cubana');
INSERT INTO clientes VALUES ('74987788G', 'Mariola', 'Cubana');
INSERT INTO clientes VALUES ('25496879L', 'Marlen', 'Angola');
INSERT INTO clientes VALUES ('56498779T', 'Heidi', 'Angola');
INSERT INTO clientes VALUES ('65496879R', 'Sara', 'Argentina');
INSERT INTO clientes VALUES ('87986748B', 'Felix', 'Argentina');

GO
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ('20304050G', 'A00', '501', '2021/01/01', '2021/01/20', 1, default, 500, default);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ('20304050G', 'A00', '500', '2021/01/20', '2021/01/30', 2, default, 600, default);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ('87468463H', 'B01', '402', '2021/02/01', '2021/02/20', 1, default, 300, 0);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ('87468463H', 'B01', '403', '2021/03/01', '2021/03/15', 1, default, 420, 0);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ('87468463H', 'C02', '304', '2021/03/16', '2021/03/27', 1, default, 530, default);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ('87987468P', 'C02', '305', '2021/04/01', '2021/04/15', 1, default, 690, default);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ('74987788G', 'D03', '206', '2021/04/16', '2021/04/28', 1, default, 730, 0);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ('65496879R', 'D03', '207', '2021/05/01', '2021/05/20', 1, default, 455, default);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ('98798649Y', 'E04', '108', '2021/06/15', '2021/06/25', 1, default, 322, default);
INSERT INTO estancias(coddnionie, codHotel, numHabitacion, fechaInicio, fechaFin, codregimen, ocupantes, precioestancia, pagado) 
VALUES ('56498779T', 'E04', '109', '2021/07/10', '2021/07/22', 1, default, 800, default);

GO
/* Crear un procedimiento almacenado que liste todas las habitaciones de cierto hotel pasando por
parámetro de entrada el nombre del hotel, ordenados por preciodia y capacidad en orden ascendente.
Los datos a visualizar serán: número de habitacion, capacidad, precio y activo.*/
GO
CREATE PROCEDURE lista_habitaciones_nomHotel(@nomHotel VARCHAR(60))
AS
BEGIN
	SELECT numHabitacion, capacidad, preciodia, activa FROM habitaciones 
	INNER JOIN hoteles on hoteles.codHotel=habitaciones.codHotel
	WHERE hoteles.nomHotel = @nomHotel 
	ORDER BY preciodia, capacidad ASC;
END

GO
/*Crear un procedimiento almacenado que inserte una habitacion, de modo que se le pase como
parámetros todos los datos.
Comprobar que el código de hotel pasado exista en la tabla hoteles. En caso de que no exista el hotel
que no se inserte.
Devolver en un parámetro de salida: 0 si el hotel no existe y 1 si el hotel existe.
Devolver en otro parámetro de salida: 0 si la habitacion no se insertó y 1 si la inserción fue correcta.*/
GO
CREATE PROCEDURE insertarHabitacion(@codHotel CHARACTER(6), @numHabitacion CHARACTER (4), 
	@capacidad TINYINT, @preciodia INTEGER, @activa BIT, @salida1 INTEGER OUT, @salida2 INTEGER OUT)
AS
BEGIN
	IF ((SELECT COUNT(*) from hoteles WHERE hoteles.codHotel = @codHotel) > 0)
		BEGIN
		insert into habitaciones values (@codHotel, @numHabitacion, @capacidad, @preciodia, @activa)
		SET @salida1=1;
		SET @salida2=1;
		END
	ELSE 
	BEGIN
		SET @salida1=0;
		SET @salida2=0;
	END
END


GO
/*Crear un procedimiento almacenado que indicándole un nombre de hotel y un preciodia, devuelva
dos parámetros. En un parámetro de salida la cantidad de habitaciones total que tiene ese hotel. En
otro parámetro de salida la cantidad de habitaciones por debajo del preciodia y sean activas.*/
GO
CREATE PROCEDURE cantidadHabitaciones(@codHotel CHARACTER(6), @nombreHotel VARCHAR(60), @preciod INTEGER, @salida1 INTEGER OUT,  @salida2 INTEGER OUT)
AS
BEGIN
	SET @salida1=(SELECT COUNT (numHabitacion) from habitaciones
	INNER JOIN hoteles on hoteles.codHotel=habitaciones.codHotel
	WHERE  hoteles.codHotel=@codHotel and hoteles.nomHotel=@nombreHotel);
	
	SET @salida2 = (SELECT COUNT(numHabitacion) from habitaciones 
	INNER JOIN hoteles ON hoteles.codHotel = habitaciones.codHotel
	WHERE hoteles.nomHotel = @nombreHotel AND habitaciones.preciodia < @preciod and activa = 0);
END

GO
/* Crear una función que dándole un dni o nie de un cliente como parámetro de entrada nos devuelva la
 suma total pagada por dicho cliente.*/
 GO
CREATE FUNCTION sumaTotalEstancias(@coddni CHARACTER(9)) 
RETURNS INTEGER
BEGIN
  DECLARE @sumaTotal INTEGER;
  SELECT  @sumaTotal = sum(precioestancia) FROM estancias WHERE coddnionie = @coddni;
  RETURN @sumaTotal;
END;

GO
-- Hacer un Trigger en la tabla estancias que al insertar o modificar el numero de ocupantes no supere la capacidad de la habitación.
GO
CREATE TRIGGER tr_insertUpdate_numOcupantesEstancias
ON ESTANCIAS
FOR INSERT, UPDATE
AS
		DECLARE @capacidad1 INTEGER;
		DECLARE @nocupantes INTEGER;
		SET @capacidad1 = (SELECT capacidad FROM habitaciones INNER JOIN inserted ON inserted.codHotel=habitaciones.codHotel WHERE inserted.codHotel=habitaciones.codHotel and inserted.numHabitacion=habitaciones.numHabitacion);
		SET @nocupantes = (SELECT capacidad FROM habitaciones WHERE codHotel = (SELECT codHotel FROM inserted) and numHabitacion = (SELECT numHabitacion FROM inserted));
		IF(@nocupantes>@capacidad1)
		BEGIN
			print 'No se hizo la inserción';
		END
GO

/*Hacer un Trigger en la tabla estancias que al insertar calcule el precio de la estancia en función del número de días de la estancia, precio día habitación, ocupantes y precio 
del régimen (precioestancia=(númerodedias*(preciodiahabitacion+ocupantes*precioregimen))). */
GO
CREATE TRIGGER tr_insert_precioEstancia
ON ESTANCIAS
INSTEAD OF INSERT
AS
	BEGIN
		DECLARE @precioEstanciaTotal INTEGER;
		DECLARE @vnumerodias INTEGER;
		DECLARE @vpreciodiahabitacion INTEGER;
		DECLARE @vocupantes INTEGER;
		DECLARE @vprecioregimen INTEGER;
		SET @precioEstanciaTotal =  (SELECT DATEDIFF(DAY,fechaInicio,fechaInicio) FROM inserted )
		SET @vnumerodias = (SELECT preciodia FROM inserted INNER JOIN habitaciones ON habitaciones.codHotel=inserted.codHotel
									WHERE inserted.codHotel=habitaciones.codHotel AND inserted.numHabitacion=habitaciones.numHabitacion)
		SET @vpreciodiahabitacion =	(SELECT ocupantes FROM inserted)
		SET @vocupantes =   (SELECT precio FROM inserted INNER JOIN regimenes ON regimenes.codregimen=inserted.codregimen WHERE inserted.codregimen=regimenes.codregimen)
		SET @vprecioregimen =  (@precioEstanciaTotal*(@vnumerodias*@vpreciodiahabitacion+@vprecioregimen));
	END
GO
-- Hacer un Trigger que no permita eliminar ningún hotel.DELIMITER $$
GO
CREATE TRIGGER tr_delete_hoteles
ON hoteles
FOR DELETE
AS
	BEGIN
	    raiserror ('No se puede eliminar', 16, 1)
	END
GO