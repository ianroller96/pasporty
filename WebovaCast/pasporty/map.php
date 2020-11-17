<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>  
  	
    <title>Mapa - Pasporty</title> 

    <meta charset="utf-8" />
    <link rel="shortcut icon" type="image/x-icon" href="img/favicon.ico" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />

    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />      
        
      <!-- PLUGINS-->    
    <!-- Leaflet 1.6.0 -->
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.6.0/dist/leaflet.css" integrity="sha512-xwE/Az9zrjBIphAcBb3F6JVqxf46+CDLwfLMHloNu6KEQCAWi6HcDUbeOfBIptF7tcCzusKFjFw2yuvEpDL9wQ==" crossorigin=""/>
    <script src="https://unpkg.com/leaflet@1.6.0/dist/leaflet.js" integrity="sha512-gZwIG9x3wUXg2hdXF6+rVkLF/0Vi9U8D2Ntg4Ga5I5BZpVkVxlJWbSQtXPSiUTtC0TjtGOmxa1AJPuV0CPthew==" crossorigin=""></script>    
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">

    <!-- Font Magra -->
    <link href="https://fonts.googleapis.com/css2?family=Magra:wght@700&display=swap" rel="stylesheet">

    <!-- L.Control.Locate -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/leaflet.locatecontrol/dist/L.Control.Locate.min.css" />
    <script src="https://cdn.jsdelivr.net/npm/leaflet.locatecontrol/dist/L.Control.Locate.min.js" charset="utf-8"></script>
    <!-- L.EasyButton -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/leaflet-easybutton@2/src/easy-button.css">
    <script src="https://cdn.jsdelivr.net/npm/leaflet-easybutton@2/src/easy-button.js"></script>
    <!-- Search -->
    <link rel="stylesheet" href="./plugins/leaflet-search.css"/>
    <script src="./plugins/leaflet-search.js"></script>

      <!-- CSS -->
    <link rel="stylesheet" type="text/css" href="./styles/style_map.css">
    <link rel="stylesheet" type="text/css" href="./styles/loader.css">  
    
  </head>
  <body>   

    <!-- LOADER -->
    <div class="loader-wrapper">
    <div class="loader"><div></div><div></div><div></div><div></div></div>
    </div>
    

    <!-- MAPA -->
    <div id='map'></div>    
    
    
    <!-- MAP SCRIPT - NAPOJENI -->
    <script>    
        <?php $src = $_GET["src"];?> //GET GEOJSON
        var src = <?php echo json_encode($src); ?>; //NASTAVIT PROMENNOU I V JS PRO POPUP   
    
        var mapdata = $.ajax({
          url:"./data/<?php echo $src; ?>",
          dataType: "json",
          success: console.log("Map data successfully loaded."),
          error: function (xhr) {
            alert(xhr.statusText)
          }
        })
    </script>

    <!-- NACIST MAP SCRIPT -->
    <script src="./scripts/map_script.js?random=<?php echo uniqid(); ?>"></script>
        
  </body>
</html>