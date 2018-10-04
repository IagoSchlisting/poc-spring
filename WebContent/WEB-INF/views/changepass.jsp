<%@ include file="templates/header.jsp"%>

    <div class="panel panel-default">
        <div class="panel-heading"> Change your password here. </div>
        <div class="panel-body">
            <%@ include file="templates/messages.jsp"%>
            <form action="/changepass" method="post">
                <div class="form-group">
                    <label for="old_password">Current Password</label>
                    <input type="password" class="form-control" id="old_password" name="old_password">
                </div>
                <div class="form-group">
                    <label for="new_password">New Password</label>
                    <input type="password" class="form-control" id="new_password" name="new_password">
                </div>
                <div class="form-group">
                    <label for="confirm_password">Confirm Password</label>
                    <input type="password" class="form-control" id="confirm_password" name="confirm_password">
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