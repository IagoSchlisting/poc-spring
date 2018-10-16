<%@ include file="templates/header.jsp"%>

<div class="panel panel-default">
    <div class="panel-heading" style="min-height: 45px">
        <div class="col-md-6"> Notification Manager </div>
    </div>
    <div class="panel-body">
        <%@ include file="templates/messages.jsp"%>

        <table class="table table-striped">
            <tr>
                <th width="100"> ID </th>
                <th> Message </th>
                <th width="150"> Actions </th>
            </tr>
            <c:forEach var="notification" items="${notifications}">
                <tr>
                    <td> ${notification.id} </td>
                    <td> ${notification.msg} </td>
                    <td>
                        <a href="/notification/delete/${notification.id}" class="btn btn-danger"> Delete </a>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <form action="/notification/add" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <td width="150">  New Notification  </td>
                    <td> <input type="text" name="msg" id="msg" class="form-control" required="required"> </td>
                    <td> <button class="btn btn-primary"> Send Notification </button> </td>
                </form>
            </tr>
        </table>

    </div>
    <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
</div>

<%@ include file="templates/footer.jsp"%>