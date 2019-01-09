<?php
require "connection.php";
$sales_id = $_POST['sales_id'];

	
		$sql = "SELECT *
				FROM sale 
				WHERE SaleID = :sales_id";

		$query = $conn->prepare($sql);	
		$query->bindParam(':sales_id', $sales_id);
		$query->execute();
		$results = $query->fetch();
		$query->closeCursor();
		$response = array();
		if($results)
		{
			$code = "return_success";
	        $message = "Sale exists";
	        array_push($response, array("code"=>$code, "message"=>$message));
		}
		else{
			$code = "return_failed";
	        $message = "No sales were made for this reference";
	        array_push($response, array("code"=>$code, "message"=>$message));
		}

	echo json_encode($response);
?>