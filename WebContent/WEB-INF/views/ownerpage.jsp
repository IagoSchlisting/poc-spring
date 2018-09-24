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
    <title> Owner Page  </title>
</head>
<body>

<c:url value="/logout" var="logoutUrl" />
<!-- csrt for log out-->
<form action="${logoutUrl}" method="post" id="logoutForm">
    <input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}" />
</form>

<nav class="navbar navbar-default">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <a class="navbar-brand" href="/"> Team Owner Home </a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="/" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" style="color: green"> ${pageContext.request.userPrincipal.name} <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="javascript:formSubmit()"> Logout </a></li>
                    </ul>
                </li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>

<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading"> ${team.name} </div>
        <div class="panel-body">
            <table class="table table-stripped">
                <tr>
                    <th> ID </th>
                    <th> Member Name </th>
                    <th> Team Name </th>
                    <th> Roles </th>
                    <th> Actions </th>
                <tr>
                <c:forEach var="member" items="${members}">
                <tr>
                    <td> ${member.id} </td>
                    <td> ${member.username}</td>
                    <td> ${member.team.name}</td>
                    <td> |
                        <c:forEach var="role" items="${member.roles}">
                            <c:out value = " ${role.role} |"/>
                        </c:forEach>
                    </td>
                    <td>
                        <a href="/user/edit/${member.id}" class="btn btn-info"> Edit </a>
                        <a href="/user/delete/${member.id}" class="btn btn-danger"> Delete </a>
                    </td>
                </tr>
                </c:forEach>
                <tr>
                    <td colspan="4"> <a href="/user/add" class="btn btn-primary"> Add new Member </a> </td>
                <tr>
            </table>
        </div>
        <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
    </div>
</div>






</body>

<script>
    function formSubmit() { document.getElementById("logoutForm").submit(); }
</script>

</html>
