<%@ include file="templates/header.jsp"%>

    <div class="panel panel-default">
        <div class="panel-heading"> Change your password here. </div>
        <div class="panel-body">
            <%@ include file="templates/messages.jsp"%>
            <form action="/changepass" method="post">
                <div class="form-group">
                    <label for="oldPassword">Current Password</label>
                    <input type="password" class="form-control" id="oldPassword" name="oldPassword">
                </div>
                <div class="form-group">
                    <label for="newPassword">New Password</label>
                    <input type="password" class="form-control" id="newPassword" name="newPassword">
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password</label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword">
                </div>

                <a href="/" class="btn btn-danger"> Back </a>
                <button type="submit" class="btn btn-primary">
                    Change
                </button>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>

        </div>
        <div class="panel-footer"> @POC/SPRING - Iago Machado </div>
    </div>

<%@ include file="templates/footer.jsp"%>