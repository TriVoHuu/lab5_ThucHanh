package Servlets;

import Objects.Account;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Xóa session
        HttpSession session = req.getSession(false); // Lấy session hiện tại, nếu không có thì trả về null
        if (session != null) {
            session.invalidate(); // Hủy session
        }

// Xóa cookie
        Cookie[] cookies = req.getCookies(); // Lấy danh sách cookie
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0); // Thiết lập thời gian sống của cookie về 0
                resp.addCookie(cookie); // Gửi cookie về phía client để xóa cookie ở phía client
            }
        }

        getServletContext().getRequestDispatcher("/login.jsp").forward(req,resp);
    }
}
