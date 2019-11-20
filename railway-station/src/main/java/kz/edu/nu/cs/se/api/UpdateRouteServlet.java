package kz.edu.nu.cs.se.api;

import com.google.gson.Gson;
import kz.edu.nu.cs.se.api.utils.DeleteScheduleObject;
import kz.edu.nu.cs.se.api.utils.UpadateRouteObject;
import kz.edu.nu.cs.se.dao.RouteController;
import kz.edu.nu.cs.se.dao.ScheduleController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static kz.edu.nu.cs.se.api.utils.JWTUtils.isExpired;
import static kz.edu.nu.cs.se.api.utils.JWTUtils.isManager;

@WebServlet(urlPatterns = {"/myrailway/manager/update-route"})
public class UpdateRouteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UpadateRouteObject updateRouteObject = new Gson().fromJson(req.getReader(),
                UpadateRouteObject.class);

        System.out.println("I am before token check");

        String token = updateRouteObject.getToken();
        if (isExpired(token)){
            System.out.println("[ERROR] Token has expired");
            resp.sendError(401, "Token has expired");
            return;
        }

        if (!isManager(token)){
            System.out.println("[ERROR] Unauthorized as Manager");
            resp.sendError(401, "Unauthorized as Manager");
            return;
        }
        System.out.println("I am after token check");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(updateRouteObject.getStartTime(), formatter);
        LocalDateTime endTime = LocalDateTime.parse(updateRouteObject.getEndTime(), formatter);
        Integer routeId = updateRouteObject.getRouteId();

        System.out.println("I am after parsing");

        Boolean status = RouteController.updateRoute(routeId, startTime, endTime);

        System.out.println("I am after status");


        if (status) {
            resp.sendError(202, "Success");
        } else {
            resp.sendError(503, "An internal error occurred");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);

        DeleteScheduleObject deleteScheduleObject = new Gson().fromJson(req.getReader(), DeleteScheduleObject.class);

        String token = deleteScheduleObject.getToken();
        if (isExpired(token)){
            System.out.println("[ERROR] Token has expired");
            resp.sendError(401, "Token has expired");
            return;
        }

        if (!isManager(token)){
            System.out.println("[ERROR] Unauthorized as Manager");
            resp.sendError(401, "Unauthorized as Manager");
            return;
        }

        Integer scheduleId = deleteScheduleObject.getScheduleId();

        Boolean status = ScheduleController.deleteSchedule(scheduleId);

        if (status) {
            resp.sendError(202, "Success");
        } else {
            resp.sendError(503, "An internal error occurred");
        }

    }
}