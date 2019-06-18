<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Producer info</title>
    <link href="../../resources/studentAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<h1>${error != null ? error : ""}</h1>
<form action="/producers/{id}" method="get">
    <div class="createWr1" >
        <h2> Producers details</h2>
    </div>
    <div class="conteiner">
        <div class="label">Id</div>
        <div class="label">Name</div>
        <div class="label">Sex</div>
        <div class="label">Age</div>
        <div class="label">Experience</div>
    </div>
    <div class="wrapper2">
        <div class="text">${Producer.id}</div>
        <div class="text">${Producer.fullName}</div>
        <div class="text">${Producer.male}</div>
        <div class="text">${Producer.age}</div>
        <div class="text">${Producer.experience}</div>
    </div>
</form>

<div class="createWr1" >
    <h2> Singers </h2>
</div>
<div class="conteiner">
    <div class="label12">Name</div>
    <div class="label12">Age</div>
</div>
    <c:forEach items="${Producer.singers}" var="singer">
        <tr>
            <td>${singer.singername}</td>
            <td>${singer.singerage}</td>
            <td>
                <button class="btn btn-outline-danger"><a href="/producers/${Producer.id}/removeSinger/${singer.id}">Remove singer</a></button>
            </td>
        </tr>
    </c:forEach>
</table>

<security:authorize access="hasRole('ROLE_ADMIN')">
    <form action = "/producers/${Producer.id}" method = "post" >
        <div class="createWr1" >
            <h2 > Update producer</h2>
        </div >
        <div class="createWr3" >
            <div class="label14">Name</div>
            <div class="label14">Sex</div>
            <div class="label14">Age</div>
            <div class="label14">Experience</div>
        </div >
        <div class="createWr3" >
            <div class="space" ><input type = "text" name = "fullName" required value="${Producer.fullName}" ></div >
            <div class="space">
                <select required name="male">
                    <c:forEach items="${Males}" var="male" >
                        <option value="${male}">${male}</option>
                    </c:forEach>
                </select>
            </div >
            <div class="space" ><input type = "number" name = "age" required min="16" max="120" value="${Producer.age}"></div >
            <div class="space" ><input type = "number" name = "experience" required min="0" max="80"  value="${Producer.experience}"></div >
        </div >

        <div class="forSubmit" >
            <button type = "submit" class="btn" > Submit </button >
        </div >
    </form>
</security:authorize>
</body>
<jsp:include page="../views/footer.jsp" />
</html>

