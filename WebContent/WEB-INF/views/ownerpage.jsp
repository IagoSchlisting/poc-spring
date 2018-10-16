<%@ include file="templates/header.jsp"%>

    <div class="panel panel-default">
        <div class="panel-heading"> ${principal.team.name} </div>
        <div class="panel-body">
            <table class="table table-striped">
                <tr>
                    <th> ID </th>
                    <th> Member Name </th>
                    <th> Team Name </th>
                    <th> Roles </th>
                    <th> Actions </th>
                <tr>
                <c:forEach var="member" items="${members}">
                <tr>
                    <td> ${member.id} </td>
                    <td> ${member.username}</td>
                    <td> ${member.team.name}</td>
                    <td> |
                        <c:forEach var="role" items="${member.roles}">
                            <c:out value = " ${role.role} |"/>
                        </c:forEach>
                    </td>
                    <td>
                        <a href="/user/edit/${member.id}" class="btn btn-info"> Edit </a>
                        <a href="/user/delete/${member.id}" class="btn btn-danger"> Delete </a>
                    </td>
                </tr>
                </c:forEach>
                <tr>
                    <td colspan="4"> <a href="/user/add" class="btn btn-primary"> Add new Member </a> </td>
                <tr>
            </table>
        </div>
        <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
    </div>

<%@ include file="templates/footer.jsp"%>