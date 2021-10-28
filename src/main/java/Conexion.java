import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
                    this.conexion = DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=bdhoteles", "sa", "sanandreas85");
                    break;
                case"access":
                    this.conexion = DriverManager.getConnection("jdbc:ucanaccess://src/bdhoteles/darel_martinez_caballero_access.accdb");
                    break;
            }
        } catch (SQLException throwables) {
            System.out.println("A ocurrido un error\n");
        }
        return this.conexion;
    }
}
