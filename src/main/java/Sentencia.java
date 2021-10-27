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

    public void visualizarHabitaciones(Statement stmt) throws SQLException {
        ResultSet  rs = null;
        try {
            rs= stmt.executeQuery("select habitaciones.*, nomHotel from habitaciones inner join hoteles on habitaciones.codHotel = hoteles.codHotel");
            System.out.println("codHotel" + "\t#\t" + "nomHotel" + "\t#\t" + "numHabitacion" + "\t#\t" + "preciodia" + "\t#\t" + "activa");
            while (rs.next()) {
                System.out.println(rs.getString("codHotel") + "\t#\t" +
                        rs.getString("nomHotel") + "\t#\t" +
                        rs.getString("numHabitacion") + "\t#\t" +
                        rs.getInt("capacidad") + "\t#\t" +
                        rs.getInt("preciodia") + "\t#\t" +
                        rs.getInt("activa") + "\t#\t");
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("A ocurrido un error");
        } finally {
            close(null, stmt, null, rs, null);
        }
    }

    protected void visualizarHoteles(Statement stmt) throws SQLException {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("select * from hoteles");
            while (rs.next()) {
                System.out.println("codHotel = " + rs.getString("codHotel") + "\t#" +
                        "nomHotel = " + rs.getString("nomHotel") + "#\t");
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("A ocurrido algún error");
        } finally {
            close(null, stmt, null, rs, null);
        }
    }

    protected void insertarHabitacion(Conexion db, PreparedStatement pstmt, Statement stmt, String nomBd) throws SQLException {
        this.sc = new Scanner(System.in);
        boolean continua = true;
        int rows;
        if (!nomBd.equals("access")) {
            try {
                System.out.println("hoteles disponible:");
                visualizarHoteles(stmt);
                System.out.println();
                continua = true;
                while (continua) {
                    System.out.print("codHotel: ");
                    codHotel = this.sc.nextLine();
                    pstmt = db.conexion().prepareStatement("SELECT * FROM hoteles where codHotel=?");
                    System.out.println();

                    pstmt.setString(1, codHotel);
                    ResultSet rows2 = pstmt.executeQuery();
                    if (rows2.next()) {
                        continua = false;
                        System.out.println("\nEl código de hotel existe\n");
                    } else {
                        System.out.println("El código de hotel no existe\n");
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
                        System.out.print("Activa solo tiene valor de 1 o 0 vuelve a escribir el valor activa: ");
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
                System.out.println("A ocurrido un quebrantamiento de claves\n2");
            } catch (MysqlDataTruncation e){
                System.out.println("Alguno de los parámetros introducidos es demasiado largo para ese campo");
            } catch (SQLException e) {
                System.out.println("A ocurrido un error\n");
                e.printStackTrace();
            } catch (NullPointerException | InputMismatchException e) {
                System.out.println("\nNo puedes introducir un null en algúno de los parámetros.\n");
            } finally {
                close(db, stmt, pstmt, null, null);
            }
        } else {
            System.out.println("No es posible en una base de datos access");
        }
    }

    protected void borrarHabitacion(Conexion db, PreparedStatement pstmt, String nomBd) throws SQLException {
        sc = new Scanner(System.in);
        int rows = 0;
        String opcion = "";
        ResultSet rs;
        PreparedStatement pstmtComprobarHabitaciones = pstmt;
        Habitacion habitacion;
        ArrayList<Habitacion> arrayListHabitacion = new ArrayList<>();
        int regBorrar;
        int pos = 0;
        if (!nomBd.equals("access")) {
            PreparedStatement pstmtSelect = pstmt;
            PreparedStatement pstmtBorrar = pstmt;
            try {
                pstmtSelect = db.conexion().prepareStatement("select * from habitaciones");
                rs = pstmtSelect.executeQuery();

                while (rs.next()) {
                    System.out.println("numeroRegistro = " + pos + "\t#\t" + rs.getString("codHotel") + "\t#\t" + rs.getString("numHabitacion") + "\t#\t" + rs.getInt("capacidad") + "\t#\t" + rs.getInt("preciodia") + "\t#\t" + rs.getInt("activa"));
                    arrayListHabitacion.add(new Habitacion(rs.getString("codHotel"), rs.getString("numHabitacion"), rs.getInt("capacidad"), rs.getInt("preciodia"), rs.getInt("activa")));
                    pos++;
                }
                System.out.println("¿Que numero de registro desea modificar?");
                regBorrar = sc.nextInt();

                habitacion = arrayListHabitacion.get(regBorrar);
                numHabitacion = habitacion.getNumHabitacion();
                codHotel = habitacion.getCodHotel();

                pstmtComprobarHabitaciones = db.conexion().prepareStatement("SELECT * FROM estancias WHERE numHabitacion=?");
                pstmtComprobarHabitaciones.setString(1, numHabitacion);
                rs = pstmtComprobarHabitaciones.executeQuery();
                if (rs.next()) {
                    sc.nextLine();
                    System.out.println("¿Quiere borrar todos las habitaciones con ese código de hotel? , escriba si o no\n");
                    opcion = sc.nextLine();
                } else {
                    opcion = "no";
                }
                do {
                    if (opcion.equals("si")) {
                        pstmtBorrar = db.conexion().prepareStatement("DELETE FROM habitaciones WHERE codHotel=?");
                        pstmtBorrar.setString(1, codHotel);
                        rows = pstmtBorrar.executeUpdate();
                    } else if (opcion.equals("no")) {
                        pstmtBorrar = db.conexion().prepareStatement("DELETE FROM habitaciones WHERE codHotel=? and numHabitacion=?");
                        pstmtBorrar.setString(1, codHotel);
                        pstmtBorrar.setString(2, numHabitacion);
                        rows = pstmtBorrar.executeUpdate();
                    } else {
                        System.out.print("No es una opción válida, escriba denuevo la opción:");
                        opcion = sc.nextLine();
                    }
                } while (!opcion.equals("si") && !opcion.equals("no"));

                if (rows > 0) {
                    System.out.println("\nBorrado con éxito\n");
                } else {
                    System.out.println("\nNo se a borrado ningún registro\n");
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("\nA ocurrido un error al borrar el registro o los registros \n");
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
        } else {
            System.out.println("No es posible en una base de datos access");
        }
    }

    public void modificarHabitacion(Conexion db, String nomBd) throws SQLException {
        sc = new Scanner(System.in);
        ResultSet rs;
        ArrayList<Habitacion> arrayListHabitacion = new ArrayList<>();
        PreparedStatement pstmt = null;
        int regMod;
        if (!nomBd.equals("access")) {
            try {
                int pos = 0;
                pstmt = db.conexion().prepareStatement("select * from habitaciones");
                rs = pstmt.executeQuery();
                Habitacion habitacion;

                while (rs.next()) {
                    System.out.println("numeroRegistro = " + pos + "\t#\t" + rs.getString("codHotel") + "\t#\t" + rs.getString("numHabitacion") + "\t#\t" + rs.getInt("capacidad") + "\t#\t" + rs.getInt("preciodia") + "\t#\t" + rs.getInt("activa"));
                    arrayListHabitacion.add(new Habitacion(rs.getString("codHotel"), rs.getString("numHabitacion"), rs.getInt("capacidad"), rs.getInt("preciodia"), rs.getInt("activa")));
                    pos++;
                }
                System.out.println("¿Que registro desea modificar?");
                regMod = sc.nextInt();
                habitacion = arrayListHabitacion.get(regMod);
                System.out.println("Vas a modificar un registro con: ");
                System.out.print("codHotel: " + habitacion.getCodHotel() + "\n");
                System.out.print("numHabitacion: " + habitacion.getNumHabitacion() + "\n");
                System.out.print("capacidad: ");
                habitacion.setCapacidad(sc.nextInt());
                System.out.println();
                System.out.println("preciodia: ");
                habitacion.setCapacidad(sc.nextInt());
                System.out.println();
                System.out.println("activa: ");
                habitacion.setPreciodia(sc.nextInt());
                System.out.println();

                pstmt = db.conexion().prepareStatement("UPDATE habitaciones SET capacidad=?, preciodia=?, activa=? WHERE (codHotel=? and numHabitacion=?)");
                pstmt.setInt(1, habitacion.getCapacidad());
                pstmt.setInt(2, habitacion.getPreciodia());
                pstmt.setInt(3, habitacion.getActiva());
                pstmt.setString(4, habitacion.getCodHotel());
                pstmt.setString(5, habitacion.getNumHabitacion());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("A ocurrido un error\n");
            } finally {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (db != null) {
                    db.conexion().close();
                }
            }
        } else {
            System.out.println("No es posible en una base de datos access");
        }
    }

    public void procListaHabitacionesNomHotel(Conexion db, String nomBd) throws SQLException {
        CallableStatement procListHabNomHotel;
        sc = new Scanner(System.in);
        ResultSet rs = null;
        String nomHotel;
        if (!nomBd.equals("access")) {
            try {
                System.out.println("Escribe el nombre de hotel");
                nomHotel = sc.nextLine();
                procListHabNomHotel = db.conexion().prepareCall("{ call lista_habitaciones_nomHotel(?)}");
                procListHabNomHotel.setString(1, nomHotel);
                rs = procListHabNomHotel.executeQuery();

                System.out.println("numHabitacion " + "\t#\t" + "capacidad" + "\t#\t" + "preciodia" + "\t#\t" + "activa");
                while (rs.next()) {
                    System.out.print(rs.getString("numHabitacion") + "\t\t\t#\t" + rs.getInt("capacidad") + "\t\t\t#\t" + rs.getInt("preciodia") + "\t\t\t\t#\t" + rs.getInt("activa") + "\n");
                }
                System.out.println();
            } catch (SQLException e) {
                System.out.println("A ocurrido algún error");
                e.printStackTrace();
            } finally {
               close(db, null, null, rs, null);
            }
        } else {
            System.out.println("No hay procedimientos en access");
        }
    }

    public void procInsertarHabitacion(Conexion db, Statement stmt, String nomBd) throws SQLException {
        CallableStatement cstmtProcInsertarHabitacion = null;
        PreparedStatement pstmt;
        ResultSet rs;
        sc = new Scanner(System.in);
        boolean continua;
        if (!nomBd.equals("access")) {
            try {
                System.out.println("hoteles disponible:");
                visualizarHoteles(stmt);
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
                        System.out.println("\nEl código de hotel existe\n");
                        cstmtProcInsertarHabitacion.setString(1, this.codHotel);
                    } else {
                        System.out.println("El código de hotel no existe\n");
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
                continua = true;
                while (continua) {
                    if (activa == 0 || activa == 1) {
                        continua = false;
                    } else {
                        System.out.print("Activa solo tiene valor de 1 o 0 vuelve a escribir el valor activa: ");
                        activa = sc.nextInt();
                        System.out.println();
                    }
                }
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

                System.out.println(cstmtProcInsertarHabitacion.getInt(6) + " " + cstmtProcInsertarHabitacion.getInt(7) + "\n");

            } catch (MysqlDataTruncation e) {
                System.out.println("Algún parámetro introducido es mas largo del permitido\n");
            } catch (SQLException e) {
                System.out.println("A ocurrido algún error\n");
                e.printStackTrace();
            } catch (InputMismatchException e) {
                System.out.println("Has introducido una letra o simbolo en un parámetro de tipo numérico\n");
            } finally {
                close(db, stmt, null, null, cstmtProcInsertarHabitacion);
            }
        } else {
            System.out.println("No hay procedimientos en access");
        }
    }

    public void procCantidadHabitaciones(Conexion db, Statement stmt, String nomBd) throws SQLException {
        CallableStatement cstmtProcCantidadHabitaciones = null;
        PreparedStatement pstmt;
        sc = new Scanner(System.in);
        boolean continua;
        if (!nomBd.equals("access")) {
            try {
                System.out.println("hoteles disponible:");
                visualizarHoteles(stmt);
                System.out.println();
                continua = true;
                while (continua) {
                    System.out.print("codHotel: ");
                    this.codHotel = this.sc.nextLine();
                    System.out.println();
                    pstmt = db.conexion().prepareStatement("SELECT codHotel FROM hoteles WHERE codHotel = ?");
                    pstmt.setString(1, this.codHotel);
                    boolean rows2 = pstmt.execute();
                    if (rows2) {
                        continua = false;
                        System.out.println("\nEl código de hotel existe\n");
                        cstmtProcCantidadHabitaciones.setString(1, this.codHotel);
                    } else {
                        System.out.println("El código de hotel no existe\n");
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

                System.out.println("Numero de habitaciones totales con codHotel = " + this.codHotel + " y nomHotel = " + this.nomHotel + " =" + cstmtProcCantidadHabitaciones.getInt(4) + " Numero de habitaciones por debajo del precio dia " + this.preciodia + " = " + cstmtProcCantidadHabitaciones.getInt(5) + "\n");

            } catch (MysqlDataTruncation e) {
                System.out.println("Algún parámetro introducido es mas largo del permitido\n");
            } catch (SQLException e) {
                System.out.println("A ocurrido algún error\n");
            } catch (InputMismatchException e) {
                System.out.println("Has introducido una letra o simbolo en un parámetro de tipo numérico\n");
            } finally {
                close(db, stmt, null, null, cstmtProcCantidadHabitaciones);
            }
        } else {
            System.out.println("No hay procedimientos en access");
        }
    }

    public void funcSumaTotalEstancias(Conexion db, PreparedStatement pstmt, String nomBd) throws SQLException {
        this.sc = new Scanner(System.in);
        String coddnionie;
        CallableStatement cstmtFunTotalEstancias = null;
        System.out.println("Escribe un cod dni o nie");
        coddnionie = sc.nextLine();
        System.out.println();
        if (!nomBd.equals("access")) {
            try {
                cstmtFunTotalEstancias = db.conexion().prepareCall("{? = call sumaTotalEstancias(?)}");
                cstmtFunTotalEstancias.registerOutParameter(1, Types.INTEGER);
                cstmtFunTotalEstancias.setString(2, coddnionie);
                cstmtFunTotalEstancias.execute();
                System.out.println("SumaTotalEstancia = " + cstmtFunTotalEstancias.getInt(1));
            } catch (SQLException e) {
                System.out.println();            }
            finally {
                close(null,null,pstmt, null, cstmtFunTotalEstancias);
            }
        } else {
            System.out.println("No hay funciones en access");
        }
    }

    private void close (Conexion db, Statement stmt, PreparedStatement pstmt, ResultSet rs, CallableStatement cstmt) throws SQLException {
        if(db != null){
            db.conexion().close();
        }else  if(stmt != null){
            stmt.close();
        }else if(pstmt!=null){
            pstmt.close();
        }else if(rs!= null){
            rs.close();
        }else if(cstmt!=null){
            cstmt.close();
        }
    }
}
