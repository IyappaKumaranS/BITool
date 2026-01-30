package com.bichart.servlet;

import jakarta.servlet.http.*;
import jakarta.servlet.*;

public class TestServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, java.io.IOException {
        res.getWriter().write("OK");
    }
}
