<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link href="../../resources/studentAllstyle.css" type="text/css" rel="stylesheet" />
    <title>Singers</title>
</head>
<jsp:include page="../views/header.jsp" />
<body>
<h1>${result != null ? result : ""}</h1>
<form action="/singers" method="get">
    <div class="createWr1" >
        <h2> Information about singers</h2>
    </div>
    <div class="conteiner">
        <div class="label">Id</div>
        <div class="label">Name</div>
        <div class="label">Age</div>
        <div class="label">Producer</div>
    </div>
    <div>
        <c:forEach  items="${SingerList}" var ="singer">
            <div class="wrapper2">
                <div class="text">${singer.id}</div>
                <div class="text">${singer.singername}</div>
                <div class="text">${singer.singerage}</div>
                <div class="text">${singer.producer.fullname}</div>
                <div class="text">
                    <a type="button", class="btnAction", href="/singers/${singer.id}">Detail</a>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <a type="button", class="btnAction", href="/singers/delete/${singer.id}">Delete</a>
                    </security:authorize>
                </div>
            </div>
        </c:forEach>
    </div>
</form>

<security:authorize access="hasRole('ROLE_ADMIN')">
    <form action = "/singers" method = "post" >
        <div class="createWr1" >
            <h2> Add new singer</h2>
        </div>
        <div class="createWr3" >
            <div class="label12" > Name </div >
            <div class="label12" > Age </div >
            <div class="label12" > Producer </div >
        </div >
        <div class="createWr3" >
            <div class="space" ><input type = "text" name = "singername" required ></div >
            <div class="space" ><input type = "number" name = "singerage" required ></div >
            <div class="space" >
            <select name="producer" >
                <c:forEach items="${Producers}" var="producer">
                    <option value="${producer.id}">${producer.fullName}</option>
                </c:forEach>
            </select>
            </div >
        </div>
        <div class="forSubmit" >
            <button type = "submit" class="btn" > Submit </button >
        </div >
    </form>
</security:authorize>

</body>
<jsp:include page="../views/footer.jsp" />
</html>