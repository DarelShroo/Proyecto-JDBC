import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Scanner;

public class ApiUsuario {
    private Conexion db;
    private String nomBd;
    private Statement stmt;
    private Scanner sc;
    private String continuar;
    private String opcion;
    private Sentencia sentencia;
    private PreparedStatement pstmt;

    public ApiUsuario() {
    }

    public void main(String[] args) {
        sc = new Scanner(System.in);
        continuar = "continuar";
        sentencia = new Sentencia();
        try {
            cambioBd();
            while (continuar.equals("continuar")) {
                System.out.println("¿Que desea hacer?");
                System.out.println("0.- visualizarHabitaciones");
                System.out.println("1.- insertarHabitacion");
                System.out.println("2.- cambiarConexion");
                System.out.println("3.- borrarHabitacion");
                System.out.println("4.- modificarHabitacion");
                System.out.println("5.- procListaHabitacionesNomHotel");
                System.out.println("6.- procInsertarHabitacion");
                System.out.println("7.- procCantidadHabitaciones");
                System.out.println("8.- funcSumTotalEstancias");
                System.out.println("9.- fin\n");
                opcion = sc.nextLine();
                switch (opcion) {
                    case "0":
                        sentencia.visualizarHabitaciones(stmt);
                        db(nomBd).conexion().close();
                        break;
                    case "1":
                        sentencia.insertarHabitacion(db(nomBd), pstmt, stmt, nomBd);
                        db(nomBd).conexion().close();
                        break;
                    case "2":
                        cambioBd();
                        break;
                    case "3":
                        sentencia.borrarHabitacion(db(nomBd), pstmt, nomBd);
                        db(nomBd).conexion().close();
                        break;
                    case "4":
                        sentencia.modificarHabitacion(db(nomBd), nomBd);
                        break;
                    case "5":
                        sentencia.procListaHabitacionesNomHotel(db(nomBd), nomBd);
                        break;
                    case "6":
                        sentencia.procInsertarHabitacion(db(nomBd), stmt, nomBd);
                        break;
                    case "7":
                        sentencia.procCantidadHabitaciones(db(nomBd), stmt, nomBd);
                        break;
                    case "8":
                        sentencia.funcSumaTotalEstancias(db(nomBd), pstmt, nomBd);
                        break;
                    case "9":
                        continuar = "fin";
                        break;
                }
                System.out.println();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Existe un quebrantamiento de claves");
        } catch (Exception e) {
            System.out.println("A ocurrido algún error");
            e.printStackTrace();
        }

    }

    private Conexion db(String nomBd) {
        db = new Conexion(nomBd);
        return db;
    }

    private void cambioBd() {
        System.out.println("¿A que base de datos desea conectar? ");
        System.out.println("mysql");
        System.out.println("access");
        System.out.println("sqlserver\n");

        do {
            nomBd = sc.nextLine().toLowerCase();
            if (!nomBd.equals("mysql") && !nomBd.equals("sqlserver")  && !nomBd.equals("access")) {
                System.out.println("No has escrito bien el nombre de la bd\n");
            }
            System.out.println();
        } while (!nomBd.equals("mysql") && !nomBd.equals("sqlserver") && !nomBd.equals("access"));
        try {
            this.stmt = db(nomBd).conexion().createStatement();
        } catch (SQLServerException e) {
            System.out.println("Errror al conectarse al servidor.\n");
        } catch (SQLException e) {
            System.out.println("A ocurrido un error");
        } catch (NullPointerException e) {
            System.out.println("No hay una conexión establecida\n");
        }
    }
}