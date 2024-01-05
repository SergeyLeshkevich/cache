package ru.clevertec.servlet.car;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.config.app.SpringConfig;
import ru.clevertec.entity.data.CarDTO;
import ru.clevertec.exception.PageNotFoundException;
import ru.clevertec.service.CarService;
import ru.clevertec.util.YamlManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "cars-page-servlet", value = "/cars/page")
public class PageCarsServlet extends HttpServlet {

    public static final String APPLICATION_YAML = "application.yaml";
    private static final String LIMIT_CAR = "limit_car";
    private static final String NUMBER_PAGE = "numberPage";
    private static final int LIMIT_CARS = 20;
    private final CarService carService;
    private final Gson gson;

    public PageCarsServlet() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        carService = context.getBean(CarService.class);
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

