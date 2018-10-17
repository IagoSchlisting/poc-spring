        </div>
    </body>
        <script>
            function formSubmit() {
                document.getElementById("logoutForm").submit();
            }

            $(".close").click(function(){
                var json = {id : $(this).attr("data-id")};
                $.ajax({
                    headers:{'X-CSRF-TOKEN': $('meta[name="${_csrf.parameterName}"]').attr('content')},
                    type: "POST",
                    url: "/visualize/notification",
                    data: JSON.stringify(json),
                    dataType: 'json',
                    contentType: 'application/json'
                });
            });
        </script>
</html>

