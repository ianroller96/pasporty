
var nacteny = "";
var objekt;

var zmeneno;

var fileName = "";

/*ZMENIT LABEL TLACITKA NA NAZEV SOUBORU*/
$('.custom-file-input input[type="file"]').change(function(e){
    $("#label").html(e.target.files[0].name);
    fileName = e.target.files[0].name;
    console.log('Soubor: '+ fileName);
});


/*FUNKCE, KTERA VLOZI UPRAVENEJ JSON DO HIDDENU*/
function JSONdoHiddenu() {
    var pouzit = JSON.stringify(objekt);
    document.getElementById('ulozit').value = pouzit; //JSON do hiddenu1

    document.getElementById('nazevsouboru').value = fileName; //nazev souboru do hiddenu2
}

/*FUNKCE NA NA PRIDANI UNIKATNIHO ID PO OTEVRENI GEOJSONU*/
document.getElementById('fileInput').addEventListener('change', function selectedFileChanged() {

    const reader = new FileReader();
    reader.onload = function fileReadCompleted() {
    // when the reader is done, the content is in reader.result.
    nacteny = reader.result;
    //console.log('NACTENY: ' + nacteny);

    try {
        objekt = JSON.parse(nacteny); // this is how you parse a string into JSON 
        //console.log('OBJEKT: ' + objekt);
    } catch (ex) {
        console.error(ex);
    }

    /*GET JSON PROPERTIES - KEY NAMES*/
    var keys = Object.keys(objekt.features[0].properties);
    //console.log('KEYS: ' + keys.join(", "));

    /*DELKA POLE - POCET PRVKU*/
    //console.log('POCET PRVKU JSONU: ' + objekt.features.length);
    var pocet_prvku = objekt.features.length;


    /*PRIDAT UNIKATNI ID - ATRIBUT 'ID_PAS_L'*/
    for (var i = 0; i < pocet_prvku; i++) {
        var cislo = (i + 1).toString(); //aby to necislovalo od nuly
        objekt.features[i].properties["ID_PAS_L"] = cislo;
    }

    //console.log('PO PRIDANI: ' + JSON.stringify(objekt));
    //Ted je pridane nove pole s indexem a muze se GeoJSON nahrat jako soubor na server

    JSONdoHiddenu(); //JSON a nazev souboru do hiddenu
  };
  reader.readAsText(this.files[0]);
});