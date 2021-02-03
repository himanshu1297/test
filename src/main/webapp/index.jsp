<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CSV read</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" media="all" />
        <link href="css/jquery.dataTables.css" rel="stylesheet" type="text/css" media="all" />
    </head>
    <body>
        <form method="post" action="" id="form"
              enctype="multipart/form-data">
            <input type="file" name="file" id="file"
                   class="form-control1" placeholder="Upload File"
                   style="width: 301px; margin-top: 4px; margin-bottom: 4px; font-family: Arial; text-transform: none; margin-bottom: 0px;" />
            <input type="button" id="submitbtn"
                   value="Submit"
                   class="btn btn-primary pull-right"
                   style="margin: 15px 10px 0 20px;">
        </form>
        <div id="res"></div>
    </body>
    <script src="js/jquery.js"></script>
    <script src="js/bs.js"></script>
    <script src="js/jquery-3.3.1.js"></script>
    <script src="js/jquery.min.js"></script>
    <script src="js/jquery.dataTables.min.js"></script>
    <script src="js/loadingoverlay.min.js"></script>
    <script>
        $("#submitbtn")
                .click(
                        function () {
//                            $("res").empty();
//                            $("#response").hide();
                            var x = $("#file").val();
                            if (x == null || x == "") {
                                alert("Please choose a CSV file");
                                return false;
                            }
                            var form = $('#form')[0];
                            var data = new FormData(form);
                            $
                                    .ajax({
                                        type: "POST",
                                        enctype: 'multipart/form-data',
                                        url: "/uploadform",
                                        data: data,
                                        dataType: 'html',
                                        processData: false,
                                        contentType: false,
                                        cache: false,
                                        beforeSend: function () {
                                            $.LoadingOverlay("show", {
                                                zIndex: 999999999
                                            });
                                        },
                                        complete: function () {
                                            $.LoadingOverlay("hide");
                                            $("#file").val('');
                                        },
                                        success: function (response) {
                                            if (response.includes(" at line")) {
                                                alert(response);
                                            } else {
                                                document
                                                        .getElementById("res").innerHTML = response;
                                            }

                                        },
                                        error: function (jqXHR, textStatus,
                                                errorThrown) {
                                            location.reload(true);
                                        }
                                    });
                        });
        $('#Mytable').DataTable({
            "paging": true,
            "ordering": true,
            "info": false,
            "order": [[0, "asc"]]
        });
    </script>
</html>
