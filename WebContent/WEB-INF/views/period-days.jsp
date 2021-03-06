<%@ include file="templates/header.jsp"%>

<div class="panel panel-default">
    <div class="panel-heading" style="min-height: 45px">
        <div class="col-md-6"> Period Manager </div>
    </div>
    <div class="panel-body">
        <div style="margin-left: 10%">
            <c:forEach var="day" items="${days}">
                <a href="/day/${day.id}">
                <div class="col-md-2 calendar">
                    <table class="table">
                        <tr><td>${day.day}</td></tr>
                        <tr>
                            <td>
                                <c:choose>
                                    <c:when test="${not day.special}">
                                        <p style="color: green">Normal</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p style="color: red">Special</p>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </table>
                </div>
                </a>
            </c:forEach>
        </div>
    </div>
    <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
</div>

<%@ include file="templates/footer.jsp"%>