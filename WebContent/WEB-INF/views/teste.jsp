<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- \\Bootstrap Library -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<!-- Bootstrap Library// -->

<html>
<head>
    <title> TESTE  </title>
</head>
<body>
<h1> You are in! </h1>

<c:url value="/logout" var="logoutUrl" />

<!-- csrt for log out-->
<form action="${logoutUrl}" method="post" id="logoutForm">
    <input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}" />
</form>


<div class="container">
    <table class="table table-striped">
        <tr>
            <td> ID </td>
            <td> Username </td>
            <td> Password </td>
            <td> Roles </td>
        </tr>
        <c:forEach var="user" items="${users}">
            <tr>
                <td> ${user.id} </td>
                <td> ${user.username}</td>
                <td> ${user.password} </td>
                <td style="background-color: red">
                    <c:forEach var="role" items="${user.roles}">
                        <p style="color: white"><c:out value = "${role.role} -- "/></p>
                    </c:forEach>
                </td>
            </tr>
        </c:forEach>

    </table>
</div>

<div class="container">
    <table class="table table-striped">
        <tr>
            <td> ID </td>
            <td> Role </td>
        </tr>
        <c:forEach var="role" items="${roles}">
            <tr>
                <td> ${role.id} </td>
                <td> ${role.role}</td>
            </tr>
        </c:forEach>

    </table>
</div>


</body>

<script>
    function formSubmit() {
        document.getElementById("logoutForm").submit();
    }
</script>

<c:if test="${pageContext.request.userPrincipal.name != null}">
    <h2>
        Welcome : ${pageContext.request.userPrincipal.name} | <a
            href="javascript:formSubmit()"> Logout</a>
    </h2>
</c:if>

</html>
