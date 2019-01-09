<?php
	session_start();
	require "connection.php";
	
	$product_id = $_POST['product_id'];
	
	$sql = "SELECT * FROM product WHERE ProductID=:product_id";
	$query = $conn->prepare($sql);	
	$query->bindParam(':product_id', $product_id);
	$query->execute();
	$result = $query->fetch();
	$query->closeCursor();

	$arrCol = array();
	$response = array();

		$tmp = array();
		$tmp["product_id"] = $result["ProductID"];
		$tmp["name"] = $result["Name"];
		$tmp["price"] = $result["Price"];
		$tmp["quantity"] = $result["Quantity"];
		array_push($response, $tmp);
	
		
	echo json_encode($response);
?>