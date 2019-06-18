<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Managers</title>
    <link href="../../resources/studentAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<form action="/managers" method="get">
    <div class="createWr1" >
        <h2> Information about managers</h2>
    </div>
    <div class="conteiner">
        <div class="label">Id</div>
        <div class="label">Name</div>
        <div class="label">Sex</div>
        <div class="label">Age</div>
        <div class="label">Manager type</div>
    </div>
    <div>
        <c:forEach  items="${ManagerList}" var ="manager">
            <div class="wrapper2">
                <div class="text">${manager.id}</div>
                <div class="text">${manager.fullName}</div>
                <div class="text">${manager.male}</div>
                <div class="text">${manager.age}</div>
                <div class="text">${manager.managerType}</div>
                <div class="text">
                    <a type="button", class="btnAction", href="/managers/${manager.id}">Detail</a>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <a type="button", class="btnAction", href="/managers/delete/${manager.id}">Delete</a>
                    </security:authorize>
                </div>
            </div>
        </c:forEach>
    </div>
</form>

<security:authorize access="hasRole('ROLE_ADMIN')">
    <form action = "/managers" method = "post" >
        <div class="createWr1" >
            <h2> Add new manager</h2>
        </div>
        <div class="createWr2" >
            <div class="label13">Name</div>
            <div class="label13">Sex</div>
            <div class="label13">Age</div>
            <div class="label13">Type</div>
        </div >
        <div class="createWr3" >
            <div class="space" ><input type = "text" name = "name" required ></div >
            <div class="space">
                <select name="male" id="maleSelect" required class="form-control">
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                </select>
            </div>
            <div class="space" ><input type = "number" name = "age" required ></div >
        <div class="space">
            <select name="managerType" id="typeSelect" required class="form-control">
                <c:forEach items="${ManagerTypes}" var="type">
                    <option value="${type}">${type}</option>
                </c:forEach>
            </select>
        </div>
        </div >

        <div class="forSubmit" >
            <button type = "submit" class="btn" > Submit </button >
        </div >
    </form>
</security:authorize>
</body>
<jsp:include page="../views/footer.jsp" />
</html>
