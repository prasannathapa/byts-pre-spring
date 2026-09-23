package demo;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** One instance of this object answers every request, on whatever thread the container has free. */
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/plain;charset=UTF-8");
        res.getWriter().print("hello " + req.getParameter("name") + ", from thread " + Thread.currentThread().getName());
    }
}
