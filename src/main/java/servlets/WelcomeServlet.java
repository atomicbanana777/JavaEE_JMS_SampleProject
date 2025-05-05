package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import ejbs.ConvertorBean;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {

    @EJB
    ConvertorBean convertor;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        
        BigDecimal input = new BigDecimal("3000");
        BigDecimal result = convertor.dollarToYen(input);
        
        PrintWriter out = response.getWriter();
        out.println("This is came from Welcome Servlet!");
        out.println(input + " dollar can convert to " + result + " yen!");
    }
    
}
