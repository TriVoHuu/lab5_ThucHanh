package Servlets;

import Objects.Account;
import Objects.Product;
import Utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "DeleteServlet", urlPatterns = "/delete")
public class DeleteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/index.jsp").forward(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("id") != null) {
            String id = req.getParameter("id");
            Session session = HibernateUtils.getFactory().openSession();
            try {
                session.beginTransaction();
                Product product = (Product) session.createQuery("from Product where id = "+id).uniqueResult();
                Query query = session.createQuery("delete from Product where id = :id");
                query.setParameter("id", Integer.parseInt(id));
                query.executeUpdate();

                HttpSession sessionHttp = req.getSession();
                Account acc = (Account) sessionHttp.getAttribute("account");
                Account account = (Account) session.createQuery("from Account where email = '"+acc.getEmail()+"'").uniqueResult();

                account.getProducts().remove(product);
                session.update(account);
                sessionHttp.setAttribute("account",account);
                session.getTransaction().commit();
            } catch (Exception e) {
            }

        }

        HttpSession session = req.getSession();
        Account acc = (Account) session.getAttribute("account");

        req.setAttribute("name", acc.getName());
        req.setAttribute("listProducts",acc.getProducts());

        getServletContext().getRequestDispatcher("/index.jsp").forward(req,resp);
    }
}
