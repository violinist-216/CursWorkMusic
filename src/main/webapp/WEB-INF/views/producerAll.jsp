<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Producers</title>
    <link href="../../resources/studentAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<h1>${result != null ? result : ""}</h1>
<form action="/producers" method="get">
    <div class="createWr1" >
        <h2> Information about producers</h2>
    </div>
    <div class="conteiner">
        <div class="label">Id</div>
        <div class="label">Name</div>
        <div class="label">Sex</div>
        <div class="label">Age</div>
        <div class="label">Experience</div>
    </div>
    <div>
        <c:forEach  items="${ProducerList}" var ="producer">
            <div class="wrapper2">
                <div class="text">${producer.id}</div>
                <div class="text">${producer.fullName}</div>
                <div class="text">${producer.male}</div>
                <div class="text">${producer.age}</div>
                <div class="text">${producer.experience}</div>
                <div class="text">
                    <a type="button", class="btnAction", href="/producers/${producer.id}">Detail</a>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <a type="button", class="btnAction", href="/producers/delete/${producer.id}">Delete</a>
                    </security:authorize>
                </div>
            </div>
        </c:forEach>
    </div>
</form>

<security:authorize access="hasRole('ROLE_ADMIN')">
    <form action = "/producers" method = "post" >
        <div class="createWr1" >
            <h2> Add new producer</h2>
        </div>
        <div class="createWr3" >
            <div class="label14">Name</div>
            <div class="label14">Sex</div>
            <div class="label14">Age</div>
            <div class="label14">Experience</div>
        </div >
        <div class="createWr3" >
            <div class="space" ><input type = "text" name = "fullName" required ></div >
            <div class="space">
                    <select name="male" id="maleSelect" required class="form-control">
                        <option value="MALE">Male</option>
                        <option value="FEMALE">Female</option>
                    </select>
            </div>
            <div class="space" ><input type = "number" name = "age" required ></div >
            <div class="space" ><input type = "number" name = "experience" required ></div >
        </div >

        <div class="forSubmit" >
            <button type = "submit" class="btn" > Submit </button >
        </div >
    </form>
</security:authorize>
</body>
<jsp:include page="../views/footer.jsp" />
</html>
