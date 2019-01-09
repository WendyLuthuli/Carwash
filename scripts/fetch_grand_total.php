<?php
	session_start();
	require "connection.php";

	$response = array();
	$response["Total"] = array();
	
	$sql = "SELECT SUM(Total) AS Total
			FROM sale
			WHERE DATE(SalesDate) = CURDATE()";

	$query = $conn->prepare($sql);	
	$query->execute();
	$result = $query->fetch();
	$query->closeCursor();

	$arrCol = array();
	$response = array();

	$tmp = array();
	$tmp['sales'] = $result["Total"];

	array_push($response, $tmp);
	echo json_encode($response);
?>