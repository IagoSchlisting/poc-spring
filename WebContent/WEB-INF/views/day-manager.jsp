<%@ include file="templates/header.jsp"%>

<div class="panel panel-default">
    <div class="panel-heading" style="min-height: 45px">
        <%--<div align="center"> <span class="date">${day.day}</span></div>--%>
        <div class="col-md-6"> Day Manager &nbsp;&nbsp;&nbsp; <span class="date">${day.day}</span> </div>
    </div>
    <div class="panel-body">

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <c:if test="${not empty msg}">
            <div class="alert alert-info">${msg}</div>
        </c:if>


        <table class="table table-stripped" id="members-day">
            <tr>
                <th> Member Name </th>
                <th> Shift </th>
                <th> Disponibility </th>
            <tr>
                <c:forEach var="userDay" items="${userDays}">
            <tr>
                <td> ${userDay.user.username} </td>
                <td> ${userDay.shift}</td>
                <td>
                    <span class="disponibility ${userDay.disponibility}">
                            ${userDay.disponibility ? 'Available' : 'Not Available'}
                    </span>
                </td>
            </tr>
            </c:forEach>
            <tr>
                <td style="border-right: 1px solid #ddd;">
                    <a href="/calendar/manage/${day.period.id}" class="btn btn-danger back"> Back </a>
                </td>
                <td colspan="2">
                    <form action="/day/update" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <input type="hidden" id="id" name="id" value="${day.id}">
                        <div class="form-group">
                            <div class="form-row"><div class="form-group col-md-4">
                                    <select id="special" name="special" class="form-control">
                                        <option value="0"> Normal business day </option>
                                        <option value="1" ${day.special ? 'selected' : ''}> Weekend or Holiday </option>
                                    </select>
                                </div>
                                <div class="form-group col-md-2" style="margin-left: -20px">
                                    <button class="btn btn-primary" type="submit"> Change </button>
                                </div>
                            </div>
                        </div>
                    </form>
                </td>
            </tr>
        </table>
    </div>
    <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
</div>

<%@ include file="templates/footer.jsp"%>