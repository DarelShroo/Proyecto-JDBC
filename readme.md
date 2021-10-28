## Conexión a BD access, mysql y sqlserver con JDBC

### Proyecto Maven

- Conexión

  - Access

    ~~~java
    DriverManager.getConnection("jdbc:ucanaccess://src/bdhoteles/bdhotelesAccess.accdb");
    
    ~~~

    

  - MySql

    ~~~java
    DriverManager.getConnection("jdbc:mysql://localhost:3306/bdhoteles", "root", "");
    ~~~

    

  - SQLServer

    ~~~java
    DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=bdhoteles", "sa", "1234");
    ~~~

    

  - Visualizar

  ~~~java
  import java.sql.*;
  
  PreparedStament pstmt=null;
  Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdhoteles", "root", "");
  
  ResultSet  rs = null;
  Statement stmt = conexion.createStament();
  rs = stmt.executeQuery("select habitaciones.*, nomHotel from habitaciones inner join hoteles on habitaciones.codHotel = hoteles.codHotel");
  ~~~

  

- Inserción

  ~~~~java
  import java.sql.*;
  import java.util.*;
  
  String codHotel;
  String nomHotel;
  PreparedStament pstmt=null;
  Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdhoteles", "root", "");
  
  System.out.println("Introduce el nomHotel");
  nomHotel=sc.nextLine();
  System.out.println();
  System.out.print("numHabitacion: ");
  numHabitacion=sc.nextLine();
  
  pstmt=conexion.prepareStatement("INSERT INTO hoteles (codHotel,nomHotel) VALUES (?,?)");
  pstmt.setString(1,codHotel);
  pstmt.setString(2,nomHotel);
  
  rows=pstmt.executeUpdate();
  if(rows>0){
  	System.out.println("Se a insertado\n");
  }


- Actualización

  ~~~java
PreparedStament = null;
  Scanner sc = new Scanner(System.in);
  Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdhoteles", "root", "");

  System.out.print("capacidad: ");
  habitacion.setCapacidad(sc.nextInt());

  System.out.println("preciodia: ");
  habitacion.setCapacidad(sc.nextInt());
  System.out.println("activa: ");
  habitacion.setPreciodia(sc.nextInt());
  try{
  	pstmt = conexion.prepareStatement("UPDATE habitaciones SET capacidad=?, preciodia=?, activa=? WHERE (codHotel=? and numHabitacion=?)");
      pstmt.setInt(1, habitacion.getCapacidad());
      pstmt.setInt(2, habitacion.getPreciodia());
      pstmt.setInt(3, habitacion.getActiva());
      pstmt.setString(4, habitacion.getCodHotel());
      pstmt.setString(5, habitacion.getNumHabitacion());
      pstmt.executeUpdate();
      pstmt.close();
      conexion.close();
  }catch(SQLException e){
  	e.printStackTrace
  }
  ~~~

  

- Borrado

  ~~~
  Scanner sc = new Scanner(System.in);
  Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdhoteles", "root", "");
  PreparedStatement pstmtBorrar;
  try {
  	codHotel = sc.nextLine();
      pstmtBorrar = conexion.prepareStatement("DELETE FROM habitaciones WHERE codHotel=?");
      pstmtBorrar.setString(1, codHotel);
      rows = pstmtBorrar.executeUpdate();
      if (rows > 0) {
      	System.out.println("\nBorrado con éxito\n");
      } else {
       	System.out.println("\nNo se a borrado ningún registro\n");
      }
      pstmt.close();
      conexion.close();
      } catch (SQLIntegrityConstraintViolationException e) {
  		System.out.println("\nA ocurrido un error al borrar el registro o los registros\n");
  }
  ~~~

  

- Visualizar

  ~~~java
  Scanner sc = new Scanner(System.in);
  ResultSet rs;
  Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdhoteles", "root", "");
  Stament stmt = conexion.createStament;
  rs = stmt.executeQuery("select * from hoteles");
  try{
  	while (rs.next()) {
          System.out.println("codHotel = " + rs.getString("codHotel") + "\t#" + "nomHotel =	 " + rs.getString("nomHotel") + "#\t");
  	}
      rs.close();
      stmt.close();
      }catch(SQLException e) {
       	e.printStackTrace
  }
  ~~~

  

- Procedimientos con parámetros de entrada y salida

- Procedimientos que devuelven un valor

- Ejecutar el proyecto

  ~~~
  mvn compile exec:java
  ~~~
