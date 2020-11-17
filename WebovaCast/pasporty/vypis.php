<?php

echo ";";

if ($handle = opendir('./data')) {
                
                    $ignoredFiles = array('.', '..', '.htaccess');
                
                    while (false !== ($entry = readdir($handle))) {
                        if(in_array(pathinfo($entry, PATHINFO_BASENAME), $ignoredFiles)) continue;
                        echo pathinfo($entry, PATHINFO_BASENAME) . ";";
                    }
                
                    closedir($handle);
                }

?>