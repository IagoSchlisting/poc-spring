<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty msg}">
    <div class="alert alert-info">${msg}</div>
</c:if>