$(document).ready(function() {

    console.log(window.location.href.split('#'));

    var loadedImages = []
    var $lg = $('#gallery');
    $lg.justifiedGallery({
        rowHeight:180,
        margins: 5,
        lastRow: 'justify',
        rel : 'gallery1'
    });/*.on('jg.complete', function () {
     $lg.lightGallery({
     thumbnail:true,
     hideBarsDelay: 2000,
     animateThumb: true
     });
     });*/

    $(document).on('click', '.custom-image', function(){
        var currentSlide = $(this).attr('data-index');
        var $gal = $lg.lightGallery({
            thumbnail:true,
            hideBarsDelay: 2000,
            animateThumb: true,
            download: false,
            animatedThumb: true,
            showThumbByDefault: true,
            dynamic: true,
            dynamicEl: loadedImages
        });

        $gal.data('lightGallery').index = currentSlide;
        return false;
    });

    var lastOffset = 0;
    var loadPictures = function() {

        $.get('/api/media/files?offset='+lastOffset, function(data) {
            $.each(data.content, function(i, picture) {
                $lg.append('<a href="/api/media/files/'+picture.id+'" class="custom-image" data-index="'+(lastOffset+i)+'">' +
                    '<img alt="'+picture.originalFilename+'" src="/api/media/files/'+picture.id+'" />' +
                    '<span class="select glyphicon glyphicon-ok-circle" />' +
                    '<span class="multi-select glyphicon glyphicon-record" />' +
                    '</a>');
                loadedImages.push({
                    "src": "/api/media/files/" + picture.id,
                    "href": "/api/media/files/" + picture.id,
                    "thumb": "/api/media/files/" + picture.id,
                    "subHtml": "<h4>" + picture.originalFilename + "</h4>"
                });
            });
            lastOffset += data.content.length;
            $lg.justifiedGallery('norewind');
        });
    }
    loadPictures();

    $(window).scroll(function() {
        if($(window).scrollTop() + $(window).height() == $(document).height()) {
            loadPictures();
        }
    });

    $(document).on('click', '.custom-image .glyphicon.select, .custom-image .glyphicon.multi-select', function(event) {
        $(this).closest('a').toggleClass('selected');
        if ($lg.find('a.selected').size() == 0) {
            $lg.removeClass('select-mode');
        } else {
            $lg.addClass('select-mode');
        }
        return false;
    });

});
