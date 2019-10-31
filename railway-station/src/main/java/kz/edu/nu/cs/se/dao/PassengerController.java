package kz.edu.nu.cs.se.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
//import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import kz.edu.nu.cs.se.model.*;

import javax.validation.Payload;
import java.security.interfaces.RSAPublicKey;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;



import java.util.Date;



public class PassengerController {

    public static boolean isValidUserName(String userName){

        Statement passengerStatement = Connector.getStatement();

        try {
            ResultSet passengerSet = passengerStatement.executeQuery(String.format("SELECT * FROM Passenger where username = '%s'", userName));
            System.out.println(userName);
            while(passengerSet.next()){
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;

    }

    public static void addPassenger(Passenger passenger){
        Statement passengerStatement = Connector.getStatement();

        String firstName = passenger.getFirstName();
        String lastName= passenger.getLastName();
        String email= passenger.getEmail();
        String phoneNumber= passenger.getPhoneNumber();
        String userName= passenger.getUserName();
        String password= passenger.getPassword();

        String values = firstName+ "," +lastName+ "," +email+ "," +phoneNumber+ "," +userName+ "," +password;

        System.out.println(values);

        String queryInsert = String.format("Insert into Passenger(firstname, lastname, email, phone_number, username, password) Values('%s','%s','%s','%s','%s','%s')",
                firstName,lastName,email,phoneNumber,userName,password);

        try {
            passengerStatement.executeUpdate(queryInsert);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isPassengerUsername(String username) {
        try {
            Statement statement = Connector.getStatement();

            ResultSet passengerSet = statement.executeQuery("SELECT username FROM Passenger WHERE username="+username);
            while (passengerSet.next()) {
                Boolean userName = passengerSet.getBoolean(1);
                if (userName == true) return true;
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean isPassengerPassword(String password) {
        try {
            Statement statement = Connector.getStatement();

            ResultSet passengerSet = statement.executeQuery("SELECT password FROM Passenger WHERE password="+password);
            while (passengerSet.next()) {
                Boolean pw = passengerSet.getBoolean(1);
                if (pw == true) return true;
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }


    public static int getPID() {
        try {
            Statement statement = Connector.getStatement();

            ResultSet passengerSet = statement.executeQuery("SELECT idPassenger FROM Passenger");
            while (passengerSet.next()) {
                Integer  passengerId = passengerSet.getInt(1);
                return passengerId;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return -1;
    }


//    public static String generateToken(Passenger passenger) {
//
//        Date date = new Date();
//        Date date2 = new Date();
//        date2.setMinutes(date.getMinutes() + 60);
//
//
//
//        try {
//            Algorithm algorithm = Algorithm.HMAC256("secret");
//            String token = JWT.create()
//                    .withIssuer("auth0")
//                    .withClaim("first_name", passenger.getFirstName())
//                    .withClaim("last_name", passenger.getLastName())
//                    .withClaim("email", passenger.getEmail())
//                    .withClaim("phone_number", passenger.getPhoneNumber())
//                    .withClaim("user_name", passenger.getUserName())
//                    .withClaim("password", passenger.getPassword())
//                    .withIssuedAt(date)
//                    .withExpiresAt(date2)
//                    .sign(algorithm);
//            return token;
//        } catch (JWTCreationException exception){
//            //Invalid Signing configuration / Couldn't convert Claims.
//        }
//
//        return "error";
//
//    }

//    public static Passenger getPassengerFromToken(String token){
//        DecodedJWT jwt = JWT.decode(token);
//        String firstName = jwt.getClaim("first_name").asString();
//        String lastName = jwt.getClaim("last_name").asString();
//        String email = jwt.getClaim("email").asString();
//        String phoneNumber = jwt.getClaim("phone_number").asString();
//        String userName = jwt.getClaim("user_name").asString();
//        String password = jwt.getClaim("password").asString();
//
//        return new Passenger(firstName,lastName,email,phoneNumber,userName,password);
//    }
}
