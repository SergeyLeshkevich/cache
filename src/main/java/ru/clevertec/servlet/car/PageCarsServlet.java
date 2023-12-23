package ru.clevertec.servlet.car;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.entity.data.CarDTO;
import ru.clevertec.exception.PageNotFoundException;
import ru.clevertec.service.CarService;
import ru.clevertec.servlet.car.config.FabricCarService;
import ru.clevertec.util.YamlManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "cars-page-servlet", value = "/cars/page")
public class PageCarsServlet extends HttpServlet {

    public static final String APPLICATION_YAML = "\\application.yaml";
    private static final String LIMIT_CAR = "limit_car";
    private static final String NUMBER_PAGE = "numberPage";
    private static final int LIMIT_CARS = 20;
    private transient CarService carService;
    private transient Gson gson;

    @Override
    public void init() throws ServletException {
        carService = FabricCarService.getInstance().getCarService();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer limitCars = (Integer) new YamlManager().getValue(APPLICATION_YAML).get(LIMIT_CAR);
        if (limitCars == null) {
            limitCars = LIMIT_CARS;
        }
        int numberPage = Integer.parseInt(req.getParameter(NUMBER_PAGE));
        int numberStartSelection = numberPage * limitCars - limitCars;

        PrintWriter out = resp.getWriter();
        try {
            List<CarDTO> dtoList = carService.findLimitList(limitCars, numberStartSelection);
            if (dtoList.isEmpty()) {
                throw new PageNotFoundException("page not found");
            }
            String json = gson.toJson(dtoList);
            resp.setStatus(HttpServletResponse.SC_OK);
            out.println(json);
        } catch (PageNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            out.println(e.getMessage());
        }
    }
}

