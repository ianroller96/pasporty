/*PODKLADOVE MAPY*/
var mapy_zakladni = L.tileLayer('https://mapserver.mapy.cz/base-m/{z}-{x}-{y}', {
    minZoom : 2,
    maxZoom : 18,
    attribution: '&copy; Seznam.cz a.s., &copy; OpenStreetMap' 
});
    
var mapy_letecka = L.tileLayer('https://mapserver.mapy.cz/bing/{z}-{x}-{y}', {
    minZoom : 2,
    maxZoom : 18,
    attribution: '&copy; Seznam.cz a.s., &copy;  OpenStreetMap'
});
    
var openstreetmap = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    maxNativeZoom:19,
    maxZoom:22
});
    
var cuzk_ortofoto = L.tileLayer.wms('https://geoportal.cuzk.cz/WMS_ORTOFOTO_PUB/service.svc/get', {
    layers: 'GR_ORTFOTORGB',
    format: 'image/jpeg',
    transparent: false,
    crs: L.CRS.EPSG4326,
    minZoom: 7,
    maxZoom: 22,
    attribution: '&copy; ČÚZK'
});
    
var cuzk_zakladni = L.tileLayer('//geoportal.cuzk.cz/WMTS_ZM_900913/WMTService.aspx?service=WMTS&request=GetTile&version=1.0.0&layer=zm&style=default&format=image/jpeg&TileMatrixSet=googlemapscompatibleext2:epsg:3857&TileMatrix={z}&TileRow={y}&TileCol={x}', {
    attribution: '&copy; ČÚZK',
    maxZoom: 20
});
    
/*OVERLAY*/            
var katastralniMapaOverlayLayers = 'parcelni_cisla,obrazy_parcel,RST_KMD,hranice_parcel,DEF_BUDOVY,RST_KN,dalsi_p_mapy,prehledka_kat_prac,prehledka_kat_uz,prehledka_kraju-linie';
var katastralniMapaOverlay = L.tileLayer.wms('https://services.cuzk.cz/wms/wms.asp', {
    layers: katastralniMapaOverlayLayers,
    format: 'image/png',
    transparent: true,
    crs: L.CRS.EPSG3857,
    minZoom: 7,
    maxZoom: 22,
    attribution: '&copy; ČÚZK'
});
        

/*-----------------------------------------------------------------------------------*/                


var styl_PL = {
    "color": "#eb6d25",
    "weight": 2,
    "fillOpacity": 0.4
};

var styl_PL_klik = {
    "color": "#006bab",
    "weight": 4,
    "fillOpacity": 0.5
};

/*-----------------------------------------------------------------------------------*/                


