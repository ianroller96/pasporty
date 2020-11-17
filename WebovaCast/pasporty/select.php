<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  
    <title>Výběr - Pasporty</title>   
    
    <meta charset="utf-8" />
    <link rel="shortcut icon" type="image/x-icon" href="img/favicon.ico" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />

    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    
    <style>
    
        /*CUSTOM SELECT*/
          /* The container must be positioned relative: */
        .custom-select {
          position: relative;
          font-family: Arial;
          margin: auto;
          margin-top: 5%;
        }
        
        .custom-select select {
          display: none; /*hide original SELECT element: */
        }
        
        .select-selected {
          background-color: #006FAD;
        }
        
        /* Style the arrow inside the select element: */
        .select-selected:after {
          position: absolute;
          content: "";
          top: 14px;
          right: 10px;
          width: 0;
          height: 0;
          border: 6px solid transparent;
          border-color: #fff transparent transparent transparent;
        }
        
        /* Point the arrow upwards when the select box is open (active): */
        .select-selected.select-arrow-active:after {
          border-color: transparent transparent #fff transparent;
          top: 7px;
        }
        
        /* style the items (options), including the selected item: */
        .select-items div,.select-selected {
          padding: 8px 16px;
          border: 1px solid transparent;
          border-color: transparent transparent rgba(0, 0, 0, 0.1) transparent;
          cursor: pointer;
        }
        
        .select-items {
            color: #5D5E60;
            font-size: 15px;            
        }
        
        .select-selected {
            color: #FFFFFF;
        }
        
        /* Style items (options): */
        .select-items {
          position: absolute;
          background-color: #ffffff;
          top: 100%;
          left: 0;
          right: 0;
          z-index: 99;
        }
        
        /* Hide the items when the select box is closed: */
        .select-hide {
          display: none;
        }
        
        .select-items div:hover, .same-as-selected {
          background-color: #00568C;
          color: #FFFFFF;
        }
      
      
        /*CUSTOM SUBMIT*/
        .custom-submit {
            text-align: center;
            margin-top: 2em;
        }
      
        input[type=submit] {
            font-family: Arial;
            font-size: 15px;
            border: none; /* Remove borders */
            color: white; /* Add a text color */
            padding: 14px 28px; /* Add some padding */
            cursor: pointer; /* Add a pointer cursor on mouse-over */
            background-color: #006FAD;
        }
        
        input[type=submit]:hover {
            background: #00568C;
        }
        
      /*LOGO KGI*/
      .footer {
        position: fixed;
        display: inline-block;
        left: 0;
        bottom: 1%;
        width: 100%;
        text-align: center;
      }

      .logoKGI {
        width: 4em;
        height: 4em; 
        bottom: 10%;
      }
        
        
      /*BODY - PRECHOD*/
      body {
          background-color: #C4C4C5;
          background-image: linear-gradient(#C4C4C5, #FFFFFF);        
      }
    
    </style>
    
  </head>
  <body>
  
    <form name="form1" action="map.php" method="get">
      <div class="custom-select" style="width:200px;">
          <select name="src">        
          <?php
                if ($handle = opendir('./data')) {
                
                    $ignoredFiles = array('.', '..', '.htaccess');
                
                    while (false !== ($entry = readdir($handle))) {
                        if(in_array(pathinfo($entry, PATHINFO_BASENAME), $ignoredFiles)) continue;
                        echo "<option value=\"" . pathinfo($entry, PATHINFO_BASENAME) . "\">" . pathinfo($entry, PATHINFO_FILENAME) . "</option>";
                    }
                
                    closedir($handle);
                }
              ?>
          </select>
      </div>
      
      <div class="custom-submit">
      <input type="submit" value="Otevřít mapu">
      </div>
    </form>
    
    <div class="footer">
    <img src=".\img\logokgi.png" alt="logo KGI" class="logoKGI">
    </div>
    
    <script>
    
          var x, i, j, selElmnt, a, b, c;
      /* Look for any elements with the class "custom-select": */
      x = document.getElementsByClassName("custom-select");
      for (i = 0; i < x.length; i++) {
        selElmnt = x[i].getElementsByTagName("select")[0];
        /* For each element, create a new DIV that will act as the selected item: */
        a = document.createElement("DIV");
        a.setAttribute("class", "select-selected");
        a.innerHTML = selElmnt.options[selElmnt.selectedIndex].innerHTML;
        x[i].appendChild(a);
        /* For each element, create a new DIV that will contain the option list: */
        b = document.createElement("DIV");
        b.setAttribute("class", "select-items select-hide");
        for (j = 1; j < selElmnt.length; j++) {
          /* For each option in the original select element,
          create a new DIV that will act as an option item: */
          c = document.createElement("DIV");
          c.innerHTML = selElmnt.options[j].innerHTML;
          c.addEventListener("click", function(e) {
              /* When an item is clicked, update the original select box,
              and the selected item: */
              var y, i, k, s, h;
              s = this.parentNode.parentNode.getElementsByTagName("select")[0];
              h = this.parentNode.previousSibling;
              for (i = 0; i < s.length; i++) {
                if (s.options[i].innerHTML == this.innerHTML) {
                  s.selectedIndex = i;
                  h.innerHTML = this.innerHTML;
                  y = this.parentNode.getElementsByClassName("same-as-selected");
                  for (k = 0; k < y.length; k++) {
                    y[k].removeAttribute("class");
                  }
                  this.setAttribute("class", "same-as-selected");
                  break;
                }
              }
              h.click();
          });
          b.appendChild(c);
        }
        x[i].appendChild(b);
        a.addEventListener("click", function(e) {
          /* When the select box is clicked, close any other select boxes,
          and open/close the current select box: */
          e.stopPropagation();
          closeAllSelect(this);
          this.nextSibling.classList.toggle("select-hide");
          this.classList.toggle("select-arrow-active");
        });
      }
      
      function closeAllSelect(elmnt) {
        /* A function that will close all select boxes in the document,
        except the current select box: */
        var x, y, i, arrNo = [];
        x = document.getElementsByClassName("select-items");
        y = document.getElementsByClassName("select-selected");
        for (i = 0; i < y.length; i++) {
          if (elmnt == y[i]) {
            arrNo.push(i)
          } else {
            y[i].classList.remove("select-arrow-active");
          }
        }
        for (i = 0; i < x.length; i++) {
          if (arrNo.indexOf(i)) {
            x[i].classList.add("select-hide");
          }
        }
      }
      
      /* If the user clicks anywhere outside the select box,
      then close all select boxes: */
      document.addEventListener("click", closeAllSelect);
    
    </script>

  </body>
</html>
