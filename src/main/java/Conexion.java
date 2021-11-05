import java.sql.*;

public class Conexion {
    private String nomBd;
    private Connection conexion;

    public Conexion() {
    }

    public Conexion(String nomBd) {
        this.nomBd = nomBd;
    }

    protected Connection conexion() {
        try {
            switch (this.nomBd.toLowerCase()) {
                case "mysql":
                    //1. Crear la conexión
                    this.conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdhoteles", "root", "");
                    break;
                case "sqlserver":
                    this.conexion = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-LRA1LTK\\SQLEXPRESS;databaseName=bdHoteles", "sa", "sanandreas85");
                    break;
                case"access":
                    this.conexion = DriverManager.getConnection("jdbc:ucanaccess://src/bdhoteles/darel_martinez_caballero_access.accdb");
                    break;
            }
        }catch (NullPointerException e){
            System.out.println("No se a podido establecer la conexión");
        } catch (Exception throwables) {
            System.out.println("A ocurrido un error\n");
        }
        return this.conexion;
    }
}
