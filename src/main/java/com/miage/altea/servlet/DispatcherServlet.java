package com.miage.altea.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miage.altea.controller.HelloController;
import com.miage.altea.annotations.Controller;
import com.miage.altea.annotations.RequestMapping;
import com.miage.altea.controller.PokemonTypeController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/*", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private Map<String, Method> uriMappings = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Getting request for " + req.getRequestURI());
        var objectMapper = new ObjectMapper();
        String uri = req.getRequestURI();
        Method calledMethod = this.uriMappings.get(uri);
        if (calledMethod != null) try {
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            Object result = null;
            if (calledMethod.getParameterCount() == 0)
            {
                result = calledMethod.invoke(calledMethod.getDeclaringClass().getDeclaredConstructor().newInstance());

            }else{
                result = calledMethod.invoke(calledMethod.getDeclaringClass().getDeclaredConstructor().newInstance(), req.getParameterMap());

            }
            Map<String, String[]> parameters = (req.getParameterMap().isEmpty() ? null : req.getParameterMap());
            resp.getWriter().print(objectMapper.writeValueAsString(result));
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "exception when calling method someThrowingMethod : some exception message");
        }
        else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "no mapping found for request uri " + req.getRequestURI());
        }

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // on enregistre notre controller au démarrage de la servlet
        this.registerController(PokemonTypeController.class);
    }

    protected void registerController(Class controllerClass) {

        System.out.println("Analysing class " + controllerClass.getName());
        if (controllerClass.getAnnotation(Controller.class) != null) {
            for (Method method : controllerClass.getMethods()) {
                this.registerMethod(method);
            }
        } else {
            throw new IllegalArgumentException("Not a controller class");
        }

    }

    protected void registerMethod(Method method) {
        System.out.println("Registering method " + method.getName());
        if (method.getAnnotation(RequestMapping.class) != null && method.getReturnType() != void.class) {
            this.uriMappings.put(method.getAnnotation(RequestMapping.class).uri(), method);
        }
        // TODO
    }

    protected Map<String, Method> getMappings() {
        return this.uriMappings;
    }

    protected Method getMappingForUri(String uri) {
        return this.uriMappings.get(uri);
    }
}
