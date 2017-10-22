<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Prediction</title>

<link rel="icon" href="images/BoxOffice.jpg">
<link rel="stylesheet" href="Styles/styles1.css" type="text/css">
<link rel="stylesheet" href="Styles/styles2.css" type="text/css">
<link rel="stylesheet" href="Styles/styles3.css" type="text/css">
<link rel="stylesheet" href="Styles/styles4.css" type="text/css">
<link rel="stylesheet" href="Styles/styles5.css" type="text/css">
<link rel="stylesheet" href="Styles/styles6.css" type="text/css">




 <%
     String movie = request.getAttribute("movie").toString();
     double prp =Math.abs(Double.parseDouble(request.getAttribute("PolarityRatioPart").toString()));
     double hp =Math.abs(Double.parseDouble(request.getAttribute("HypePart").toString()));
     double ap =Math.abs(Double.parseDouble(request.getAttribute("ActorPart").toString()));
     double asp =Math.abs(Double.parseDouble(request.getAttribute("ActressPart").toString()));
     double hop =Math.abs(Double.parseDouble(request.getAttribute("HolidayPart").toString()));
     double gp =Math.abs(Double.parseDouble(request.getAttribute("GenrePart").toString()));
     double sp =Math.abs(Double.parseDouble(request.getAttribute("SequelPart").toString()));     
  %>

<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load("current", {packages:["corechart"]});
      google.charts.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([
                                                          ['Factor', 'Contribution(in percent)'],
                                                          ['Polarity Ratio', <%=prp%>], ['Hype', <%=hp%>], ['Actor', <%=ap%>],
                                                          ['Actress', <%=asp%>], ['Holiday Effect', <%=hop%>], ['Genre', <%=gp%>],
                                                          ['Sequel', <%=sp%>]
                                                          ]);

        var options = {
          title: 'Factors contributing in Predicting Box Office Collection:',
          legend: 'none',
          pieSliceText: 'label',
          slices: {  4: {offset: 0.2},
                    12: {offset: 0.3},
                    14: {offset: 0.4},
                    15: {offset: 0.5},
          },
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart'));
        chart.draw(data, options);
       }
      
     
    </script>


</head>


<body style="background-image:url('images/tweed.png');">

<div id="container">
  <header>
     <h1>Box Office Prediction for <br> <%=request.getAttribute("movie") %></h1>
     <h2> The Movie is likely to collect INR <b><%=request.getAttribute("Revenue") %> </b> Crore</h2>
     
 </header> 
 
 
    
		<div id="piechart" style="width: 500px; height: 400px;"></div>
        <iframe id="video" width="500" height="400" src="">
		</iframe>
			
		<script>
			 var seed="<%=request.getAttribute("url").toString()%>";
			 document.getElementById('video').src="http://www.youtube.com/embed/"+seed+"?autoplay=1";
		</script>
		
</div>
</body>
</html>