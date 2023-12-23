package ru.clevertec.servlet.car;

import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.entity.data.CarDTO;
import ru.clevertec.exception.CarNotFoundException;
import ru.clevertec.service.CarService;
import ru.clevertec.servlet.car.config.FabricCarService;
import ru.clevertec.util.FileHandler;
import ru.clevertec.util.constant.FieldsCarConstants;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "car-servlet", value = "/cars/file")
public class CarPrintServlet extends HttpServlet {

    private transient CarService carService;
    private transient Gson gson;

    @Override
    public void init() throws ServletException {
        carService = FabricCarService.getInstance().getCarService();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        UUID idCar = UUID.fromString(req.getParameter(FieldsCarConstants.UUID_CAR));
        PrintWriter out = resp.getWriter();
        try {
            CarDTO car = carService.get(idCar);
            String json = gson.toJson(car);
            FileHandler fileHandler = new FileHandler();
            ServletContext context = getServletContext();
            fileHandler.writeTableToFilePDF(context.getRealPath("/WEB_INF/classes/") + idCar + ".pdf",
                    List.of(car),
                    "Car from DB");
            resp.setStatus(HttpServletResponse.SC_OK);
            out.println(json);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (CarNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}
