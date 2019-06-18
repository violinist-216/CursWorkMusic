<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Song info</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<jsp:include page="../views/header.jsp" />
<body>
<h1>${error != null ? error : "Information about manager:"}</h1>
<form action="/managers/{id}" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Name</div>
        <div class="label">Sex</div>
        <div class="label">Age</div>
        <div class="label">Manager type</div>
    </div>
    <div class="wrapper2">
        <div class="text">${manager.id}</div>
        <div class="text">${manager.fullName}</div>
        <div class="text">${manager.male}</div>
        <div class="text">${manager.age}</div>
        <div class="text">${manager.managetType}</div>
    </div>
</form>

<security:authorize access="hasRole('ROLE_ADMIN')">
    <form action = "/managers/${Song.id}" method = "post" >
        <div class="updateWr1" >
            <h2 > Update song</h2>
        </div >
        <div class="updateWr2" >
            <div class="label">Name</div>
            <div class="label">Sex</div>
            <div class="label">Age</div>
            <div class="label">Manager type</div>
        </div >
        <div class="updateWr3" >
            <div class="space" ><input type = "text" name = "name" required value="${Manager.fullName}"></div >
            <div class="space">
                <select required name="Male">
                    <c:forEach items="${Male}" var="male" >
                        <option value="${male}">${male}</option>
                    </c:forEach>
                </select>
            </div >
            <div class="space" ><input type = "number" name = "age" required value="${Manager.age}" ></div >
            <div class="space">
                <select name="managerType" id="typeSelect" required class="form-control">
                    <c:forEach items="${ManagerTypes}" var="type">
                        <option value="${type}">${type}</option>
                    </c:forEach>
                </select>
            </div >
        </div >

        <div class="forSubmit" >
            <button type = "submit" class="btn" > Submit </button >
        </div >
    </form>
</security:authorize>
<div class="goBack" >
    <button class="btn" onclick="location.href='/menu'">Menu</button>
</div >

</body>
<jsp:include page="../views/footer.jsp" />
</html>

