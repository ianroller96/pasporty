<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  
    <title>BP Roller | Upload</title>   
    
    <meta charset="utf-8" />
    <link rel="shortcut icon" type="image/x-icon" href="img/favicon.ico" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />

    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

    <!-- Plugin-CSS -->
    <link rel="stylesheet" href="./upload/css/bootstrap.min.css">
    <link rel="stylesheet" href="./upload/css/themify-icons.css">
    <!-- Main-Stylesheets -->
    <link rel="stylesheet" href="./upload/css/normalize.css">
    <link rel="stylesheet" type="text/css" href="./styles/style_upload.css">
    <link rel="stylesheet" href="./upload/css/responsive.css">
    
    <!--Vendor-JS-->
    <script src="./upload/js/bootstrap.min.js"></script>

    <!--Sweet-Alert-->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@9"></script>

  </head>
  <body>
    <!--Mainmenu-area-->
    <div class="mainmenu-area affix">
        <div class="container">
            <!--Logo-->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#primary-menu">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a href="javascript: history.go(-1)" class="navbar-brand logo">
                    <h2>PASPORTY</h2>
                </a>
            </div>
            <!--Logo/-->
            <nav class="collapse navbar-collapse" id="primary-menu">
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="javascript: history.go(-1)">Zpět</a></li>
                    <li class="active"><a href="#">NAHRÁT DATA</a></li>
                </ul>
            </nav>
        </div>
    </div>
    <!--Mainmenu-area/-->

    <section class="sky-bg" style="padding-top: 150px !important; padding-bottom: 70px !important;">
        <div class="container">
            <div class="row">
                <div class="col-xs-12 col-sm-6 col-sm-offset-3 text-center">
                    <div class="page-title">
                        <h2>NAHRÁT DATA</h2>
                        <p>Zde lze do pasportizační aplikace nahrát data ve formátu GeoJSON. A&nbsp;nebo můžete data <a href="./browse.php">stáhnout či smazat zde</a>.</p>
                    </div>
                </div>
            </div>
            <div class="row" style="text-align: center !important;">
                <form name="ukladani" id="ukladani" action="" method="post">
                    <div class="custom-file-input">
                        <label for="fileInput" class="button" id="label">Vybrat GeoJSON</label>
                        <input type="file" id="fileInput" accept=".geojson,.json" required>
                    </div>
                    <br>
                    <input type="hidden" id="ulozit" name="ulozit">
                    <input type="hidden" id="nazevsouboru" name="nazevsouboru">
                    <button type="submit" id="SubmitButton" name="SubmitButton" class="button orange">Nahrát</button>
                </form>

                <!--PREVENT FORM RESUBMISSION-->
                <script>
                    if ( window.history.replaceState ) {
                        window.history.replaceState( null, null, window.location.href );
                    }
                </script>

                <!--SAMOTNY UPLOAD-->
                <?php
                    if (isset($_POST['SubmitButton'])) { // Check if form was submitted
                        $input = $_POST['ulozit']; // Get input text - JSON
                        $name = $_POST['nazevsouboru']; //Nazevsouboru
                        file_put_contents('./data/'.$name, $input); //ulozit do souboru
                        echo '<script>Swal.fire(\'Hotovo\',\'Soubor byl úspěšně nahrán na server!\',\'success\')</script>'; //alert
                    }
                ?>

                <!--SCRIPT UPLOADU-->
                <script src="./scripts/upload_script.js?random=<?php echo uniqid(); ?>"></script>               

            </div>
        </div>
    </section>

    <!--Zacatek-ke-stazeni-->
    <section class="gray-bg" style="padding-top: 70px !important; padding-bottom: 70px !important;">
        <div class="container">
            <div class="row">
                <div class="col-xs-12 col-sm-6 col-sm-offset-3 text-center">
                    <div class="page-title">
                        <h2>DŮLEŽITÉ ODKAZY A TESTOVACÍ DATA</h2>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-6 col-md-3">
                    <div class="box">
                        <div style="font-size: 50px !important; margin-bottom: 35px !important;">
                            <span class="ti-reload"></span>
                        </div>
                        <h3>Online Geodata Converter</h3>
                        <p>Online převodník geodat, kde je možné převést různé formáty do požadovaného GeoJSONu.</p>
                        <p>Omezení je na <b>3 datasety</b> nebo <b>5 MB dat / měsíc</b>. Je důležité nastavit cílový souřadnicový systém na <b>WGS 84</b> (EPSG: 4326).</p>
                        <br>
                        <a href="https://mygeodata.cloud/converter/" class="button orange">Přejít</a>
                    </div>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-3">
                    <div class="box">
                        <div style="font-size: 50px !important; margin-bottom: 35px !important;">
                            <span class="ti-write"></span>
                        </div>
                        <h3>Geojson.io</h3>
                        <p>Nástroj k editaci geodat v souborech typu GeoJSON.</p>
                        <br>
                        <a href="http://geojson.io/" class="button orange">Přejít</a>
                    </div>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-3">
                    <div class="box">
                        <div style="font-size: 50px !important; margin-bottom: 35px !important;">
                            <span class="ti-save"></span>
                        </div>
                        <h3>Testovací Data 1</h3>
                        <p>Pilotní data 1 ve formátu GeoJSON pochází z projektu Inovačního Voucheru Olomouckého kraje pro obec Příkazy. Jedná se o data parcel.<br>Soubory <i>polohopi</i> a <i>polohopi_2</i> v archivu ZIP (633&nbsp;kB)</p>
                        <br>
                        <a href="./upload/download/polohopi.zip" class="button orange">Stáhnout</a>
                    </div>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-3">
                    <div class="box">
                        <div style="font-size: 50px !important; margin-bottom: 35px !important;">
                            <span class="ti-save"></span>
                        </div>
                        <h3>Testovací Data 2</h3>
                        <p>Pilotní data 2 ve formátu GeoJSON byla vytvořena autorem práce. Jedná se o 3 ukázkové vrstvy možných pasportů hřbitova, komunikací a veřejného osvětlení.<br>3 vrstvy (SHP a GeoJSON) v archivu ZIP (21&nbsp;kB)</p>
                        <br>
                        <a href="./upload/download/PilotniData2.zip" class="button orange">Stáhnout</a>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!--Konec-ke-stazeni-->

    <!--Zacatek-footer-->
    <footer class="footer-area relative sky-bg" id="footer">
        <div class="absolute footer-bg"></div>
        <div class="footer-middle">
            <div class="container">
                <div class="row">
                    <div class="col-lg-3 col-sm-6">
                        <div class="single-footer-widget">
                            <h6 class="text-white text-uppercase mb-20">Autor</h6>
                            <div class="d-flex">
                                <ul style="list-style-type: none !important; padding-left: 0 !important;">
                                    <li>Jan ROLLER</li>
                                    <li><a href="mailto:inflames.honza@gmail.com">inflames.honza@gmail.com</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-3 col-sm-6">
                        <div class="single-footer-widget">
                            <h6 class="text-white text-uppercase mb-20">Vedoucí práce</h6>
                            <div class="d-flex">
                                <ul style="list-style-type: none !important; padding-left: 0 !important;">
                                    <li>Mgr. Rostislav NÉTEK, Ph.D.</li>
                                    <li><a href="mailto:rostislav.netek@upol.cz">rostislav.netek@upol.cz</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-3 col-sm-6">
                        <div class="single-footer-widget">
                            <div class="d-flex">
                                <a href="http://www.geoinformatics.upol.cz/">
                                    <img src="img/kgi-logo.png" alt="logo KGI UPOL">
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-3 col-sm-6">
                        <div class="single-footer-widget">
                            <div class="d-flex">
                                <a href="https://www.upol.cz/">
                                    <img src="img/up-logo.png" alt="logo UPOL">
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="footer-bottom">
            <div class="container">
                <div class="row">
                    <div class="col-xs-12 col-sm-6 pull-left">
                        <p>&copy; Jan ROLLER, Olomouc 2019/20 | This template is made with <i class="ti-heart" aria-hidden="true"></i> by <a href="https://colorlib.com">Colorlib</a></p>
                    </div>
                </div>
            </div>
        </div>
    </footer>
    <!--Konec-footer-->

  </body>
</html>
  