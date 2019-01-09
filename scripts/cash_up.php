<?php
	session_start();
	require "connection.php";

	date_default_timezone_set('Africa/Johannesburg');
	
	$cash_up_date = date('Y-m-d');
	$cash_up_time = date("H:i:s"); 
	$total_sales = $_POST["total_sales"];
	$cash = $_POST["cash"];
	$difference = $_POST["difference"];
	$user_id = $_POST["user_id"];
	
	$response = array();

	
	$sql = "INSERT INTO cash_up(CashupDate, CashupTime, TotalSales, Cash, Difference, UserID) VALUES(:cash_up_date, :cash_up_time, :total_sales, :cash, :difference, :user_id)";
	$query = $conn->prepare($sql);
	$query->bindParam(':cash_up_date', $cash_up_date);
	$query->bindParam(':cash_up_time', $cash_up_time);
	$query->bindParam(':total_sales', $total_sales);
	$query->bindParam(':cash', $cash);
	$query->bindParam(':difference', $difference);
	$query->bindParam(':user_id', $user_id);
	$result = $query->execute();
		
		
	if(!$result)
	{
		$code = "cashup_failed";
		$message = "Error";
		array_push($response, array("code"=>$code, "message"=>$message));
	    echo json_encode($response);
	}
	elseif ($result) 
	{
        $code = "cashup_success";
        $message = "Cash up successfully";
        array_push($response, array("code"=>$code, "message"=>$message));
	    echo json_encode($response);
    }
?>
