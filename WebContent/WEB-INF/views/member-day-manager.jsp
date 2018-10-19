<%@ include file="templates/header.jsp"%>

<c:if test="${shift != 'NONE'}">
    <div class="alert alert-warning" role="alert">
        <span class="glyphicon glyphicon-info-sign"></span>&nbsp;&nbsp;&nbsp; Notification: <strong> This day needs someone in the <span class='variable'> [ ${shift} ] </span> shift, please select it and click save. </strong>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</c:if>

<div class="panel panel-default">
    <div class="panel-heading" style="min-height: 45px">
        <div class="col-md-6"> Day Manager &nbsp;&nbsp;&nbsp; <span class="date">${day.day}</span> ${day.special ? '<span class="wh"> This is a special day! </span>' : '' } </div>
    </div>
    <div class="panel-body">
        <%@ include file="templates/messages.jsp"%>
        <form id="form" action="/userDay/update" method="post">
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
                    <option value="any"> Any </option>
                </select>
            </div>
        <a href="/calendar/manage/${day.period.id}" class="btn btn-danger"> Back </a>
            <button class="btn btn-primary" type="submit"> Save Changes </button>
        </form>
    </div>
    <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
</div>

<%@ include file="templates/footer.jsp"%>

<script>

    $("#disponibility").change(function () {
        var selected = $(this).val();
        if(selected == "0"){
            $("#shift").prop('disabled', 'disabled');
            $("#shift").append('<option value="none" selected="selected">None</option>');
        }else{
            $("#shift").prop('disabled', false);
            $("#shift option[value='none']").remove();

        }
    });

    <c:if test="${not userDay.disponibility}">
        $("#shift").append('<option value="none" selected="selected">None</option>');
        $("#shift").prop('disabled', 'disabled');
    </c:if>

</script>