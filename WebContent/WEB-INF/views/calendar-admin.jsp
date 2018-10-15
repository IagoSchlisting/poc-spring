<%@ include file="templates/header.jsp"%>

<div class="panel panel-default">
    <div class="panel-heading" style="min-height: 45px">
        <div class="col-md-6"> Calendar Manager </div>
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
                            <li><a href="/calendar/configure/${period.id}">Configure</a></li>
                            <li><a href="/calendar/delete/${period.id}">Delete</a></li>
                        </ul>
                    </div>

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
                    <form action="/period/add" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <div class="modal-body" style="min-height: 300px">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="startDate">Start Date</label>
                                        <input type="date" class="form-control" id="startDate" name="startDate">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="endDate">End Date</label>
                                        <input type="date" class="form-control" id="endDate" name="endDate">
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <div style="padding: 5px; border: 1px solid #ccc; border-radius: 5px">
                                        <div style="background-color: #383c92; padding: 2px 0px 0px 10px; border-radius: 5px; margin-bottom: 10px"><label style="color: white"> Normal Days requires </label></div>
                                        <div class="form-group">
                                            <label for="numberDayNormal">For day shifts</label>
                                            <input type="number" class="form-control" id="numberDayNormal" name="numberDayNormal" required="required">
                                        </div>
                                        <div class="form-group">
                                            <label for="numberLateNormal">For late shifts</label>
                                            <input type="number" class="form-control" id="numberLateNormal" name="numberLateNormal"required="required">
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div style="padding: 5px; border: 1px solid #ccc; border-radius: 5px">
                                        <div style="background-color: #b74a55; padding: 2px 0px 0px 10px; border-radius: 5px; margin-bottom: 10px"><label style="color: white"> Special Days requires </label></div>
                                        <div class="form-group">
                                            <label for="numberDaySpecial">For day shifts</label>
                                            <input type="number" class="form-control" id="numberDaySpecial" name="numberDaySpecial" required="required">
                                        </div>
                                        <div class="form-group">
                                            <label for="numberLateSpecial">For late shifts</label>
                                            <input type="number" class="form-control" id="numberLateSpecial" name="numberLateSpecial" required="required">
                                        </div>
                                    </div>
                                </div>

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Save Period</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>



    </div>
    <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
</div>

<%@ include file="templates/footer.jsp"%>