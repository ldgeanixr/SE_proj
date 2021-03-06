package kz.edu.nu.cs.se.dao;

import kz.edu.nu.cs.se.model.TicketModel;
import kz.edu.nu.cs.se.service.EmailService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketController {
    public static Optional<Integer> BuyTicket(Integer scheduleId, Integer passengerId, Integer origin_id,
                                    Integer destination_id, Float price, String start_date,
                                    String end_date, String owner_document_type,
                                    Integer owner_document_id,
                                    String owner_first_name, String owner_last_name,
                                    String ticketStatus) {
        try {
            // Creates ticket on database
            System.out.println("HERE IN BUY TICKET");
            Statement statement = Connector.getStatement();

            statement.execute(String.format("INSERT INTO Ticket(" +
                            "Passenger_idPassenger, start_date, end_date, origin_id, " +
                            "destination_id, status, owner_document_type," +
                            "owner_first_name, owner_last_name, owner_document_id, " +
                            "price, schedule_id)" +
                            "VALUE(%d, \"%s\", \"%s\", %d, %d, \"%s\", \"%s\", \"%s\", \"%s\", %d, %d, %d)",
                    passengerId, start_date, end_date,
                    origin_id, destination_id, ticketStatus, owner_document_type,
                    owner_first_name, owner_last_name, owner_document_id,
                    price.intValue(),scheduleId));

            ResultSet ticketSet = statement.executeQuery(String.format("SELECT idTicket FROM Ticket WHERE Passenger_idPassenger = %d ORDER BY idTicket DESC LIMIT 1",
                            passengerId));

            // Increase capacity for all routes in the given range
            ArrayList<Integer> rangeIDs = RouteController.getRangeIDs(scheduleId, origin_id, destination_id);
            for (Integer id : rangeIDs) {
                RouteController.updatePassengerNumber(id);
            }

            while (ticketSet.next()) {
                Integer idTicket1 = ticketSet.getInt(1);
                return Optional.of(idTicket1);
            }

            ticketSet.close();
            statement.close();

            System.out.printf("[ERROR] Failed to FETCH ticket for passengerID: %d%n", passengerId);


        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        System.out.printf("[ERROR] Failed to BUY ticket for passengerID: %d%n", passengerId);
        return Optional.empty();
    }

    private static Optional<TicketModel> getTicketModel(ResultSet ticketSet) {
        try {
            Integer idTicket = ticketSet.getInt(1);
            Integer idPassenger = ticketSet.getInt(2);
            String starDate = ticketSet.getString(3);
            String endDate = ticketSet.getString(4);
            Integer originId = ticketSet.getInt(5);
            Integer destinationId = ticketSet.getInt(6);
            String status = ticketSet.getString(7);
            String owner_document_type = ticketSet.getString(8);
            String owner_first_name = ticketSet.getString(9);
            String owner_last_name = ticketSet.getString(10);
            String owner_document_id = ticketSet.getString(11);
            Integer price = ((int) ticketSet.getFloat(12));
            Integer agentId = ticketSet.getInt(13);
            Integer schedule_id = ticketSet.getInt(14);

            TicketModel ticketModel = new TicketModel(idTicket);

            ticketModel.setIdPassenger(idPassenger);
            ticketModel.setStartDate(starDate);
            ticketModel.setEndDate(endDate);
            ticketModel.setIdOrigin(originId);
            ticketModel.setIdDestination(destinationId);
            ticketModel.setStatus(status);
            ticketModel.setOwnerDocumentId(owner_document_id);
            ticketModel.setOwnerDocumentType(owner_document_type);
            ticketModel.setOwnerFirstName(owner_first_name);
            ticketModel.setOwnerLastName(owner_last_name);
            ticketModel.setPrice(price);
            ticketModel.setAgentId(agentId);
            ticketModel.setScheduleId(schedule_id);

            return Optional.of(ticketModel);
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return Optional.empty();
    }

    public static ArrayList<TicketModel> getTicketsForPassenger(Integer passengerID) {
        ArrayList<TicketModel> result = new ArrayList<>();
        try {
            Statement statement = Connector.getStatement();
            ResultSet ticketSet = statement.executeQuery(
                    String.format("SELECT * FROM Ticket WHERE Passenger_idPassenger=%d", passengerID));
            while (ticketSet.next()) {
                Optional<TicketModel> optionalTicketModel = getTicketModel(ticketSet);
                if (optionalTicketModel.isPresent()) {
                    result.add(optionalTicketModel.get());
                } else {
                    System.out.printf("[FAILED] Failed to fetch ticketModel for passengerID:%d%n", passengerID);
                }
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return result;
    }

    public static ArrayList<TicketModel> getTicketsForAgent(Integer agentID) {
        ArrayList<TicketModel> result = new ArrayList<>();

        try {
            Statement statement = Connector.getStatement();
            ResultSet ticketSet = statement.executeQuery(
                    String.format("SELECT * FROM Ticket WHERE agent_id=%d", agentID));
            while (ticketSet.next()) {
                Optional<TicketModel> optionalTicketModel = getTicketModel(ticketSet);
                if (optionalTicketModel.isPresent()) {
                    result.add(optionalTicketModel.get());
                } else {
                    System.out.printf("[FAILED] Failed to fetch ticketModel with agentID:%d%n", agentID);
                }
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return result;
    }

    public static ArrayList<TicketModel> getUnapprovedTickets(Integer stationID) {
        ArrayList<TicketModel> result = new ArrayList<>();
        try {
            Statement statement = Connector.getStatement();

            ResultSet ticketSet = statement.executeQuery(String.format(
                    "SELECT * FROM Ticket WHERE status=\"UNAPPROVED\" and agent_id is NULL and origin_id=%d", stationID)
            );

            while (ticketSet.next()) {
                Optional<TicketModel> optionalTicketModel = getTicketModel(ticketSet);
                if (optionalTicketModel.isPresent()) {
                    result.add(optionalTicketModel.get());
                } else {
                    System.out.printf("[FAILED] Failed to fetch ticketModel for stationID:%d%n", stationID);
                }
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return result;
    }

    public static Boolean assignTicketToAgent(Integer agentID, Integer ticketID) {
        Boolean result = false;
        try {
            Statement statement = Connector.getStatement();

            statement.execute(String.format(
                    "UPDATE Ticket SET agent_id=%d WHERE idTicket=%d AND agent_id is NULL", agentID, ticketID)
            );

            result = statement.execute(String.format(
                    "SELECT * FROM Ticket WHERE agent_id=%d AND idTicket=%d",agentID, ticketID));

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return result;
    }

    private static Boolean verifyStatus(Integer ticketID, String newStatus) {
        try {
            Statement statement = Connector.getStatement();

            ResultSet resultSet = statement.executeQuery("SELECT status FROM Ticket WHERE idTicket=" + ticketID);

            while (resultSet.next()) {
                String dataBaseStatus = resultSet.getString(1);
                return newStatus.equals(dataBaseStatus);
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return false;
    }

    public static Boolean changeStatus(Integer ticketID, String newStatus) {
        try {
            Statement statement = Connector.getStatement();

            statement.execute(String.format("UPDATE Ticket SET status=\"%s\", agent_id=NULL WHERE idTicket=%d",
                    newStatus, ticketID));

            statement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return verifyStatus(ticketID, newStatus);
    }

    public static Boolean verifyAssigmentOfAgent(Integer agentID, Integer ticketID) {
        Boolean status = false;

        try {
            Statement statement = Connector.getStatement();

            ResultSet ticketRows = statement.executeQuery(String.format(
                    "SELECT * FROM Ticket WHERE idTicket=%d AND agent_id=%d", ticketID, agentID));

            status = ticketRows.next();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return status;
    }

    public static Optional<TicketModel> getSingleTicket(Integer userID, Integer ticketID) {
        try {
            Statement statement = Connector.getStatement();

            ResultSet ticketSet = statement.executeQuery(
                    String.format("SELECT * FROM Ticket WHERE idTicket=%d AND Passenger_idPassenger=%d",
                            ticketID, userID));

            if (ticketSet.next()) return getTicketModel(ticketSet);

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return Optional.empty();
    }

    public static Boolean setTicketScheduleToNull(Integer scheduleId) {
        List<String> emailList = new ArrayList<>();
        try {
            Statement statement = Connector.getStatement();

            ResultSet emailSet = statement.executeQuery(String.format("SELECT email\n" +
                    "FROM Passenger, Ticket\n" +
                    "WHERE schedule_id = %d and Ticket.Passenger_idPassenger = Passenger.idPassenger", scheduleId));
            while (emailSet.next()) {
                String email = emailSet.getString(1);
                emailList.add(email);
            }

            EmailService.sendTicketCanceled(emailList);

            int deletedTicketsCount = statement.executeUpdate(String.format("UPDATE Ticket SET schedule_id=NULL, status='CANCELLED' WHERE schedule_id=%d", scheduleId));

            if(deletedTicketsCount <= 0) {
                System.out.println("[ERROR] Failed to tickets schedule_id to null");
                return false;
            }
            statement.close();

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return false;
        }

        return true;
    }

}