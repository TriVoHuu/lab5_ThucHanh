package Servlets;

import Objects.Account;
import Utils.HibernateUtils;
import org.hibernate.Session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirm_pass = req.getParameter("confirm_pass");

        String error = null;
        if(name == null || email == null || password == null || confirm_pass == null) {
            error = "Missing information";
            req.setAttribute("error", error);
            getServletContext().getRequestDispatcher("/register.jsp").forward(req,resp);
        } else if(!password.equals(confirm_pass)) {
            error = "Password not match";
            req.setAttribute("error", error);
            getServletContext().getRequestDispatcher("/register.jsp").forward(req,resp);
        }
        else {
            Account acc = new Account(email, name, password);
            Session session = HibernateUtils.getFactory().openSession();
            session.beginTransaction();

            session.save(acc);

            session.getTransaction().commit();

            HttpSession sessionHttp = req.getSession();
            sessionHttp.setAttribute("account", acc);
            getServletContext().getRequestDispatcher("/index").forward(req,resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("account") != null) {
            session.setAttribute("account", (Account) session.getAttribute("account"));
            req.getRequestDispatcher("/index").forward(req,resp);
            System.out.println((Account) session.getAttribute("account"));
        } else {
            // Nếu chưa đăng nhập thì cho hiển thị trang đăng ký
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
    }
}
