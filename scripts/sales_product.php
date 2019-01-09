<?php
	session_start();
	require "connection.php";
	
	$sales_id = $_POST['sales_id'];

	
		$sql = "SELECT sale_detail.*, product.Name, product.Price, sale.SalesDate
				FROM product, sale_detail, sale 
				WHERE sale_detail.SaleID = :sales_id 
				AND sale_detail.ProductID = product.ProductID 
				AND sale_detail.SaleID = sale.SaleID";

		$query = $conn->prepare($sql);	
		$query->bindParam(':sales_id', $sales_id);
		$query->execute();
		$results = $query->fetchAll();
		$query->closeCursor();

		$arrCol = array();
		$response = array();


		if ($results)
		{
			foreach ($results as $result) 
			{
				$tmp = array();
				$tmp["product_id"] = $result['ProductID'];
				$tmp["name"] = $result["Name"];
				$tmp["price"] = $result["Price"];
				$tmp["quantity"] = $result["Quantity"];
				$tmp["sales_id"] = $result["SaleID"];
				$tmp["sales_date"] = $result["SalesDate"];

				
				array_push($response, $tmp);
			}
		}
		else
		{
			$code = "return_failed";
	        $message = "No sales were made for this reference";
	        array_push($response, array("code"=>$code, "message"=>$message));
	            
		}

	echo json_encode($response);
	
?>