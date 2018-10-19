<%@ include file="templates/header.jsp"%>

    <div class="panel panel-default">
        <div class="panel-heading" style="min-height: 45px">
            <div class="col-md-6"> Calendar Manager </div>
            <div class="col-md-6" align="right"> You are from <strong>${principal.team.name}</strong> </div>
        </div>
        <div class="panel-body">
            <%@ include file="templates/messages.jsp"%>
            <table class="table table-stripped">
                <tr>
                    <th width="60"> ID </th>
                    <th width="200"> Start Date </th>
                    <th width="200"> End Date </th>
                    <th width="230"> Normal Days </th>
                    <th width="230"> Weekend or Holidays </th>
                    <th> Actions  </th>
                <tr>
                    <c:forEach var="period" items="${periods}">

                <tr>
                    <td> ${period.id} </td>
                    <td> ${period.start}</td>
                    <td> ${period.end}</td>
                    <td class="success">
                        <span class="status DAY"> ${period.numberDayNormal}</span>
                        <span class="status LATE"> ${period.numberLateNormal} </span>
                    </td>
                    <td class="danger">
                        <span class="status DAY"> ${period.numberDaySpecial} </span>
                        <span class="status LATE" style="margin-left: 2px"> ${period.numberLateSpecial} </span>
                    </td>
                    <td>
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Actions
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu">
                                <li><a href="/calendar/manage/${period.id}">Supervise</a></li>
                                <%--<li><a href="/calendar/configure/${period.id}">Configure</a></li>--%>
                            </ul>
                        </div>

                    </td>
                </tr>
                </c:forEach>

            </table>

        </div>
        <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
    </div>

<%@ include file="templates/footer.jsp"%>