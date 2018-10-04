<%@ include file="templates/header.jsp"%>

    <div class="panel panel-default">
        <div class="panel-heading" style="min-height: 45px">
            <div class="col-md-6"> Calendar Manager </div>
            <div class="col-md-6" align="right"> You are from <strong>${team.name}</strong> </div>
        </div>
        <div class="panel-body">
            <%@ include file="templates/messages.jsp"%>
            <table class="table table-stripped">
                <tr>
                    <th> ID </th>
                    <th> Start Date </th>
                    <th> End Date </th>
                    <th> Actions </th>
                <tr>
                    <c:forEach var="period" items="${periods}">
                <tr>
                    <td> ${period.id} </td>
                    <td> ${period.start}</td>
                    <td> ${period.end}</td>
                    <td>
                        <a href="/calendar/manage/${period.id}" class="btn btn-info"> Manage </a>
                    </td>
                </tr>
                </c:forEach>

            </table>

        </div>
        <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
    </div>

<%@ include file="templates/footer.jsp"%>