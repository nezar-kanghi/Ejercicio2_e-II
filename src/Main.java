import javax.naming.directory.InitialDirContext;
import java.sql.*;
import java.util.Scanner;

public class CRUDciclista {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String url ="jdbc:oracle:thin:@localhost:1521:xe";
        String usuario = "RIBERA";
        String password = "ribera";

        int dato = 0;

        //MENU
        do{
            System.out.println("MENU (0 para salir)");
            System.out.println("1. Insertar un nuevo ciclista");
            System.out.println("2. Actualizar ciclista");
            System.out.println("3. Insertar un nuevo ciclista");
            dato = sc.nextInt();


            switch (dato){

                //Insertar ciclista
                case 1:
                    try(Connection conexion = DriverManager.getConnection(url, usuario, password);
                        Statement statement = conexion.createStatement()){

                        int idCiclista = 0;

                        System.out.println("Ingresa el nombre que quieres insertar: ");
                        String nombre = sc.nextLine();
                        sc.nextLine();
                        System.out.println("Ingresa la nacionalidad que quieres insertar: ");
                        String nacionalidad = sc.nextLine();
                        System.out.println("Ingresa la edad que quieres insertar: ");
                        int edad = sc.nextInt();
                        System.out.println("Ingresa el id_equipo que quieres insertar: ");
                        int id_equipo = sc.nextInt();

                        String sql1 = "SELECT MAX (ID_CICLISTA) AS max_id FROM CICLISTA";  //sacamos el max id
                        ResultSet rs = statement.executeQuery(sql1);
                        while(rs.next()){
                            idCiclista = rs.getInt("max_id") + 1; //le sumamos uno apra añadirselo al siguiente ciclista

                        }


                        String sql = "INSERT INTO ciclista (ID_CICLISTA, NOMBRE, NACIONALIDAD, EDAD, ID_EQUIPO) VALUES (?,?,?,?,?)"; //añadimos los datos
                        PreparedStatement ps = conexion.prepareStatement(sql);
                        ps.setInt(1, idCiclista);
                        ps.setString(2, nombre);
                        ps.setString(3, nacionalidad);
                        ps.setInt(4, edad);
                        ps.setInt(5, id_equipo);

                        int n = ps.executeUpdate(); //devuelve el numero de registros que a añadido
                        System.out.println("Empleado insertado exitosamente" + n + "con ID: " + idCiclista);

                    }catch (SQLException e){
                        System.out.println("Error al añadir a la tabla" + e.getMessage());
                    }
                    break;

                case 2:
                    //actualizamos ciclista
                    try(Connection conexion = DriverManager.getConnection(url,usuario,password)){

                        System.out.println("Ingresa la edad que quieres actualizar: ");
                        int Edad = sc.nextInt();
                        System.out.println("Ingresa la id_ciclista del ciclista que vamos a actualizar                                                                                                                                              : ");
                        int idC = sc.nextInt();

                        String sql ="UPDATE CICLISTA SET EDAD = ? WHERE id_ciclista = ?";
                        PreparedStatement ps = conexion.prepareStatement(sql);
                        ps.setDouble(1, Edad);
                        ps.setInt(2, idC);
                        int n = ps.executeUpdate(); //devuelve el numero de registros que a añadido
                        System.out.println("Empleado actualizado exitosamente" + n);
                    }catch (SQLException e){
                        System.out.println("Error al añadir a la tabla" + e.getMessage());
                    }
                    break;
                case 3:

                    //Borramos ciclista
                    try(Connection conexion = DriverManager.getConnection(url,usuario,password)){

                        System.out.println("ID del ciclista a eliminar:");
                        int idCiclista = sc.nextInt();

                        boolean existe = false;

                        //Comprobar si el ciclista existe

                        String comprobar =
                                "SELECT COUNT(*) FROM CICLISTA WHERE ID_CICLISTA = ?";

                        PreparedStatement ps1 = conexion.prepareStatement(comprobar);

                        ps1.setInt(1,idCiclista);

                        ResultSet rs = ps1.executeQuery();

                        if(rs.next()){ //comprobamos que existe

                            if(rs.getInt(1) > 0){

                                existe = true;
                            }
                        }


                        if(existe){ //si existe se borra

                            // Eliminar primero en PARTICIPACION

                            String borrarParticipacion =
                                    "DELETE FROM PARTICIPACION WHERE ID_CICLISTA = ?";

                            PreparedStatement ps2 = conexion.prepareStatement(borrarParticipacion);

                            ps2.setInt(1,idCiclista);

                            int n1 = ps2.executeUpdate();

                            // Eliminar después en CICLISTA

                            String borrarCiclista = "DELETE FROM CICLISTA WHERE ID_CICLISTA = ?";

                            PreparedStatement ps3 = conexion.prepareStatement(borrarCiclista);

                            ps3.setInt(1,idCiclista);

                            int n2 = ps3.executeUpdate();

                            // Confirmación

                            System.out.println(
                                    "Participaciones eliminadas: " + n1);

                            System.out.println(
                                    "Ciclista eliminado correctamente: " + n2);

                        }
                        else{

                            System.out.println("El ciclista NO existe");

                        }

                    }catch(SQLException e){
                        System.out.println("Error al eliminar: " + e.getMessage());
                    }
                    break;
            }
        }while(dato != 0);
    }
}
