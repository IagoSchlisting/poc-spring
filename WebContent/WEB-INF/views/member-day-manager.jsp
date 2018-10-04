<%@ include file="templates/header.jsp"%>

<div class="panel panel-default">
    <div class="panel-heading" style="min-height: 45px">
        <div class="col-md-6"> Day Manager &nbsp;&nbsp;&nbsp; <span class="date">${day.day}</span> </div>
    </div>
    <div class="panel-body">
        <%@ include file="templates/messages.jsp"%>
        <form action="/userDay/update" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input type="hidden" name="id" id="id" value="${userDay.id}"/>
            <c:if test="${userDay.day.special}">
                <div class="form-group">
                    <label for="disponibility"> Disponibility </label>
                    <select class="form-control" id="disponibility" name="disponibility">
                        <option value="1">Available</option>
                        <option value="0" ${not userDay.disponibility ? 'selected' : ''}>Not Available</option>
                    </select>
                </div>
            </c:if>
            <div class="form-group">
                <label for="shift"> Shift </label>
                <select class="form-control" id="shift" name="shift">
                    <option value="day" ${userDay.shift == "DAY" ? 'selected' : ''}> Day </option>
                    <option value="late" ${userDay.shift == "LATE" ? 'selected' : ''}> Late </option>
                    <option value="any" ${userDay.shift == "ANY" ? 'selected' : ''}> Any </option>
                </select>
            </div>
        <a href="/calendar/manage/${day.period.id}" class="btn btn-danger"> Back </a>
            <button class="btn btn-primary" type="submit"> Save Changes </button>
        </form>
    </div>
    <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
</div>

<%@ include file="templates/footer.jsp"%>