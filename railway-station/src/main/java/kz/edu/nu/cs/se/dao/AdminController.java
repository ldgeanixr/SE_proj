package kz.edu.nu.cs.se.dao;

import kz.edu.nu.cs.se.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class AdminController {
    public static Optional<User> getAdmin(String username, String pass){
        Statement adminStatement = Connector.getStatement();

        try {
            ResultSet agentSet = adminStatement.executeQuery(String.format("SELECT * FROM Agent where username = '%s' and password = '%s' limit 1", username, pass));
            while(agentSet.next()){
                int userId = agentSet.getInt(1);
                String firstName = agentSet.getString(2);
                String lastName= agentSet.getString(3);
                String email= agentSet.getString(4);
                String phoneNumber= agentSet.getString(5);
                String userName= agentSet.getString(6);

                User user = new User(firstName,lastName,email,phoneNumber,userName,userId);
                user.setUserRole("agent");
            }

            ResultSet managerSet = adminStatement.executeQuery(String.format("SELECT * FROM Manager where username = '%s' and password = '%s' limit 1", username, pass));
            while(managerSet.next()){
                int userId = managerSet.getInt(1);
                String firstName = managerSet.getString(2);
                String lastName= managerSet.getString(3);
                String email= managerSet.getString(4);
                String phoneNumber= managerSet.getString(5);
                String userName= managerSet.getString(6);

                User user = new User(firstName,lastName,email,phoneNumber,userName,userId);
                user.setUserRole("manager");
                adminStatement.close();
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
