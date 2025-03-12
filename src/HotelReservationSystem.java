import javax.swing.plaf.nimbus.State;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;
public class HotelReservationSystem {
    private  static  final String url="jdbc:mysql://localhost:3306/hotel_db";
    private static  final  String password="MDbihar@123";
    private static  final  String username="root";

    public static void main(String[] args) throws ClassNotFoundException , Exception{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connect Establish Successsfully");

        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection connection=DriverManager.getConnection(url,username,password);
            Statement statement= connection.createStatement();
            while(true){
                System.out.println();
                System.out.println("Hotel Management System");
                Scanner sc=new Scanner(System.in);
                System.out.println("1: Reserva a room");
                System.out.println("2: View Reservation");
                System.out.println("3: Get room number");
                System.out.println("4: Update Reservation");
                System.out.println("5: Delete Reservation");
                System.out.println("0: Exit");
                System.out.println();
                System.out.println("Choose an option:");
                int choice=sc.nextInt();

                switch (choice){
                    case 1: reserveRoom(connection,sc,statement);
                    break;
                    case 2: viewReservations(connection,statement);
                    break;
                    case 3: getRoomNumber(connection,sc,statement);
                    break;
                    case 4: updateReservation(connection,sc,statement);
                    break;
                    case 5: deleteReservation(connection,sc,statement);
                    break;
                    case 0: exit();
                           sc.close();
                           return;
                    default:
                        System.out.println("Invalid Choice . Try Again");
                }
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private  static  void reserveRoom(Connection connection, Scanner sc ,Statement statement){
       try{
           System.out.println("Enter guest name");
           String guestName=sc.next();
           sc.nextLine();
           System.out.println("Enter room number");
           int roomNumber=sc.nextInt();
           System.out.println("Enter contact number");
           String contactNumber=sc.next();

           String sql="insert into reservations (guest_name,room_number,contact_Number) "+
                       "values('" + guestName+ "',"+roomNumber+",'"+contactNumber+"')";
           try {
               int affectedRows=statement.executeUpdate(sql);
               if(affectedRows>0){
                   System.out.println("Reservation successfull");
               }else {
                   System.out.println("Reservation Failed");
               }
           }catch(SQLException e){
                e.printStackTrace();
           }

       }catch(Exception e){
           System.out.println(e.getMessage());
       }
    }
    private  static  void viewReservations(Connection connection ,Statement statement){

        String sql="Select reservation_id, guest_name,room_number,contact_number,reservation_date from reservations";
        try (ResultSet resultSet=statement.executeQuery(sql)){
            System.out.println("Current reservation");
            System.out.println("+----------------+------------+-----------------+--------------------+---------------------------+");
            System.out.println("| Reservation ID | Guest      | Room number     | Contact number     |  Reservation  Date         |");
            System.out.println("+----------------+------------+-----------------+--------------------+----------------------------+");

            while (resultSet.next()){
                int reservationId=resultSet.getInt("reservation_id");
                String  guestName=resultSet.getString("guest_name");
                int roomNumber= resultSet.getInt("room_number");
                String contactNumber=resultSet.getString("contact_number");
                String reservationDate=resultSet.getTimestamp("reservation_date").toString();

                //Format and display the reservation date in a table like format
//                System.out.println("| %-14d | %-15s | %-13d | %-20s | %-19s |\n",
//                        reservationId+guestName+roomNumber+contactNumber,reservationDate);
                System.out.println("Reservation Id"+reservationId);
                System.out.println("Guest Name"+guestName);
                System.out.println("Guest Room Number"+roomNumber);
                System.out.println("Guest Contact Number "+contactNumber);
                System.out.println("Reservation Date"+reservationDate);
            }
            System.out.println("+-------------------+--------------+----------------+---------------------+------------------------+");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    private  static  void getRoomNumber(Connection connection, Scanner sc ,Statement statement){
        try{
            System.out.println("Enter reservation Id ");
            int reservationId=sc.nextInt();
            System.out.println("Enter guest name");
            String guestName=sc.next();

            String sql="select room_number from reservations where reservation_id="+reservationId+"" +
                    "and guest_name='"+guestName+"'";
            try{
                ResultSet resultSet=statement.executeQuery(sql);
                if(resultSet.next()){
                    int roomNumber=resultSet.getInt("room_number");
                    System.out.println("Room number for reservation ID "+reservationId+"and guest"+guestName+
                    "is"+ roomNumber);
                }else {
                    System.out.println("Reservation not found for the given ID and guest name");
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private  static  void updateReservation(Connection connection, Scanner sc ,Statement statement){
        try{
            System.out.println("Enter resrvation Id to update :");
            int reservationId=sc.nextInt();
            if(!reservationsExists(connection,reservationId)){
                System.out.println("Reservation not found for given Id");
                return;
            }
            System.out.println("Enter new guest name:");
            String newGuestName=sc.nextLine();
            System.out.println("Enter new room number");
            int newRoomNumber=sc.nextInt();
            System.out.println("Enter new contact number");
            String newContactNumber=sc.next();

            String sql="update reservations set guest_name='"+newGuestName+"',"+
                    "room_number="+newRoomNumber+","+
                    "contact_number="+newContactNumber+"'"+
                    "where reservation_id="+reservationId;

            try {
                 int affectedRows=statement.executeUpdate(sql);
                 if(affectedRows>0){
                     System.out.println("Reservation Updated succesffully");
                 }else{
                     System.out.println("Reservation update failed");
                 }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private  static  void deleteReservation(Connection connection,Scanner sc,Statement statement){
       try{
           System.out.println("Enter reservation for delete reservation");
           int reservationId=sc.nextInt();
           if(!reservationsExists(connection,reservationId)){
               System.out.println("Reservation not found for given ID");
           }
           String sql="Delete from reservations where reservation_id="+reservationId;
           try{
               int affectedRows=statement.executeUpdate(sql);
               if(affectedRows>0){
                   System.out.println("Reservation delete successfully");
               }else {
                   System.out.println("Reservation deletion failed");
               }
           }catch (Exception e){
               System.out.println(e.getMessage());
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }
    private static boolean reservationsExists(Connection connection,int reservationId ){
        try{
            String sql="select reservation_id from reservations where reservation_id="+reservationId;
            try(Statement statement = connection.createStatement();
                ResultSet resultSet=statement.executeQuery(sql)){
                 return resultSet.next();
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    private  static  void exit() throws  InterruptedException{
        System.out.print("Exiting System");
        int i=5;
        while (i!=0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thank you for using Hotel Reservation System");
    }
}
