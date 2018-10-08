<%@ include file="templates/header.jsp"%>

<div class="panel panel-default">
    <div class="panel-heading" style="min-height: 45px">
        <div class="col-md-6"> Calendar Manager </div>
    </div>
    <div class="panel-body">
        <%@ include file="templates/messages.jsp"%>
        <table class="table table-stripped">
            <tr>
                <th> ID </th>
                <th> Start Date </th>
                <th> End Date </th>
                <th> Period's Team </th>
                <th> Actions </th>
            <tr>
                <c:forEach var="period" items="${periods}">
            <tr>
                <td> ${period.id} </td>
                <td> ${period.start}</td>
                <td> ${period.end}</td>
                <td> ${period.team.name}</td>
                <td>
                    <a href="/calendar/manage/${period.id}" class="btn btn-info"> Manage </a>
                    <a href="/calendar/delete/${period.id}" class="btn btn-danger"> Delete </a>
                </td>
            </tr>
            </c:forEach>
            <tr>
                <td colspan="4"> <button class="btn btn-primary" data-toggle="modal" data-target="#addperiod"> Add new Period </button> </td>
            <tr>
        </table>


        <!-- Modal -->
        <div class="modal fade" id="addperiod" tabindex="-1" role="dialog" aria-labelledby="addperiodTitle" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="addperiodTitle"> Create a new period in the calendar. </h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">

                        <form action="/period/add" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <div class="form-group">
                                <label for="start-date">Start Date</label>
                                <input type="date" class="form-control" id="start-date" name="start-date">
                            </div>
                            <div class="form-group">
                                <label for="end-date">End Date</label>
                                <input type="date" class="form-control" id="end-date" name="end-date">
                            </div>
                            
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Save Period</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>



    </div>
    <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
</div>

<%@ include file="templates/footer.jsp"%>