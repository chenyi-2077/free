<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String redirect = request.getContextPath() + "/login";
    response.sendRedirect(redirect);
%>
