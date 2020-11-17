<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  
    <title>Editace - Pasporty</title>   
    
    <meta charset="utf-8" />
    <link rel="shortcut icon" type="image/x-icon" href="./img/favicon.ico" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />

    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />

    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

    <!-- Material Icons -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    
      <!-- CSS -->
    <link rel="stylesheet" type="text/css" href="./styles/style_edit.css">
    
  </head>
  <body> 
    <?php
      $src = $_GET["src"];
      $id = $_GET["id"];
    ?>

    <script>
      var src = <?php echo json_encode($src); ?>;
      var id = <?php echo json_encode($id); ?>;
      var cesta = './data/' + src;

      <?php

      $jsonString = file_get_contents('./data/'.$src);
      $data = json_decode($jsonString, true);

      ?>

      var json_js = <?php echo json_encode($data); ?>;
      console.log('PREDTIM');
      console.log(json_js);

      var json_string = JSON.stringify(json_js);


    	/*GET JSON PROPERTIES - KEY NAMES*/
    	var keys = Object.keys(json_js.features[0].properties);
    	//console.log( keys[ 0 ] );
    	console.log('KEYS: ' + keys.join(", "));

      /*DELKA POLE - POCET ATRIBUTU*/
      console.log('POCET ATRIBUTU: ' + keys.length);
      var pocet_atributu = keys.length;

      /*DELKA POLE - POCET PRVKU*/
      console.log('POCET PRVKU JSONU: ' + json_js.features.length);
      var pocet_prvku = json_js.features.length;

      /*FUNKCE NA HLEDANI - VRATI POZICI V POLI PODLE HLEDANEHO ID*/
      function najdiPodleID(jsonIn, prvkyCount, idIn) {
        for (var i = 0; i < prvkyCount; i++) {
          if (jsonIn.features[i].properties.ID_PAS_L == idIn) {
            return i;
          }
        }
      }

      var hledany = najdiPodleID(json_js, pocet_prvku, id);
      console.log('HLEDANY: ' + hledany);

      /*FUNKCE NA TVORBU TEXTBOXU*/
      var s = "";
      function createTextbox(atributyCount, idIn) {
          for (var i = 0; i < atributyCount; i++) {
              s += '<label for="'+ keys[i] +'"><span>'+ keys[i] +'</span></label><input type="text" id ="textbox_'+ i +'" name="'+ keys[i] +'" value="'+ json_js.features[hledany].properties[keys[i]] + '"><br>'; //Vytvori textbox
          }          
      }

      /*EDITACE*/
      function saveEdits() {
        for (var i = 0; i < pocet_atributu; i++) {
          json_js.features[hledany].properties[keys[i]] = document.getElementById('textbox_' + i).value;
        }
        console.log('Vse bylo ulozeno!');
        console.log('POTE');
        console.log(json_js);

        var pouzit = JSON.stringify(json_js);
        document.getElementById('ulozit').value = pouzit;
      }

      function Pouzit() {
        document.getElementById('pouzit').innerHTML = "Změny provedeny!";
      }

      window.onload = function() {
        document.getElementById('ulozit').value = json_string;

        createTextbox(pocet_atributu);
        document.getElementById("textboxy").innerHTML = s;

        $("input[name=ID_PAS_L]").prop("disabled", true);
      }

      /*TLACITKO ZPET*/
      function tlacitkoZpet() {
        window.location = 'map.php?src='+src;
      }


    </script>

    <!-- UKLADANI -->
    <?php
      $message = "";
      if (isset($_POST['SubmitButton'])) { // Check if form was submitted
        $input = $_POST['ulozit']; // Get input text
        file_put_contents('./data/'.$src, $input);
        echo '<script>window.location = \'map.php?src=\'+src;</script>'; //Po ulozeni zpet na mapu
      }
    ?>

    <div class="topnav">
      <button class="btn_zpet" onclick="tlacitkoZpet()"><span class="material-icons">arrow_back</span></button>
      <div class="nadpis">Upravit</div>
      <div class="rozdelovac">
        <svg height="50" width="5">
          <line x1="3" y1="10" x2="3" y2="40" />
        </svg>
      </div>
      <div class="nadpis2"><?php echo $src;?> – ID: <?php echo $id;?></div>
    </div>

    <div class="obsah">
      <div class="form-style-2">
        <div id="textboxy"></div>
        <span id="pouzit"></span>
      </div>
    </div>

    <div class="spodni_lista">
      <div class="btn_container">
        <div class="tlacitka">
          <button class ="btn_pouzit" id="btn_pouzit" onclick="saveEdits(); Pouzit();">Použít</button>
        </div>
        <div class="tlacitka">
          <form name="ukladani" id="ukladani" action="" method="post">
            <input type="hidden" id="ulozit" name="ulozit">
            <input type="submit" id="SubmitButton" name="SubmitButton" value="Uložit a zpět">
          </form>
        </div>
      </div>
    </div>
  </body>
</html>