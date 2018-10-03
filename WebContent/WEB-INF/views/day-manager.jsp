<%@ include file="templates/header.jsp"%>

<div class="panel panel-default">
    <div class="panel-heading" style="min-height: 45px">
        <div class="col-md-6"> Day Manager </div>
        <div class="col-md-6" align="right"> <strong>${day.day}</strong></div>

    </div>
    <div class="panel-body">
        <table class="table table-stripped">
            <tr>
                <th> Member Name </th>
                <th> Shift </th>
                <th> Disponibility </th>
            <tr>
                <c:forEach var="userDay" items="${userDays}">
            <tr>
                <td> ${userDay.user.username} </td>
                <td> ${userDay.shift}</td>
                <td> ${userDay.disponibility}</td>
            </tr>
            </c:forEach>
        </table>
        <a href="/calendar/manage/${day.period.id}" class="btn btn-danger"> Back </a>
    </div>
    <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
</div>

<%@ include file="templates/footer.jsp"%>