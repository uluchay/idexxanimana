console.log("main.js loaded");
var data2display = [];



$(document).ready(function() {
    $('.results-fields').hide();
    $('.search-button').click(function(){
        var searchTerm = $('.form-control').val();
        clearTable();
        $('.results-fields').show();
        if(searchTerm != ''){
            $.ajax({
            url: 'getItunesAlbums?searchTerm='+searchTerm,
            type: "GET",
            dataType: "json",
            success: function (data) {
                for(i = 0; i < data.length; i++){
                    addArtwork2List(data[i], data2display);
                    //insertTableRow([artwork.title, artwork.author, artwork.type], 0);
                }
                checkDisplay();
            }
            });
            $.ajax({
            url: 'getGoogleBooks?searchTerm='+searchTerm,
            type: "GET",
            dataType: "json",
            success: function (data) {
                for(i = 0; i < data.length; i++){
                    addArtwork2List(data[i], data2display);

                }
                checkDisplay();
            }
            });
        }
    });


});



function checkDisplay(){

        data2display.sort(function(a, b){
            var titleA = a.title.toLowerCase();
                titleB = b.title.toLowerCase();
            return (titleA < titleB) ? 1 : (titleA > titleB) ? -1 : 0;
        });

       if(data2display.length > 5){
            data2display.sort();
            for(i = 0; i < data2display.length; i++){
                artwork = data2display[i];
                insertTableRow([artwork.title, artwork.author, artwork.type], 0);
            }
           data2display = [];
        }
}

function insertTableRow(rowData, index) {
  var newRow = $('<tr/>').insertAfter( $('.table-body').find('tr').eq(index) );
  $(rowData).each(function(colIndex) {
      newRow.append($('<td/>').text(this));
  });

  return newRow;
}

function addArtwork2List(data, data2display){
        var artwork = {}
        artwork.author = data.author;
        artwork.title  = data.title;
        artwork.type   = data.type;
        data2display.push(artwork);
        console.log('added '+artwork.type+' with title '+artwork.title+' of '+ artwork.author);
}

function clearTable(){
    $('.table-body').html('');
    $('.table-body').html('<tr><td></td><td></td><td></td></tr>');
}

