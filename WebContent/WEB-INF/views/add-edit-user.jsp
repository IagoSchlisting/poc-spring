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
    <title> Owner User Manipulation  </title>
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
        <div class="panel-heading">
            <c:if test="${not empty user}">
                Edit user ${user.id}
            </c:if>
            <c:if test="${empty user}">
                Add new user
            </c:if>
        </div>
        <div class="panel-body">

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <c:if test="${not empty msg}">
                <div class="alert alert-info">${msg}</div>
            </c:if>


            <c:if test="${empty user}">  <form action="/user/add" method="post">  </c:if>
                <c:if test="${not empty user}">  <form action="/user/edit" method="post">  </c:if>
            <div class="form-group">
                <label for="username">Username</label>
                <c:if test="${not empty user}"> <input type="hidden" name="id" id="id" value="${user.id}">  </c:if>
                <input type="text" class="form-control" id="username" name="username"
                       value="<c:if test="${not empty user}">  ${user.username}  </c:if>">
            </div>
                <a href="/" class="btn btn-danger"> Back </a>
            <button type="submit" class="btn btn-primary">
                Save
            </button>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>
        </div>
        <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
    </div>
</div>


</body>

<script>
    function formSubmit() { document.getElementById("logoutForm").submit(); }
</script>

</html>