/*ZACATEK PODMINKY*/
$.when(mapdata).done(function() {
	var map = L.map('map', {
        layers: openstreetmap
    }).fitWorld();
    
    $(".loader-wrapper").fadeOut("slow");
    
    
    /*ZOOM A OBARVIT ON CLICK*/
    function select(evt) {
        data.resetStyle();
        var prvek = evt.target;

        if (!(prvek instanceof L.Marker)) {
            map.fitBounds(prvek.getBounds());
            prvek.setStyle(styl_PL_klik);

            if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
            prvek.bringToFront();
            }
        } else {
            map.setView(prvek.getLatLng(), 22);
        }
    }

    /*NA SEARCH - GET JSON PROPERTIES - KEY NAMES*/
    var keys = Object.keys(mapdata.responseJSON.features[0].properties);
    var pocet_atributu = keys.length;

    
    /*NACIST KAZDEMU PRVKU V JSONU POPUP S DATAMA*/
    function onEachFeature(feature, layer) {
            var popupContent = [];
            // render properties
            for (var key in feature.properties) {
                popupContent.push("<td><b>" + key + "</b></td><td>" + feature.properties[key] + "</td>");
            }

            layer.bindPopup("<table class=\"zui-table zui-table-rounded zui-table-zebra\"><thead><tr><th>Atribut</th><th>Hodnota</th></tr></thead><tbody><tr>"+ popupContent.join("</tr>") + "</tbody></table> <br> <div class=\"edit_div\"><a href=\"edit.php?src="+src+"&id="+feature.properties.ID_PAS_L+"\" class=\"edit\">Editovat</a></div>");
            /*^ NEJDULEZITEJSI CAST - POPUP S TLACITKEM EDITOVAT POSILA NA edit.php ATRIBUT 'ID_PAS_L'*/
            
            /*ZOOM A OBARVIT ON CLICK 2*/
            layer.on({
    			click: select
            });

            /*NA SEARCH - KAZYDMU PRVKU VYTVORIT POLE PRO HLEDANI*/
            const p = feature.properties;
            p.l_search = "| ";
            for (i = 0; i < pocet_atributu; i++) {
                p.l_search += p[keys[i]] + " | ";
            }
        } 
                                         
    
    /*NACIST JSON*/                
    var data = new L.geoJson(mapdata.responseJSON, {
        style: styl_PL,
        onEachFeature: onEachFeature 
        }).addTo(map);

    /*AUTO ZOOM NA NAHRANY JSON*/
    map.fitBounds(data.getBounds());


/*-----------------------------------------------------------------------------------*/                

    /*CONTROL PODKLADOVE MAPY*/
    var baseMaps = {
    	"OpenStreetMap" : openstreetmap,
        "Mapy.cz" : mapy_zakladni,
        "Letecká" : mapy_letecka,
        "Letecká ČÚZK" : cuzk_ortofoto,
        "Základní ČÚZK" : cuzk_zakladni
    };
    
    var overlayMaps = {
        "Hlavní vrstva" : data,
        "Katastrální mapa" : katastralniMapaOverlay
    };            

    L.control.layers(baseMaps, overlayMaps).addTo(map);
    
    //AUTOMATICKY ZAVRT CONTROL PO PREPNUTI
    map.addEventListener('baselayerchange', function(){
        map.fire('click');
    });


/*-----------------------------------------------------------------------------------*/                
    
    // TITULEK PODKLADY
    $('<div class="leg_nadpis">PODKLADOVÉ MAPY</div>').insertBefore('div.leaflet-control-layers-base');
    // TITULEK OVERLAYS
    $('<div class="leg_nadpis">PŘEKRYVNÉ VRSTVY</div>').insertBefore('div.leaflet-control-layers-overlays');

    /*LEGENDA*/
    var legenda = '<div class="leaflet-control-layers-separator"></div><div class="leaflet-control-layers-separator"></div> <div class="leg_nadpis">LEGENDA</div> <div class="leg_prvek" id="polygon"><svg width="16" height="16"><rect width="16" height="16" class="leg_poly"/></svg><span class="leg_popis">Polygon</span></div> <div class="leg_prvek" id="line"><svg width="16" height="16"><line x1="0" y1="7" x2="16" y2="7" class="leg_line"/></svg><span class="leg_popis">Linie</span></div> <div class="leg_prvek" id="point"><img src="./img/marker-icon.png" height="16" class="leg_point"></img><span class="leg_popis_bod">Bod</span></div>';

    $(legenda).insertAfter('div.leaflet-control-layers-overlays');

    /*ZOBRAZOVAT DYNAMICKY*/
    $('#polygon').hide();
    $('#line').hide();
    $('#point').hide();

    data.eachLayer(function(layer) {
        if (layer instanceof L.Polygon) {
            $('#polygon').show();
        }
        if (layer instanceof L.Polyline) {
            $('#line').show();
        }
        if (layer instanceof L.Marker) {
            $('#point').show();
        }
    });

/*-----------------------------------------------------------------------------------*/                

    /*PRI KLIKU MIMO - RESETSTYLE*/
    map.on('click', function(){data.resetStyle();});                  

    /*SCHOVAT COPYRIGHTY*/
    $('.leaflet-control-attribution').hide();


    /*SCHOVAT ZOOM*/
    map.removeControl(map.zoomControl);


    /*MERITKO*/
    L.control.scale({
        position: 'bottomright',
        imperial: false,
        metric: true                
    }).addTo(map);


	/*L.CONTROL.LOCATE*/
    L.control.locate({
        position: 'bottomright',
        watch: true,
        showCompass: true,
        icon: 'fa fa-compass',
        showPopup: false,
        strings: {
            title: "Moje poloha"
        }
    }).addTo(map);


    /*HOME*/            
    L.easyButton( 'fa-map', function(){
      map.fitBounds(data.getBounds());
    }, 'Domů', {position: 'bottomright'}).addTo(map); 


    /*SEARCH*/
    var searchControl = new L.Control.Search({
        layer: data,
        propertyName: 'l_search',
        initial: false,
        collapsed: false,
        textErr: 'Nebyly nalezeny žádné výsledky',
        textCancel: 'Zrušit',
        textPlaceholder: 'Zadejte, co hledáte...',
        marker: false,
        autoResize: false
    });

    searchControl.on('search:locationfound', function(e) {
        data.resetStyle();
        map.closePopup();
        var prvek =  e.layer;
        if (!(prvek instanceof L.Marker)) {
            map.fitBounds(prvek.getBounds());
            prvek.setStyle(styl_PL_klik);

            if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
            prvek.bringToFront();
            }
        } else {
            map.setView(prvek.getLatLng(), 22);
        }
        prvek.openPopup();
    }).on('search:collapsed', function(e) {
        data.resetStyle();
    });

    map.addControl(searchControl);


});
/*KONEC PODMINKY*/