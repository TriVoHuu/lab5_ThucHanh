package Servlets;

import Objects.Account;
import Utils.HibernateUtils;
import org.hibernate.Session;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember");
        boolean isRememberMe = remember != null && remember.equals("true");
        String errorMessage = null;

        if (isRememberMe) {
            // create a cookie for the username
            Cookie usernameCookie = new Cookie("username", username);
            usernameCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days in seconds
            resp.addCookie(usernameCookie);

            // create a cookie for the password
            Cookie passwordCookie = new Cookie("password", password);
            passwordCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days in seconds
            resp.addCookie(passwordCookie);
        } else {
            // remove any existing username and password cookies
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("username") || cookie.getName().equals("password")) {
                        cookie.setMaxAge(0);
                        resp.addCookie(cookie);
                    }
                }
            }
        }

        // Check if username and password are provided
        if (username == null || username.trim().length()==0 || password == null || password.trim().length()==0 ) {
            errorMessage = "Username and password are required.";
            req.setAttribute("error",errorMessage);
            getServletContext().getRequestDispatcher("/login.jsp").forward(req,resp);

        } else {
            // Check if username and password are correct
            Account authenticated;
            try {
                authenticated = authenticateUser(username, password);
                if (authenticated == null) {
                    errorMessage = "Name/Password does not match";
                    req.setAttribute("error", errorMessage);
                    getServletContext().getRequestDispatcher("/register.jsp").forward(req,resp);
                } else {
                    HttpSession session = req.getSession();
                    session.setAttribute("account", authenticated);
                    getServletContext().getRequestDispatcher("/index").forward(req,resp);
                }
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
            // Nếu chưa đăng nhập thì cho hiển thị trang đăng nhập
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

    private Account authenticateUser(String username, String password)throws ClassNotFoundException {
        Session session = HibernateUtils.getFactory().openSession();
        session.beginTransaction();

        List<Account> ls = session.createQuery("SELECT p FROM Account p WHERE p.email = '"+username+"' and p.password ='"+password+"'", Account.class).getResultList();
        session.getTransaction().commit();
        if(!ls.isEmpty()) {
            return ls.get(0);
        } else return null;
    }
}
