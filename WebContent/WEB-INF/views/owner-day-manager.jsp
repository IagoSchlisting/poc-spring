<%@ include file="templates/header.jsp"%>

<div class="panel panel-default">
    <div class="panel-heading" style="min-height: 45px">
        <div class="col-md-6"> Day Manager &nbsp;&nbsp;&nbsp; <span class="date">${day.day}</span> </div>
    </div>
    <div class="panel-body">
        <%@ include file="templates/messages.jsp"%>
        <table class="table table-stripped" id="members-day">
            <tr>
                <th> Member Name </th>
                <th> Shift </th>
                <th> Disponibility </th>
            <tr>
                <c:forEach var="userDay" items="${userDays}">
            <tr>
                <td> ${userDay.user.username} </td>
            <td>
                <span class="status ${userDay.anyShift ? 'ANY' : userDay.shift}">${userDay.anyShift ? 'ANY / ' : ''}${userDay.shift}</span>
            </td>
                <td>
                    <span class="status ${userDay.disponibility}">
                            ${userDay.disponibility ? 'Available' : 'Not Available'}
                    </span>
                </td>
            </tr>
            </c:forEach>
            <tr>
                <td width="250">
                    <a href="/calendar/manage/${day.period.id}" class="btn btn-danger back m-top"> Back </a>
                </td>
                <td colspan="2">
                    <c:if test="${shift != 'NONE'}">
                        <form action="/notification/add" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <input type="hidden" name="msg" id="msg" class="form-control"
                                   value=" Day <span class='variable'> [ ${day.day} ] </span> needs someone in the <span class='variable'> [ ${shift} ] </span> shift, <a href='/day/${day.id}'>click here</a> to help us.">
                            <button class="btn btn-info m-top"> <span class="glyphicon glyphicon-info-sign"></span>&nbsp;  Active Helper</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </table>
    </div>
    <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
</div>

<%@ include file="templates/footer.jsp"%>