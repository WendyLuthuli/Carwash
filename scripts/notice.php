<?php
	session_start();
	require "connection.php";
	
	$product_id = $_POST['product_id'];
	
	$sql = "SELECT Name, Quantity, Notice FROM product WHERE Status = 'Active'";
	$query = $conn -> prepare($sql);
	$query->bindParam(':product_id', $product_id);
	$query->execute();
	$results=$query->fetchAll();

	$query->closeCursor();

	$arrCol = array();
	$response = array();

	
	foreach($results as $result)
	{
		if ($result['Quantity']<=$result['Notice'])
		{
			$code = "notice";
        	$message = "Product(s) to re-order";
        	
        	array_push($response, array("code"=>$code, "message"=>$message));
			// echo htmlentities($result['Name']);
			// echo htmlentities($result['Quantity']);
		}

		// $code = "notice";
  //       $message = "Product(s) to re-order";
  //       array_push($response, array("code"=>$code, "message"=>$message));
	}
	

	echo json_encode($response);
									
									
?>