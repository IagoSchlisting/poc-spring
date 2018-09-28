<%@ include file="templates/header.jsp"%>

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
                <c:if test="${not empty user}">
                    <input type="hidden" name="id" id="id" value="${user.id}">
                </c:if>
                <input type="text" class="form-control" id="username" name="username"
                       value="<c:if test="${not empty user}">${user.username}</c:if>">
            </div>

            <c:if test="${not empty teams}">
                <div class="form-group">
                    <label for="team"> Change Team </label>
                    <select id="team" name="team" class="form-control">
                        <c:forEach var="team" items="${teams}">
                            <c:choose>
                                <c:when test="${user.team.name == team.name}">
                                    <option value="${team.id}" selected="selected">${team.name}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${team.id}">${team.name}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </c:if>

<a href="/" class="btn btn-danger"> Back </a>
            <button type="submit" class="btn btn-primary">
                Save
            </button>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>
        </div>
        <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
    </div>

<%@ include file="templates/footer.jsp"%>