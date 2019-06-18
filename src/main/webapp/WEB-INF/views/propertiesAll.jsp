<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
            <!DOCTYPE>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                <title>Messages</title>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <link href="../../resources/studentAllstyle.css" type="text/css" rel="stylesheet" />
            </head>
            <jsp:include page="../views/header.jsp" />

            <body>
                <security:authorize access="hasRole('ROLE_ADMIN')">
                    <div class="container col-8 justify-content-center">
                        <h3>${result != null ? result : ""}</h3>
                        <div class="createWr1">
                            <h2>All properties from all modules</h2>
                        </div>
                        <div class="createWr1"><table class="table ">
                            <tr>
                                <th scape="col">Music service's properties</th>
                                <td class="text" >${school}</td>
                            </tr>
                            <tr>
                                <th scape="col">Label service's properties:</th>
                                <td class="text">${lesson}</td>
                            </tr>
                            <tr>
                                <th scape="col">Server's properties:</th>
                                <td class="text">${server}</td>
                            </tr>
                            <tr>
                                <th scape="col">Client's properties:</th>
                                <td class="text">${client}</td>
                            </tr>
                        </table>
                        </div>
                        <form action="/updateAllProperties" method="post">
                            <div class="createWr1">
                            <h2>Update all properties from all modules</h2>
                            </div>
                            <div class="forSubmit" >
                                <button type = "submit" class="btn" > Do bus-refresh </button >
                            </div >
                        </form>
                </security:authorize>
                </div>
            </body>
            <jsp:include page="../views/footer.jsp" />

            </html>