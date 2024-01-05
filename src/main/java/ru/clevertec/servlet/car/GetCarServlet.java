package ru.clevertec.servlet.car;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.config.app.SpringConfig;
import ru.clevertec.entity.data.CarDTO;
import ru.clevertec.enums.BodyType;
import ru.clevertec.enums.Fuel;
import ru.clevertec.exception.CarNotFoundException;
import ru.clevertec.service.CarService;
import ru.clevertec.util.constant.FieldsCarConstants;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@WebServlet(name = "get-car-servlet", value = "/cars")
public class GetCarServlet extends HttpServlet {
    private final CarService carService;
    private final Gson gson;

    public GetCarServlet() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        carService = context.getBean(CarService.class);
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();

        try {
            UUID uuidCar = UUID.fromString(req.getParameter(FieldsCarConstants.UUID_CAR));
            CarDTO carDTO = carService.get(uuidCar);
            String json = gson.toJson(carDTO);
            resp.setStatus(HttpServletResponse.SC_OK);
            out.println(json);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (CarNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        String brand = req.getParameter(FieldsCarConstants.BRAND);
        String model = req.getParameter(FieldsCarConstants.MODEL);
        BodyType bodyType = BodyType.valueOf(req.getParameter(FieldsCarConstants.BODY_TYPE).toUpperCase());
        double engineCapacity = Double.parseDouble(req.getParameter(FieldsCarConstants.ENGINE_CAPACITY));
        Fuel fuelType = Fuel.valueOf(req.getParameter(FieldsCarConstants.FUEL_TYPE).toUpperCase());
        CarDTO newCar = new CarDTO(brand, model, bodyType, engineCapacity, fuelType);

        UUID uuidCar = carService.create(newCar);
        CarDTO carFromDB = carService.get(uuidCar);
        String json = gson.toJson(carFromDB);
        out.println(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            UUID uuidCar = UUID.fromString(req.getParameter(FieldsCarConstants.UUID_CAR));
            carService.delete(uuidCar);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            UUID uuid = java.util.UUID.fromString(req.getParameter(FieldsCarConstants.UUID_CAR));
            String model = req.getParameter(FieldsCarConstants.MODEL);
            String brand = req.getParameter(FieldsCarConstants.BRAND);
            double engineCapacity = Double.parseDouble(req.getParameter(FieldsCarConstants.ENGINE_CAPACITY));
            BodyType bodyType = BodyType.valueOf(req.getParameter(FieldsCarConstants.BODY_TYPE).toUpperCase());
            Fuel fuelType = Fuel.valueOf(req.getParameter(FieldsCarConstants.FUEL_TYPE).toUpperCase());
            CarDTO carDTO = new CarDTO(brand, model, bodyType, engineCapacity, fuelType);
            PrintWriter out = resp.getWriter();

            carService.update(uuid, carDTO);

            out.println("Car with id: " + uuid + " is update");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (CarNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}
