<%@ include file="templates/header.jsp"%>

<div class="panel panel-default">
    <div class="panel-heading" style="min-height: 45px">
        <div class="col-md-6"> Period Configurations </div>
    </div>
    <div class="panel-body">
        <%@ include file="templates/messages.jsp"%>
        <form action="/calendar/configure" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input type="hidden" id="period_id" name="period_id" value="${days.get(0).period.id}">
            <table class="table" style="width: 70%; margin-left: 14%">
            <tr>
                <th> Id</th>
                <th> Date</th>
                <th> Type</th>
                <th> Num.Day </th>
                <th> Num.Late </th>
            </tr>
        <c:forEach var="day" items="${days}">
            <tr class="${day.special ? 'bg-danger' : 'bg-success'}">
                <td width="50"> ${day.id}</td>
                <td width="150"> ${day.day}</td>
                <td>
                    <select id="special-${day.id}" name="special-${day.id}" class="form-control">
                        <option value="0"> Normal business day </option>
                        <option value="1" ${day.special ? 'selected' : ''}> Weekend or Holiday </option>
                    </select>
                </td>
                <td width="150"> <input type="number" name="late-${day.id}" id="late-${day.id}" value="${day.numberDay}" class="form-control"> </td>
                <td width="150"> <input type="number" name="day-${day.id}" id="day-${day.id}" value="${day.numberLate}" class="form-control"> </td>
            </tr>
        </c:forEach>
            <tr>
                <td colspan="5">
                    <a href="/calendar/admin" class="btn btn-danger back"> Back </a>
                    <button class="btn btn-primary" type="submit"> Save configurations </button>
                </td>
            </tr>
        </table>
        </form>
    </div>
    <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
</div>

<%@ include file="templates/footer.jsp"%>