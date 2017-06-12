/*
 * jQuery File Upload Plugin JS Example 8.9.1
 * https://github.com/blueimp/jQuery-File-Upload
 *
 * Copyright 2010, Sebastian Tschan
 * https://blueimp.net
 *
 * Licensed under the MIT license:
 * http://www.opensource.org/licenses/MIT
 */

/* global $, window */

$(function () {
    'use strict';

    // Initialize the jQuery File Upload widget:
    $('#fileupload').fileupload({
        // Uncomment the following to send cross-domain cookies:
        //xhrFields: {withCredentials: true},
        url: '/api/media/files/upload',
        done: function(e, data) {
            //hide completed upload element in queue
            $(data.context['0']).fadeOut(700);

            //here isoutput of uploaded objects
            console.log(data.result);
        },
        dataType: 'json',
        // Enable image resizing, except for Android and Opera,
        // which actually support image resizing, but fail to
        // send Blob objects via XHR requests:
        disableImageResize: /Android(?!.*Chrome)|Opera/
            .test(window.navigator && navigator.userAgent),
        imageMaxWidth: 2000,
        imageMaxHeight: 2000,
        imageCrop: false // Force cropped images
    });
    $('#fileupload').bind('fileuploaddestroy', function (e, data) {
        $.ajax({
            url: data.url,
            type: 'DELETE',
            success: function() {
                $(data.context).fadeOut(function(){$(this).remove()})
            },
            contentType: "json"
        });
        console.log(data, e)
    });
});
