package Servlets;

import Objects.Account;
import Objects.Product;
import Utils.HibernateUtils;
import org.hibernate.Session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "AddServlet", urlPatterns = "/add")
public class AddServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("name") != null && req.getParameter("name").length()>0) {
            String name = req.getParameter("name");
            double price = Double.parseDouble(req.getParameter("price"));

            HttpSession sessionHttp = req.getSession();
            Account acc = (Account) sessionHttp.getAttribute("account");

            Product product = new Product(name, price);
            product.setAccount(acc);

            Session session = HibernateUtils.getFactory().openSession();
            session.beginTransaction();

            session.save(product);

            Account account = (Account) session.createQuery("from Account where email = '"+acc.getEmail()+"'").uniqueResult();

            account.getProducts().add(product);
            session.update(account);
            sessionHttp.setAttribute("account",account);
            session.getTransaction().commit();
        }
        HttpSession session = req.getSession();
        Account acc = (Account) session.getAttribute("account");

        req.setAttribute("name", acc.getName());
        req.setAttribute("listProducts",acc.getProducts());
        System.out.println(acc.getName());
        getServletContext().getRequestDispatcher("/index.jsp").forward(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        resp.sendRedirect(contextPath+"/login");
    }
}
