$(document).ready(function() {

    var usersTable = $('table.users-table').DataTable({
        "info":     true,
        "paging": false,
        "ordering": true,
        "compact": true
    });

    var users = [];
    $.get('/admin/api/users', function(response) {
        users = response;
        usersTable.clear();
        $.each(users, function(i, user) {
            usersTable.row.add( [
                user.name,
                user.lastname,
                user.email,
                JSON.stringify(user.roles),
                '<a class="remove-user" href="#" data-id="' + user.id + '"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>'])
                .draw(false);
        })
        setTimeout(function () {
        }, 100)
    });


    $(document).on('click', '.remove-user', function () {
        var userId = $(this).attr('data-id');
        var $row = $(this).closest('tr');
        $.ajax({
            type: "DELETE",
            url: "/admin/api/users/" + userId,
            contentType: 'application/json',
            success: function(data){
                $($row).remove();
            },
            dataType: "json"
        });
        return false;
    });
    $(document).on('click', '#create-user-form button.add-user', function () {
        var requestBody = {};
        $.each($('#create-user-form').find('input, select'), function (i, input) {
            var name = $(input).attr("name");
            var val = $(input).val();
            requestBody[name] = val;
        })

        $.ajax({
            type: "POST",
            url: "/admin/api/users",
            contentType: 'application/json',
            data: JSON.stringify(requestBody),
            success: function(data){
            },
            dataType: "json"
        });
    });
});
