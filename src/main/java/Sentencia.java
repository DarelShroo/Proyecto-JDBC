import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Sentencia {
    private Scanner sc;
    private String codHotel;
    private String numHabitacion;
    private String nomHotel;
    private int capacidad;
    private int preciodia;
    private int activa;

    public void visualizarHabitaciones(Conexion db) throws SQLException {
        Statement stmt = db.conexion().createStatement();
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("select habitaciones.*, nomHotel from habitaciones inner join hoteles on habitaciones.codHotel = hoteles.codHotel");
            System.out.printf("|%14s|%14s|%14s|%14s|%14s|%n","codHotel","nomHotel" , "numHabitacion" , "preciodia", "activa");
            while (rs.next()) {
                System.out.printf("|%14s|%14s|%14s|%14s|%14s|%n",rs.getString("codHotel"),
                        rs.getString("nomHotel") ,
                        rs.getString("numHabitacion") ,
                        rs.getInt("capacidad"),
                        rs.getInt("preciodia") ,
                        rs.getInt("activa"));
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("A ocurrido un error");
        } finally {
            close(null, stmt, null, rs, null);
        }
    }

    protected void visualizarHoteles(Conexion db) throws SQLException {
        Statement stmt = db.conexion().createStatement();
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("select * from hoteles");
            System.out.printf("|%14s|%14s|%n","codHotel", "nomHotel");
            while (rs.next()) {
                System.out.printf("|%14s|%14s|%n", rs.getString("codHotel"),
                        rs.getString("nomHotel"));
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("A ocurrido alg??n error");
        } finally {
            close(null, stmt, null, rs, null);
        }
    }

    protected void insertarHabitacion(Conexion db, String nomBd) throws SQLException {
        PreparedStatement pstmt;
        this.sc = new Scanner(System.in);
        boolean continua = true;
        int rows;
        try {
            System.out.println("hoteles disponible:");
            visualizarHoteles(db);
            System.out.println();
            while (continua) {
                System.out.print("codHotel: ");
                codHotel = this.sc.nextLine();
                pstmt = db.conexion().prepareStatement("SELECT * FROM hoteles where codHotel=?");
                System.out.println();

                pstmt.setString(1, codHotel);
                ResultSet rows2 = pstmt.executeQuery();
                if (rows2.next()) {
                    continua = false;
                    System.out.println("\nEl c??digo de hotel existe\n");
                } else {
                    System.out.println("El c??digo de hotel no existe\n");
                }

            }
            System.out.println();
            System.out.print("numHabitacion: ");
            numHabitacion = sc.nextLine();

            System.out.println();
            System.out.print("capacidad: ");
            capacidad = sc.nextInt();
            System.out.println();
            System.out.print("preciodia: ");
            preciodia = sc.nextInt();
            System.out.println();
            System.out.print("activa: ");
            activa = sc.nextInt();
            continua = true;
            while (continua) {
                if (activa == 0 || activa == 1) {
                    continua = false;
                } else {
                    System.out.print("\nmActiva solo tiene valor de 1 o 0 vuelve a escribir el valor activa: ");
                    activa = sc.nextInt();
                    System.out.println();
                }
            }
            System.out.println();
            pstmt = db.conexion().prepareStatement("INSERT INTO habitaciones (codHotel, numHabitacion, capacidad, preciodia, activa) VALUES (?,?,?,?,?)");
            pstmt.setString(1, codHotel);
            pstmt.setString(2, numHabitacion);
            pstmt.setInt(3, capacidad);

            pstmt.setInt(4, preciodia);
            pstmt.setInt(5, activa);
            rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Se a insertado\n");
            }

        } catch (SQLIntegrityConstraintViolationException | SQLServerException e) {
            System.out.println("A ocurrido un quebrantamiento de claves\n");
        } catch (MysqlDataTruncation e) {
            System.out.println("Alguno de los par??metros introducidos es demasiado largo para ese campo");
        } catch (SQLException e) {
            System.out.println("A ocurrido un error\n");
            e.printStackTrace();
        } catch (NullPointerException | InputMismatchException e) {
            System.out.println("\nNo puedes introducir un null en alg??no de los par??metros.\n");
        } finally {
            close(db, null, null, null, null);
        }

    }

    protected void borrarHabitacion(Conexion db, String nomBd) throws SQLException {
        sc = new Scanner(System.in);
        int rows = 0;
        int regBorrar;
        int pos = 0;
        String opcion = "";
        ResultSet rs;
        PreparedStatement pstmtComprobarHabitaciones = null;
        Habitacion habitacion;
        ArrayList<Habitacion> arrayListHabitacion = new ArrayList<>();
        PreparedStatement pstmtSelect = null;
        PreparedStatement pstmtBorrar = null;
        try {
            pstmtSelect = db.conexion().prepareStatement("select * from habitaciones");
            rs = pstmtSelect.executeQuery();
            System.out.printf("|%14s|%14s|%14s|%14s|%14s|%14s|%n","numeroRegistro","codHotel","numHabitacion" ,"capacidad" , "preciodia","activa");
            while (rs.next()) {
                System.out.printf("|%14s|%14s|%14s|%14s|%14s|%14s|%n",
                        pos,
                        rs.getString("codHotel"),
                        rs.getString("numHabitacion"),
                        rs.getInt("capacidad"),
                        rs.getInt("preciodia"),
                        rs.getInt("activa"));
                arrayListHabitacion.add(new Habitacion(rs.getString("codHotel"), rs.getString("numHabitacion"), rs.getInt("capacidad"), rs.getInt("preciodia"), rs.getInt("activa")));
                pos++;
            }
            System.out.print("\n??Que numero de registro desea borrar?: ");
            regBorrar = compruebaTamanioIndex(sc.nextInt(), arrayListHabitacion.size() - 1);

            habitacion = arrayListHabitacion.get(regBorrar);
            numHabitacion = habitacion.getNumHabitacion();
            codHotel = habitacion.getCodHotel();

            pstmtComprobarHabitaciones = db.conexion().prepareStatement("SELECT * FROM estancias WHERE numHabitacion=?");
            pstmtComprobarHabitaciones.setString(1, numHabitacion);
            rs = pstmtComprobarHabitaciones.executeQuery();
            if (rs.next()) {
                sc.nextLine();
                System.out.println("??Quiere borrar todos las estancias con ese c??digo de hotel? , escriba si o no\n");
                opcion = sc.nextLine();
            } else {
                opcion = "no";
            }
            do {
                if (opcion.equals("si")) {
                    pstmtBorrar = db.conexion().prepareStatement("DELETE FROM estancias WHERE codHotel=?");
                    pstmtBorrar.setString(1, codHotel);
                    rows = pstmtBorrar.executeUpdate();
                } else if (opcion.equals("no")) {
                    pstmtBorrar = db.conexion().prepareStatement("DELETE FROM habitaciones WHERE codHotel=? and numHabitacion=?");
                    pstmtBorrar.setString(1, codHotel);
                    pstmtBorrar.setString(2, numHabitacion);
                    rows = pstmtBorrar.executeUpdate();
                } else {
                    System.out.print("No es una opci??n v??lida, escriba denuevo la opci??n:");
                    opcion = sc.nextLine();
                }
            } while (!opcion.equals("si") && !opcion.equals("no"));

            if (rows > 0) {
                System.out.println("\nBorrado con ??xito\n");
            } else {
                System.out.println("\nNo se a borrado ning??n registro\n");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("\nA ocurrido un error al borrar el registro o los registros \n");
        } catch (NullPointerException e) {
            System.out.println("Alg??n par??metro est?? a null");
        } catch (InputMismatchException e) {
            System.out.println("A ocurrido un error");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmtBorrar != null) {
                pstmtBorrar.close();
            } else if (pstmtComprobarHabitaciones != null) {
                pstmtComprobarHabitaciones.close();
            } else if (pstmtSelect != null) {
                pstmtSelect.close();
            }
        }
    }

    public void modificarHabitacion(Conexion db, String nomBd) throws SQLException {
        sc = new Scanner(System.in);
        ResultSet rs;
        ArrayList<Habitacion> arrayListHabitacion = new ArrayList<>();
        PreparedStatement pstmt = null;
        int regMod;
        try {
            int pos = 0;
            pstmt = db.conexion().prepareStatement("select * from habitaciones");
            rs = pstmt.executeQuery();
            Habitacion habitacion;
            System.out.printf("|%14s|%14s|%14s|%14s|%14s|%14s|%n","numeroRegistro","codHotel" , "numHabitacion" , "capacidad", "preciodia" , "activa");

            while (rs.next()) {
                System.out.printf("|%14s|%14s|%14s|%14s|%14s|%14s|%n",
                        pos,
                        rs.getString("codHotel"),
                        rs.getString("numHabitacion"),
                        rs.getInt("capacidad"),
                        rs.getInt("preciodia"),
                        rs.getInt("activa"));
                arrayListHabitacion.add(new Habitacion(rs.getString("codHotel"), rs.getString("numHabitacion"), rs.getInt("capacidad"), rs.getInt("preciodia"), rs.getInt("activa")));
                pos++;
            }

            System.out.println("??Que registro desea modificar?");
            regMod = compruebaTamanioIndex(sc.nextInt(), arrayListHabitacion.size() - 1);
            habitacion = arrayListHabitacion.get(regMod);
            System.out.println("Vas a modificar un registro con: ");
            System.out.print("codHotel: " + habitacion.getCodHotel() + "\n");
            System.out.print("numHabitacion: " + habitacion.getNumHabitacion() + "\n");
            System.out.print("capacidad: ");
            habitacion.setCapacidad(sc.nextInt());
            System.out.println();
            System.out.print("preciodia: ");
            habitacion.setPreciodia(sc.nextInt());
            System.out.println();
            System.out.print("activa: ");
            habitacion.setActiva(compruebaActiva(sc.nextInt()));
            System.out.println();

            pstmt = db.conexion().prepareStatement("UPDATE habitaciones SET capacidad=?, preciodia=?, activa=? WHERE (codHotel=? and numHabitacion=?)");
            pstmt.setInt(1, habitacion.getCapacidad());
            pstmt.setInt(2, habitacion.getPreciodia());
            pstmt.setInt(3, habitacion.getActiva());
            pstmt.setString(4, habitacion.getCodHotel());
            pstmt.setString(5, habitacion.getNumHabitacion());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Modificaci??n realizada");
            }
        } catch (NullPointerException e) {
            System.out.println("Alg??n par??metro est?? a null");
        } catch (SQLException | InputMismatchException e) {
            System.out.println("A ocurrido un error\n");
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (db != null) {
                db.conexion().close();
            }
        }
    }

    public void procListaHabitacionesNomHotel(Conexion db, String nomBd) throws SQLException {
        CallableStatement procListHabNomHotel;
        sc = new Scanner(System.in);
        ResultSet rs = null;
        String nomHotel;
        if (!nomBd.equals("access")) {
            try {
                visualizarHoteles(db);
                System.out.println("Escribe el nombre de hotel");
                nomHotel = sc.nextLine();
                procListHabNomHotel = db.conexion().prepareCall("{ call lista_habitaciones_nomHotel(?)}");
                procListHabNomHotel.setString(1, nomHotel);
                rs = procListHabNomHotel.executeQuery();
                System.out.println("Lista de habitaciones ordenadas por preciodia y  capacidad en orden ascendente");
                if (rs.next()) {
                    System.out.printf("|%14s|%14s|%14s|%14s|%n","numHabitacion" , "capacidad", "preciodia" , "activa");
                } else {
                    System.out.println("Este hotel no existe");
                }
                while (rs.next()) {
                    System.out.printf("%14s|%14s|%14s|%14s|%n",rs.getString("numHabitacion") ,rs.getInt("capacidad") , rs.getInt("preciodia") , rs.getInt("activa") + "\n");
                }
            } catch (SQLException e) {
                System.out.println("A ocurrido alg??n error");
            } finally {
                close(db, null, null, rs, null);
            }
        } else {
            System.out.println("No hay procedimientos en access");
        }
    }

    public void procInsertarHabitacion(Conexion db, String nomBd) throws SQLException {
        CallableStatement cstmtProcInsertarHabitacion = null;
        PreparedStatement pstmt;
        ResultSet rs;
        sc = new Scanner(System.in);
        boolean continua;
        if (!nomBd.equals("access")) {
            try {
                System.out.println("hoteles disponible:");
                visualizarHoteles(db);
                System.out.println();
                continua = true;
                while (continua) {
                    System.out.print("codHotel: ");
                    this.codHotel = this.sc.nextLine();
                    System.out.println();
                    pstmt = db.conexion().prepareStatement("SELECT codHotel FROM hoteles WHERE codHotel = ?");
                    pstmt.setString(1, this.codHotel);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        continua = false;
                        System.out.println("\nEl c??digo de hotel existe\n");
                    } else {
                        System.out.println("El c??digo de hotel no existe\n");
                    }
                }
                System.out.println();
                System.out.print("numHabitacion: ");
                this.numHabitacion = this.sc.nextLine();

                System.out.println();
                System.out.print("capacidad: ");
                this.capacidad = this.sc.nextInt();
                System.out.println();
                System.out.print("preciodia: ");
                this.preciodia = this.sc.nextInt();
                System.out.println();
                System.out.print("activa: ");
                this.activa = this.sc.nextInt();
                this.activa = compruebaActiva(this.activa);
                System.out.println();
                cstmtProcInsertarHabitacion = db.conexion().prepareCall("{call insertarHabitacion(?,?,?,?,?,?,?)}");

                cstmtProcInsertarHabitacion.setString(1, this.codHotel);
                cstmtProcInsertarHabitacion.setString(2, this.numHabitacion);
                cstmtProcInsertarHabitacion.setInt(3, this.capacidad);

                cstmtProcInsertarHabitacion.setInt(4, this.preciodia);
                cstmtProcInsertarHabitacion.setInt(5, this.activa);
                cstmtProcInsertarHabitacion.registerOutParameter(6, Types.INTEGER, 1);
                cstmtProcInsertarHabitacion.registerOutParameter(7, Types.INTEGER, 1);

                cstmtProcInsertarHabitacion.execute();

                if (cstmtProcInsertarHabitacion.getInt(6) == 1) {
                    System.out.println("Hotel existe: " + cstmtProcInsertarHabitacion.getInt(6));
                } else {
                    System.out.println("Hotel no existe: " + cstmtProcInsertarHabitacion.getInt(6));
                }

                if (cstmtProcInsertarHabitacion.getInt(7) == 1) {
                    System.out.println("La habitacion se insert??: " + cstmtProcInsertarHabitacion.getInt(7));
                } else {
                    System.out.println("La no habitacion se insert?? " + cstmtProcInsertarHabitacion.getInt(7));
                }


            } catch (MysqlDataTruncation e) {
                System.out.println("Alg??n par??metro introducido es mas largo del permitido\n");
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("A ocurrido un quebrantamiento de claves");
            } catch (SQLException e) {
                System.out.println("A ocurrido alg??n error\n");
            } catch (InputMismatchException e) {
                System.out.println("Has introducido una letra o simbolo en un par??metro de tipo num??rico\n");
            } finally {
                close(db, null, null, null, cstmtProcInsertarHabitacion);
            }
        } else {
            System.out.println("No hay procedimientos en access");
        }
    }

    public void procCantidadHabitaciones(Conexion db, String nomBd) throws SQLException {
        CallableStatement cstmtProcCantidadHabitaciones = null;
        PreparedStatement pstmt;
        sc = new Scanner(System.in);
        boolean continua;
        if (!nomBd.equals("access")) {
            try {
                System.out.println("hoteles disponible:");
                visualizarHoteles(db);
                System.out.println();
                continua = true;
                while (continua) {
                    System.out.print("codHotel: ");
                    this.codHotel = this.sc.nextLine();
                    System.out.println();
                    pstmt = db.conexion().prepareStatement("SELECT codHotel FROM hoteles WHERE codHotel = ?");
                    pstmt.setString(1, this.codHotel);
                    ResultSet rows2 = pstmt.executeQuery();
                    if (rows2.next()) {
                        continua = false;
                        System.out.println("\nEl c??digo de hotel existe\n");
                    } else {
                        System.out.println("El c??digo de hotel no existe\n");
                    }

                }

                System.out.println();
                System.out.print("nomHotel: ");
                this.nomHotel = this.sc.nextLine();
                System.out.println();

                System.out.print("preciodia: ");
                this.preciodia = this.sc.nextInt();
                System.out.println();

                cstmtProcCantidadHabitaciones = db.conexion().prepareCall("{call cantidadHabitaciones(?,?,?,?,?)}");

                cstmtProcCantidadHabitaciones.setString(1, this.codHotel);
                cstmtProcCantidadHabitaciones.setString(2, this.nomHotel);
                cstmtProcCantidadHabitaciones.setInt(3, this.preciodia);
                cstmtProcCantidadHabitaciones.registerOutParameter(4, Types.INTEGER);
                cstmtProcCantidadHabitaciones.registerOutParameter(5, Types.INTEGER);

                cstmtProcCantidadHabitaciones.execute();

                System.out.println("Numero de habitaciones totales con codHotel (" + this.codHotel + ") y nomHotel (" + this.nomHotel + ") es igual a "
                        + cstmtProcCantidadHabitaciones.getInt(4) +
                        " \nCon el mismo codHotel y nomHotel hay un total de " +
                        cstmtProcCantidadHabitaciones.getInt(5) +
                        " habitaciones por debajo del precio dia (" + this.preciodia + ")   ");

            } catch (MysqlDataTruncation e) {
                System.out.println("Alg??n par??metro introducido es mas largo del permitido\n");
            } catch (SQLException e) {
                System.out.println("A ocurrido alg??n error\n");
            } catch (InputMismatchException e) {
                System.out.println("Has introducido una letra o simbolo en un par??metro de tipo num??rico\n");
            } finally {
                close(db, null, null, null, cstmtProcCantidadHabitaciones);
            }
        } else {
            System.out.println("No hay procedimientos en access");
        }
    }

    public void funcSumaTotalEstancias(Conexion db, String nomBd) throws SQLException {
        PreparedStatement pstmt = null;
        this.sc = new Scanner(System.in);
        String coddnionie;
        CallableStatement cstmtFunTotalEstancias = null;
        boolean continuar = false;
        if (!nomBd.equals("access")) {
            mostrarDNI(db);
            System.out.println("Escribe un cod dni o nie");
            coddnionie = sc.nextLine();
            continuar = true;
            int i = compruebaDNI(coddnionie, db);
            while(continuar){
                if (i > 0){
                    continuar = false;
                }else {
                    System.out.println("Este coddnionie no existe, vuelve a escribirlo: ");
                    coddnionie = sc.nextLine();
                }
            }

            System.out.println();
            try {
                cstmtFunTotalEstancias = db.conexion().prepareCall("{? = call sumaTotalEstancias(?)}");
                cstmtFunTotalEstancias.registerOutParameter(1, Types.INTEGER);
                cstmtFunTotalEstancias.setString(2, coddnionie);
                cstmtFunTotalEstancias.execute();
                System.out.println("SumaTotalEstancia = " + cstmtFunTotalEstancias.getInt(1));
            } catch (SQLException e) {
                System.out.println();
            } finally {
                close(null, null, pstmt, null, cstmtFunTotalEstancias);
            }
        } else {
            System.out.println("No hay funciones en access");
        }
    }

    private void close(Conexion db, Statement stmt, PreparedStatement pstmt, ResultSet rs, CallableStatement cstmt) throws SQLException {
        if (db != null) {
            db.conexion().close();
        } else if (stmt != null) {
            stmt.close();
        } else if (pstmt != null) {
            pstmt.close();
        } else if (rs != null) {
            rs.close();
        } else if (cstmt != null) {
            cstmt.close();
        }
    }

    private int compruebaTamanioIndex(int nextInt, int tamanioIndex) {
        boolean continua = true;
        while (continua) {
            if (nextInt > tamanioIndex) {
                System.out.print("Escriba un numero de registro v??lido: ");
                nextInt = sc.nextInt();
            } else {
                continua = false;
            }
        }
        return nextInt;
    }

    private int compruebaDNI(String dni, Conexion db) {
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {

                pstmt = db.conexion().prepareStatement("SELECT COUNT(coddnionie) FROM clientes WHERE coddnionie = ?");
                pstmt.setString(1, dni);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    i = rs.getInt(1);
                }
                return i;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    private void mostrarDNI(Conexion db) {
        ResultSet rs = null;
        try {
            rs = db.conexion().prepareStatement("SELECT coddnionie FROM CLIENTES").executeQuery();
            System.out.println("DNI disponibles");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
            close(db, null, null, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int compruebaActiva(int activa) {
        boolean continua = true;
        while (continua) {
            if (activa == 0 || activa == 1) {
                continua = false;
            } else {
                System.out.print("Activa solo tiene valor de 1 o 0 vuelve a escribir el valor activa: ");
                activa = sc.nextInt();
                System.out.println();
            }
        }
        return activa;
    }
}
