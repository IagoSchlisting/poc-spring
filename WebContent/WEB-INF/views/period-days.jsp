<%@ include file="templates/header.jsp"%>

<div class="panel panel-default">
    <div class="panel-heading" style="min-height: 45px">
        <div class="col-md-6"> Period Manager </div>
    </div>
    <div class="panel-body">
        <div style="margin-left: 10%">
            <c:forEach var="day" items="${days}">
                <a href="/day/${day.id}">
                <div class="col-md-2 calendar ${day.special ? 'special' : ''}">
                    <table class="table">
                        <tr><td align="center">${day.day}</td></tr>
                        <tr>
                            <td align="center">
                                <span class="box DAY">
                                        <c:set var="count" value="0" scope="page" />
                                    <c:forEach var="userday" items="${day.memberdays}">
                                        <c:if test="${userday.shift == 'DAY'}">
                                            <c:set var="count" value="${count + 1}" scope="page"/>
                                        </c:if>
                                    </c:forEach>${count} / ${day.numberDay}
                                </span>
                                <span class="box LATE" style="margin-left: 2px">
                                    <c:set var="count" value="0" scope="page" />
                                        <c:forEach var="userday" items="${day.memberdays}">
                                            <c:if test="${userday.shift == 'LATE'}">
                                                <c:set var="count" value="${count + 1}" scope="page"/>
                                            </c:if>
                                        </c:forEach>${count} / ${day.numberLate}
                                </span>
                            </td>
                        </tr>
                    </table>
                </div>
                </a>
            </c:forEach>
            <div class="col-md-12" style="margin-top:15px">
                <a href="${member ? '/' : '/calendar/admin'}" class="btn btn-danger back"> Back </a>
            </div>
        </div>
    </div>
    <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
</div>

<%@ include file="templates/footer.jsp"%>